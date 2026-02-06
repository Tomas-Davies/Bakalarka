package com.tomdev.logopadix.presentationLayer.screens.rythm.rythmRepeatScreen

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tomdev.logopadix.dataLayer.repositories.RythmRepeatRepo
import com.tomdev.logopadix.presentationLayer.states.ScreenState
import com.tomdev.logopadix.viewModels.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.linc.amplituda.Amplituda
import com.linc.amplituda.callback.AmplitudaErrorListener
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class RythmResource(
    val soundId: Int,
    val amplitudes: List<Int>
)

class RythmRepeatViewModel(
    private val repo: RythmRepeatRepo,
    private val app: com.tomdev.logopadix.LogoApp
) : BaseViewModel(app) {

    private val dailyActivityRepo = app.dailyActivityRepo
    private lateinit var _sounds: MutableStateFlow<List<RythmResource>>
    lateinit var sounds: StateFlow<List<RythmResource>>
        private set

    private val _currentlyPlayingIdx = MutableStateFlow(-1)
    val currentlyPlayngIdx = _currentlyPlayingIdx.asStateFlow()
    private var mediaPlayer: MediaPlayer? = null//MediaPlayer.create(app.applicationContext, getSoundId(""))

    init {
        viewModelScope.launch {
            var loadedData = repo.loadData()
            _sounds = MutableStateFlow(getRythmResource(loadedData))
            sounds = _sounds
            _screenState.value = ScreenState.Success
            dailyActivityRepo.markPracticed()
        }
    }

    fun playSound(soundId: Int, cardIdx: Int) {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
        }
        if (_currentlyPlayingIdx.value == cardIdx) {
            _currentlyPlayingIdx.value = -1
            mediaPlayer?.release()
            mediaPlayer = null
        } else {
            mediaPlayer = MediaPlayer.create(app.applicationContext, soundId)
            mediaPlayer?.start()
            _currentlyPlayingIdx.value = cardIdx
            mediaPlayer?.setOnCompletionListener { mp ->
                mp.release()
                mediaPlayer = null
                if (_currentlyPlayingIdx.value == cardIdx) _currentlyPlayingIdx.value = -1
            }
        }
    }


    private suspend fun getRythmResource(soundNames: List<String>): List<RythmResource> {
        val amplituda = Amplituda(app.applicationContext)

        return coroutineScope {
            soundNames.map { name ->
                async {
                    val soundId = getSoundId(name)
                    val amplitudes = amplituda.processAudio(soundId).get(
                        AmplitudaErrorListener { e ->
                            Log.e(
                                "Amplituda Exception",
                                "Message: ${e.message}\nCause: ${e.cause}\nStack trace: ${e.stackTraceToString()}"
                            )
                        }
                    ).amplitudesAsList()
                    RythmResource(soundId, amplitudes)
                }
            }.awaitAll()
        }
    }
}


class RythmRepeatViewModelFactory(
    private val repo: RythmRepeatRepo,
    private val app: com.tomdev.logopadix.LogoApp
) : ViewModelProvider.Factory
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RythmRepeatViewModel::class.java)) {
            return RythmRepeatViewModel(repo, app) as T
        }
        throw IllegalArgumentException("Unknown viewModel class: $modelClass")
    }
}