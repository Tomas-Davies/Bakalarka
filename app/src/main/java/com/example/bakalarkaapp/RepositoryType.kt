package com.example.bakalarkaapp

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