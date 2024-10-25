package com.example.bakalarkaapp

import java.text.Normalizer

fun String.withoutDiacritics(): String {
    val regexUnaccent = "\\p{Block=Combiningdiacriticalmarks}+".toRegex()
    val tmp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return regexUnaccent.replace(tmp, "")
}

fun String.toDrawableId(): String {
    return this.withoutDiacritics().lowercase().replace(" ", "_")
}

fun String.shuffle(): String {
    val oldString = this.toCharArray()
    val wordArr = oldString.copyOf()
    wordArr.shuffle()
    while (wordArr.contentEquals(oldString)) {
        wordArr.shuffle()
    }
    return wordArr.joinToString("")
}