package com.example.logopadix.utils.xml

import android.content.Context
import com.example.logopadix.dataLayer.repositories.Tale
import com.example.logopadix.dataLayer.repositories.TaleImage
import org.xmlpull.v1.XmlPullParser

/**
 * Xml parser providing xml parsing functionality for tales_data.xml resource file
 * containing *mixed xml elements*.
 */
object TalesXmlParser {
    fun parse(ctx: Context, resId: Int): List<Tale> {
        val parser = ctx.resources.getXml(resId)
        val tales = mutableListOf<Tale>()
        var taleName = ""
        var frontImageName = ""
        val stringWithPlaceholders = StringBuilder()
        var content = mutableListOf<TaleImage>()
        var i = 0

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "name" -> {
                            taleName = parser.nextText()
                        }
                        "frontImageName" -> {
                            frontImageName = parser.nextText()
                        }
                        "content" -> {
                            content = mutableListOf()
                        }
                        "image" -> {
                            val imageName = parser.getAttributeValue(null, "imageName") ?: ""
                            val nounFormSoundName = parser.getAttributeValue(null, "nounFormSoundName") ?: ""
                            val soundName = parser.getAttributeValue(null, "soundName") ?: ""
                            val image = TaleImage(imageName, nounFormSoundName, soundName)
                            content.add(image)
                            stringWithPlaceholders.append("[${Tale.ANNOTATION_KEY}$i]")
                            i++
                        }
                        else -> {}
                    }
                }
                XmlPullParser.TEXT -> {
                    val txt = parser.text
                        .trim()
                        .replace(Regex("\n| +"), " ")

                    stringWithPlaceholders.append(txt)
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "tale"){
                        val tale = Tale(taleName, frontImageName, content, stringWithPlaceholders.toString())
                        stringWithPlaceholders.clear()
                        tales.add(tale)
                        i = 0
                    }
                }
            }
            parser.next()
        }
        return tales
    }
}