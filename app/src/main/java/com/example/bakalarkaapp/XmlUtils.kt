package com.example.bakalarkaapp

import android.content.Context
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.xmlpull.v1.XmlPullParser

class XmlUtils {

    fun <T:Any> parseXmlData(context: Context, xmlFileName: String, mappingClass: Class<T>): T{
        val xmlString = readXmlFile(context, xmlFileName)
        val xmlMapper = XmlMapper(JacksonXmlModule().apply { setDefaultUseWrapper(false) })
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val dataList = xmlMapper.readValue(xmlString, mappingClass)
        return dataList
    }

    private fun readXmlFile(context: Context, fileName: String): String {
        val resId = context.resources.getIdentifier(fileName, "xml", context.packageName)
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