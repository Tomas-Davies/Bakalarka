package com.example.bakalarkaapp.utils.xml

import android.content.Context
import com.example.bakalarkaapp.dataLayer.models.Tale
import com.example.bakalarkaapp.dataLayer.models.TaleContent
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
        var content = mutableListOf<TaleContent>()

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
                            val image = TaleContent.Image(imageName, nounFormSoundName, soundName)
                            content.add(image)
                        }
                        else -> {}
                    }
                }
                XmlPullParser.TEXT -> {
                    val words = parser.text.split(Regex("\\s+"))
                    words.forEach { word ->
                        val w = word.replace("\n", "")
                        val contentWord = TaleContent.Word(w)
                        content.add(contentWord)
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "tale"){
                        val tale = Tale(taleName, frontImageName, content)
                        tales.add(tale)
                    }
                }
            }
            parser.next()
        }
        return tales
    }
}