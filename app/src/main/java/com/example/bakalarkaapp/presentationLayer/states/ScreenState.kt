package com.example.bakalarkaapp.presentationLayer.states


sealed class ScreenState {
    data object Running : ScreenState()
    data object Finished : ScreenState()
}