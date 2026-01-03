package com.tomdev.logopadix.utils.string

fun String.normalizedWithCh(): List<String> {
    val l = mutableListOf<String>()
    val word = this.lowercase()
    if (word.isEmpty()) return listOf()

    var i = 0
    while (i < word.length) {
        if (i < word.length - 1 && word[i] == 'c' && word[i + 1] == 'h') {
            l.add("ch")
            i += 2
        } else {
            val str = word[i].toString()
            l.add(str)
            i++
        }
    }
    return l
}


fun String.startsWithCh(prefix: String): Boolean {
    if (this.length < prefix.length) return false

    val normalizedWord = this.normalizedWithCh()
    val normalizedPrefix = prefix.normalizedWithCh()
    var i = 0
    while (i < normalizedPrefix.size){
        if (normalizedWord[i] != normalizedPrefix[i]) return false
        i++
    }
    return true
}


fun String.endsWithCh(suffix: String): Boolean {
    return this.reversed().startsWithCh(suffix.reversed())
}