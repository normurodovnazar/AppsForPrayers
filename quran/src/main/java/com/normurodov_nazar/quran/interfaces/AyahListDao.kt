package com.normurodov_nazar.quran.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.normurodov_nazar.quran.models.Ayah
import com.normurodov_nazar.quran.models.AyahData

@Dao
interface AyahListDao {
    @Query("SELECT * FROM AyahData WHERE number=:i")
    fun getAyahsInSurah(i: Int): AyahData?
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAyahsToSurah(ayahData: AyahData)
}