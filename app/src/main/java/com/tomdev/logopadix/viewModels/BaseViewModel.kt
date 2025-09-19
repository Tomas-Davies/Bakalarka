package com.tomdev.logopadix.viewModels

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.tomdev.logopadix.presentationLayer.states.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


/**
 * Abstract base ViewModel that provides common functionality for audio, vibration, and resource management.
 *
 * This class serves as a foundation for majority of ViewModels in the application.
 *
 * It manages:
 * - Playing sound effects.
 * - Creating vibration feedback.
 * - Accessing drawable resources.
 *
 * @param app The application instance that provides the application context.
 */
abstract class BaseViewModel(app: com.tomdev.logopadix.LogoApp) : ViewModel() {
    private val appContext = app.applicationContext
    protected val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    fun playSound(soundId: Int) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(appContext, soundId)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { mp ->
            mp.release()
        }
    }

    fun playSoundByName(name: String) {
        val soundId = getSoundId(name)
        playSound(soundId)
    }


    fun vibrate(vibrationEffect: VibrationEffect) {
        val vibrator = getVibrator()
        vibrator.cancel()
        vibrator.vibrate(vibrationEffect)
    }

    fun getDrawableId(drawableName: String): Int {
        var id = appContext.resources.getIdentifier(drawableName, "drawable", appContext.packageName)
        if (id == 0) {
            id = appContext.resources.getIdentifier("dummy_image_500", "drawable", appContext.packageName)
        }
        return id
    }

    fun getSoundId(soundName: String): Int {
        var id = appContext.resources.getIdentifier(soundName, "raw", appContext.packageName)
        if (id == 0) {
            id = appContext.resources.getIdentifier("wrong_answer", "raw", appContext.packageName)
        }
        return id
    }


    private fun getVibrator(): Vibrator {
        val vibrator: Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                appContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibrator = vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            vibrator = appContext.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
        }
        return vibrator
    }
}