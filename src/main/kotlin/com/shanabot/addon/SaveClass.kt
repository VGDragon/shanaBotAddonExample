package com.shanabot.addon

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonParser


class SaveClass{
    var counter = 0

    // convert this class (with variables) to a json
    fun save(): String{
        return Gson().toJson(this)
    }
    // convert a Json to this class (with variables)
    fun load(string: String){
        val saveClass: SaveClass = Gson().fromJson(JsonParser().parse(string))
        counter = saveClass.counter
    }
}