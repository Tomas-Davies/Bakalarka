package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import android.util.Log
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.models.Tale
import com.example.bakalarkaapp.utils.xml.TalesXmlParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class TalesRepo(val ctx: Context) {
    var tales = emptyList<Tale>()


    suspend fun loadData(){
        if (tales.isEmpty()){
            withContext(Dispatchers.IO){
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