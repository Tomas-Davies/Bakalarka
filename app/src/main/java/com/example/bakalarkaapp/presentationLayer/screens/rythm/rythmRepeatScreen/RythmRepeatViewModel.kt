package com.example.bakalarkaapp.presentationLayer.screens.rythm.rythmRepeatScreen

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.repositories.RythmRepeatRepo
import com.example.bakalarkaapp.viewModels.BaseViewModel
import com.example.bakalarkaapp.viewModels.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
    private val app: LogoApp
) : BaseViewModel(app) {

    private lateinit var _sounds: MutableStateFlow<List<RythmResource>>
    lateinit var sounds: StateFlow<List<RythmResource>>
        private set

    private val _currentlyPlayingIdx = MutableStateFlow(-1)
    val currentlyPlayngIdx = _currentlyPlayingIdx.asStateFlow()
    private var mediaPlayer: MediaPlayer? = null//MediaPlayer.create(app.applicationContext, getSoundId(""))

    init {
        viewModelScope.launch {
            repo.loadData()
            _sounds = MutableStateFlow(getRythmResource(repo.data))
            sounds = _sounds
            _screenState.value = ScreenState.Success
        }
    }

    fun playSound(soundId: Int, cardIdx: Int) {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
        }
        if (_currentlyPlayingIdx.value == cardIdx) {
            _currentlyPlayingIdx.update { -1 }
            mediaPlayer?.release()
            mediaPlayer = null
        } else {
            mediaPlayer = MediaPlayer.create(app.applicationContext, soundId)
            mediaPlayer?.start()
            _currentlyPlayingIdx.update { cardIdx }
            mediaPlayer?.setOnCompletionListener { mp ->
                mp.release()
                mediaPlayer = null
                if (_currentlyPlayingIdx.value == cardIdx) _currentlyPlayingIdx.update { -1 }
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
    private val app: LogoApp
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