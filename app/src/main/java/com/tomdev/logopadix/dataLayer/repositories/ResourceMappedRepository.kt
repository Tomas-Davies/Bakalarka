package com.tomdev.logopadix.dataLayer.repositories

import android.content.Context
import android.util.Log
import com.tomdev.logopadix.dataLayer.IData
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Abstract base repository for asynchronous resource data loading and deserialization.
 *
 * @param TData The type of the class that is going to be mapped.
 * @param TItem The type of the individual data items that will be exposed by the repository.
 * @param ctx The Android Context used to access resources.
 * @param resourceId The resource ID of the raw XML file to be loaded.
 * @param dataClass The Class object representing type T, used for deserialization.
 */
open class ResourceMappedRepository<TData : IData<TItem>, TItem>(
    private val ctx: Context,
    private val resourceId: Int,
    private val dataClass: Class<TData>
) : IRepository<TItem>
{
    private var mappedClass: TData? = null
    var data: List<TItem> = emptyList()

    override suspend fun loadData(): List<TItem> {
        if (mappedClass == null){
            withContext(Dispatchers.Default){
                mappedClass = mapXml(ctx, resourceId, dataClass)
            }
        }
        val result = mappedClass?.data ?: emptyList()
        data = result
        return result
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