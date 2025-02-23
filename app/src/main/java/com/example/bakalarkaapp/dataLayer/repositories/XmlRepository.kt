package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import android.util.Log
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

abstract class XmlRepository<T, R>(
    ctx: Context,
    resourceId: Int,
    dataClass: Class<T>
)  {
    protected val mappedClass: T? = mapXml(ctx, resourceId, dataClass)
    abstract val data: List<R>


    private fun <T> mapXml(ctx: Context, resourceId: Int, mappingClass: Class<T>): T? {
        val xmlData = ctx.resources.openRawResource(resourceId)
        val xmlMapper = XmlMapper()
        xmlMapper.registerKotlinModule()
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        return try {
            xmlMapper.readValue(xmlData, mappingClass)
        } catch (e: Exception){
            e.printStackTrace()
            Log.e(
                "Error mapping XML",
                "Message: ${e.message}\nCause: ${e.cause}\nStack trace: ${e.stackTraceToString()}"
            )
            null
        }
    }
}