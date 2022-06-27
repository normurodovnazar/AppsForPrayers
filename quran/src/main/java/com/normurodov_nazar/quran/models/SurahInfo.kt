package com.normurodov_nazar.quran.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SurahInfo(
    @PrimaryKey val number: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "englishName") val englishName: String,
    @ColumnInfo(name = "englishNameTranslation") val englishNameTranslation: String,
    @ColumnInfo(name = "numberOfAyahs") val numberOfAyahs: Int,
    @ColumnInfo(name = "revelationType") val revelationType: String
)