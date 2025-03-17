package com.example.bakalarkaapp.dataLayer.models

data class TaleImage(
    val imageName: String,
    val nounFormSoundName: String,
    val soundName: String,
)


class Tale(
    val name: String = "",
    val taleImageName: String = "",
    val images: List<TaleImage> = emptyList(),
    val textWithPlaceholders: String
) {
    companion object {
        const val ANNOTATION_KEY = "key"
    }
}