package com.example.logopadix.presentationLayer.states

sealed class ScreenState {
    data object Loading: ScreenState()
    data object Success: ScreenState()
}