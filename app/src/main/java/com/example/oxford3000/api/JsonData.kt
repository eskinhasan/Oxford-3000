package com.example.oxford3000.api

import android.content.Context

class JsonData {
    companion object {
        fun getJsonDataFromAsset(context: Context,fileName:String):String{
            val jsonString:String
            try{
                jsonString=context.assets.open(fileName).bufferedReader().use { it.readText()
                }
            }catch (e:Exception)
            {
                e.printStackTrace()
                return ""
            }
            return jsonString
        }

    }
}