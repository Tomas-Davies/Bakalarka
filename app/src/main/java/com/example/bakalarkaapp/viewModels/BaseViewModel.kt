package com.example.bakalarkaapp.viewModels

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import kotlinx.coroutines.launch

abstract class BaseViewModel(app: LogoApp): ViewModel() {
    private val appContext = app.applicationContext

    fun playSound(soundId: Int) {
        viewModelScope.launch {
            val mediaPlayer = MediaPlayer.create(appContext, soundId)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mp ->
                mp.release()
            }
        }
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

    fun vibrate(vibrationEffect: VibrationEffect){
        viewModelScope.launch {
            val vibrator = getVibrator()
            vibrator.cancel()
            vibrator.vibrate(vibrationEffect)
        }
    }

    fun getDrawableId(drawableName: String): Int {
        var id = appContext.resources.getIdentifier(drawableName, "drawable", appContext.packageName)
        if (id == 0){
            id = appContext.resources.getIdentifier("dummy_img_500", "drawable", appContext.packageName)
        }
        return id
    }

    fun getSoundId(soundName: String): Int {
        var id = appContext.resources.getIdentifier(soundName, "raw", appContext.packageName)
        if (id == 0){
            id = appContext.resources.getIdentifier("wrong_answer", "raw", appContext.packageName)
        }
        return id
    }
}