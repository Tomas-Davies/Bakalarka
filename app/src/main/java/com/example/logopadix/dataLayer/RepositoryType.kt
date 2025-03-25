package com.example.logopadix.dataLayer

import com.example.logopadix.presentationLayer.screens.levels.LevelsScreen

/**
 * Defines the different types of repositories used for [LevelsScreen] navigation.
 * Also provides *key tag* for intent extras.
 */
enum class RepositoryType(val id: Int) {
    EYESIGHT_SEARCH(0),
    EYESIGHT_DIFFER(1),
    EYESIGHT_COMPARISON(2),
    EYESIGHT_SYNTHESIS(3),
    RYTHM_SYLLABLES(4);

    companion object {
        const val TAG = "REPO_TYPE"
    }
}