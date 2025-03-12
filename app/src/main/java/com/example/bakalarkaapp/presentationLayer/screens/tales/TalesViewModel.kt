package com.example.bakalarkaapp.presentationLayer.screens.tales

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


    fun getTaleByIdx(idx: Int): Tale {
        return tales[idx]
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