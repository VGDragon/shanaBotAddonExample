package com.shanabot.addon

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonParser

class SaveClass{
    var counter = 0

    fun save(): String{
        return Gson().toJson(this)
    }
    fun load(string: String){
        val saveClass: SaveClass = Gson().fromJson(JsonParser().parse(string))
        counter = saveClass.counter
    }
}