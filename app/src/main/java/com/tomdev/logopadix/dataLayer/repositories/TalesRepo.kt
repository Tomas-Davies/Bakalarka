package com.tomdev.logopadix.dataLayer.repositories

import android.content.Context
import android.util.Log
import com.tomdev.logopadix.R
import com.tomdev.logopadix.utils.xml.TalesXmlParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class TalesRepo(val ctx: Context) {
    var tales = emptyList<Tale>()


    suspend fun loadData(){
        if (tales.isEmpty()){
            withContext(Dispatchers.Default){
                tales = try {
                    TalesXmlParser.parse(ctx, R.xml.tales_data)
                } catch(e: Exception){
                    Log.e("TALE PARSING ERROR", e.stackTraceToString())
                    emptyList()
                }
            }
        }
    }
}

data class TaleImage(
    val imageName: String,
    val nounFormSoundName: String,
    val soundName: String,
)

class Tale(
    val name: String = "",
    val taleImageName: String = "",
    val images: List<TaleImage> = emptyList(),
    val textWithPlaceholders: String
) {
    companion object {
        const val ANNOTATION_KEY = "key"
    }
}