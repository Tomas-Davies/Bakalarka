package com.example.bakalarkaapp.presentationLayer.screens.tales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.viewModels.BaseViewModel
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.models.Tale

class TalesViewModel(app: LogoApp) : BaseViewModel(app) {
    private val repo = app.talesRepository
    val tales = repo.tales

    fun getTaleByIdx(idx: Int): Tale {
        return tales[idx]
    }
}

class TalesViewModelFactory(private val app: LogoApp) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TalesViewModel::class.java)){
            return TalesViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}