package com.example.bakalarkaapp

/**
 * Defines the different types of themes used in application.
 * Also provides *key tag* for intent extras.
 */
enum class ThemeType(val id: Int) {
    THEME_SPEECH(0),
    THEME_EYESIGHT(1),
    THEME_RYTHM(2),
    THEME_HEARING(3),
    THEME_TALES(4);

    companion object {
        const val TAG = "THYME_TYPE"
    }

}