package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import android.util.Log
import com.example.bakalarkaapp.dataLayer.models.IModel
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Abstract base repository for asynchronous resource data loading and deserialization.
 *
 * @param T The type of the class that is going to be mapped.
 * @param R The type of the individual data items that will be exposed by the repository.
 * @param ctx The Android Context used to access resources.
 * @param resourceId The resource ID of the raw XML file to be loaded.
 * @param dataClass The Class object representing type T, used for deserialization.
 */
abstract class ResourceMappedRepository<T : IModel<R>, R>(
    private val ctx: Context,
    private val resourceId: Int,
    private val dataClass: Class<T>
)  {
    private var mappedClass: T? = null
    var data: List<R> = emptyList()
        private set


    suspend fun loadData(){
        if (mappedClass == null){
            withContext(Dispatchers.IO){
                mappedClass = mapXml(ctx, resourceId, dataClass)
            }
        }
        data = mappedClass?.data ?: emptyList()
    }


    /**
     * Deserializes XML data from a raw resource into an object of type T.
     *
     * @param ctx The Android Context used to access resources.
     * @param resourceId The resource ID of the raw XML file to be loaded.
     * @param mappingClass The Class object representing the target type for deserialization.
     * @return The deserialized object, or null if an error occurred.
     */
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