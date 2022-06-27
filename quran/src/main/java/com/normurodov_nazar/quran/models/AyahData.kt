package com.normurodov_nazar.quran.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AyahData(
    @PrimaryKey val number: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "englishName") val englishName: String,
    @ColumnInfo(name = "englishTranslation") var englishTranslation: String?,
    @ColumnInfo(name = "revelationType") val revelationType: String,
    @ColumnInfo(name = "numberOfAyahs") val numberOfAyahs: Int,
    @ColumnInfo(name = "ayahs") val ayahs: List<Ayah>
)
