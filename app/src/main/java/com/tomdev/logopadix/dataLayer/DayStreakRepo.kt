package com.tomdev.logopadix.dataLayer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

class DayStreakRepo(
    private val dao: DailyActivityDao,
    private val scope: CoroutineScope
) {
    fun markPracticed(){
        scope.launch {
            val activity = DailyActivity(LocalDate.now().toString(), true, false)
            dao.update(activity)
        }
    }


    fun markFrozen(date: LocalDate){
        scope.launch {
            val activity = DailyActivity(date.toString(), false, true)
            dao.insert(activity)
        }
    }

    suspend fun getActivities(): List<DailyActivity>{
        return dao.getAll()
    }

    suspend fun getWeek(start: String, end: String): List<DailyActivity> {
        return dao.getWeek(start, end)
    }

    suspend fun addDayIfMissing(){
            val date = LocalDate.now().toString()
            val test = dao.getByDate(date)
            if (test == null){
                val activity = DailyActivity(date, false, false)
                dao.insert(activity)
            }
    }

    suspend fun getFirstDate(): LocalDate {
        val activity = dao.getFirstDate()
        return LocalDate.parse(activity.date)
    }

    suspend fun loadCurrentWeek(): List<DayInfo> {
        val firstDate = getFirstDate()
        val today = LocalDate.now()
        val weekFields = WeekFields.ISO
        val weekStart = today.with(weekFields.dayOfWeek(), 1)
        val weekEnd = weekStart.plusDays(6)
        val weekActivity = getWeek(weekStart.toString(), weekEnd.toString())
        val byDate = weekActivity.associateBy { LocalDate.parse(it.date) }
        val week = (0..6).map { offset ->
            val date = weekStart.plusDays(offset.toLong())
            val activity = byDate[date]
            val state = when {
                date.isBefore(firstDate) || date.isAfter(today) -> DayState.UNKNOWN
                activity == null -> DayState.MISSED
                activity.practiced && date == today -> DayState.CURRENT_PRACTICED
                date == today -> DayState.CURRENT
                activity.practiced -> DayState.PRACTICED
                activity.frozen -> DayState.FROZEN
                else -> DayState.MISSED
            }
            val label = date.dayOfWeek.getDisplayName(
                TextStyle.SHORT,
                Locale("cs", "CZ")
            )
            DayInfo(
                date = date,
                label = label,
                state = state
            )
        }
        return week
    }
}


data class DayInfo(
    val date: LocalDate,
    val label: String,
    val state: DayState
)

enum class DayState {
    CURRENT_PRACTICED,
    UNKNOWN,
    CURRENT,
    MISSED,
    PRACTICED,
    FROZEN
}