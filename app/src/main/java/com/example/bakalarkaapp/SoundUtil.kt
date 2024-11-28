package com.example.bakalarkaapp

import android.content.Context
import android.media.MediaPlayer

fun playSound(ctx: Context, soundId: Int) {
    val mediaPlayer = MediaPlayer.create(ctx, soundId)
    mediaPlayer.start()
    mediaPlayer.setOnCompletionListener { mp ->
        mp.release()
    }
}

fun playResultSound(ctx: Context, result: Boolean){
    if (result){
        playSound(ctx, R.raw.correct_answer)
    } else {
        playSound(ctx, R.raw.wrong_answer)
    }
}