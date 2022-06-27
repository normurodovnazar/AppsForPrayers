package com.normurodov_nazar.quran.adapters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.normurodov_nazar.quran.models.Ayah

class Converters {
    @TypeConverter
    fun listToJson(value: List<Ayah>): String{
        return Gson().toJson(value)
    }
    @TypeConverter
    fun jsonToList(value: String): List<Ayah>{
        return Gson().fromJson(value,Array<Ayah>::class.java).toList()
    }
}