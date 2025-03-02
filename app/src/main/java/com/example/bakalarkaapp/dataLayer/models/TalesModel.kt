package com.example.bakalarkaapp.dataLayer.models

/**
 * Represents possible content types within a Tale: either [Word] or [Image].
 *
 * TaleContent serves as a sealed interface that restricts possible content types,
 * ensuring type safety throughout the application.
 */
sealed interface TaleContent {
    /**
     * Represents textual content within a Tale.
     *
     * @property value The string text content to be displayed or processed.
     */
    data class Word(val value: String): TaleContent

    /**
     * Represents visual content within a Tale, along with its associated audio resources.
     *
     * @property imageName The name of the drawable resource file.
     * @property nounFormSoundName The name of the mp3 raw resource file containing the noun form pronunciation.
     * @property soundName The name of the mp3 raw resource file containing the standard pronunciation.
     */
    data class Image(
        val imageName: String,
        val nounFormSoundName: String,
        val soundName: String,
    ): TaleContent
}


/**
 * Describes a Tale structure
 *
 * @property name A name of the Tale.
 * @property taleImageName A drawable resource name of the main image.
 * @property content A list of [TaleContent].
 */
data class Tale(
    val name: String = "",
    val taleImageName: String = "",
    val content: List<TaleContent> = emptyList()
)