package com.tomdev.logopadix.presentationLayer.screens.tales

import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tomdev.logopadix.viewModels.BaseViewModel
import com.tomdev.logopadix.dataLayer.repositories.Tale
import com.tomdev.logopadix.dataLayer.repositories.TalesRepo
import com.tomdev.logopadix.presentationLayer.states.ScreenState
import kotlinx.coroutines.launch


class TalesViewModel(
    private val repo: TalesRepo,
    app: com.tomdev.logopadix.LogoApp
) : BaseViewModel(app)
{
    lateinit var tales: List<Tale>
        private set

    init {
        viewModelScope.launch {
            repo.loadData()
            tales = repo.tales
            _screenState.value = ScreenState.Success
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
    private val app: com.tomdev.logopadix.LogoApp
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