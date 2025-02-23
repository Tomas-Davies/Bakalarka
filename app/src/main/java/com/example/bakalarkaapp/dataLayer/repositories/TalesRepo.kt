package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import android.util.Log
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.utils.xml.TalesXmlParser


class TalesRepo(val ctx: Context) {
    var tales = try {
        TalesXmlParser.parse(ctx, R.xml.tales_data)
    } catch(err: Exception){
        Log.e("TALE PARSING ERROR", err.stackTraceToString())
        emptyList()
    }
}