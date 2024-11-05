package com.example.bakalarkaapp

import android.content.Context
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.xmlpull.v1.XmlPullParser

object XmlUtils {
    fun <T:Any> parseXmlData(context: Context, resourceId: Int, mappingClass: Class<T>): T{
        val xmlString = readXmlFile(context, resourceId)
        val xmlMapper = XmlMapper().apply {
            registerKotlinModule()
        }

        return xmlMapper.readValue(xmlString, mappingClass)
    }

    private fun readXmlFile(context: Context, resId: Int): String {
        val xmlResource = context.resources.getXml(resId)
        val stringBuilder = StringBuilder()

        while (xmlResource.eventType != XmlPullParser.END_DOCUMENT) {
            when (xmlResource.eventType) {
                XmlPullParser.START_TAG -> stringBuilder.append("<${xmlResource.name}>")
                XmlPullParser.TEXT -> stringBuilder.append(xmlResource.text)
                XmlPullParser.END_TAG -> stringBuilder.append("</${xmlResource.name}>")            }
            xmlResource.next()
        }
        return stringBuilder.toString()
    }

}