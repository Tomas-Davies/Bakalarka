package com.example.bakalarkaapp.utils.string

import java.text.Normalizer

fun String.withoutDiacritics(): String {
    val regexUnaccent = "\\p{Block=Combiningdiacriticalmarks}+".toRegex()
    val diacritiacalSymbols = Normalizer.normalize(this, Normalizer.Form.NFD)
    return regexUnaccent.replace(diacritiacalSymbols, "")
}

fun String.toDrawableId(): String {
    return this.withoutDiacritics().lowercase().replace(" ", "_")
}