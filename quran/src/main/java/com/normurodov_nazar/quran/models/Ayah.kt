package com.normurodov_nazar.quran.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Ayah(
    @PrimaryKey @SerializedName("number") val number: Int,
    @ColumnInfo(name = "text") @SerializedName("text") val text: String,
    @ColumnInfo(name = "numberInSurah") @SerializedName("numberInSurah") val numberInSurah: Int,
    @ColumnInfo(name = "juz") @SerializedName("juz") val juz: Int,
    @ColumnInfo(name = "manzil") @SerializedName("manzil") val manzil: Int,
    @ColumnInfo(name = "page") @SerializedName("page") val page: Int,
    @ColumnInfo(name = "ruku") @SerializedName("ruku") val ruku: Int,
    @ColumnInfo(name = "hizbQuarter") @SerializedName("hizbQuarter") val hizbQuarter: Int,
    @ColumnInfo(name = "sajda") @SerializedName("sajda") val sajda: Boolean
)
