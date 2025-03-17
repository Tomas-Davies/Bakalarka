package com.example.bakalarkaapp.presentationLayer.screens.tales

import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.viewModels.BaseViewModel
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.models.Tale
import com.example.bakalarkaapp.dataLayer.repositories.TalesRepo
import kotlinx.coroutines.launch

class TalesViewModel(
    private val repo: TalesRepo,
    app: LogoApp
) : BaseViewModel(app)
{
    lateinit var tales: List<Tale>
        private set

    init {
        viewModelScope.launch {
            repo.loadData()
            tales = repo.tales
            dataLoaded()
        }
    }


    fun getTaleAndAnnotatedString(taleIdx: Int): Pair<Tale, AnnotatedString> {
        val tale = tales[taleIdx]
        val splitPattern = Regex("\\[|]")
        val taleText = tale.textWithPlaceholders.split(splitPattern)
        val keyPattern = Regex("${Tale.ANNOTATION_KEY}\\d+")
        val annotatedString = buildAnnotatedString {
            taleText.forEach { s ->
                if (keyPattern.matches(s)) {
                    appendInlineContent(s, "[IMAGE]")
                } else {
                    append(s)
                }
            }
        }
        return Pair(tale, annotatedString)
    }
}


class TalesViewModelFactory(
    private val repo: TalesRepo,
    private val app: LogoApp
) : ViewModelProvider.Factory
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TalesViewModel::class.java)){
            return TalesViewModel(repo, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}