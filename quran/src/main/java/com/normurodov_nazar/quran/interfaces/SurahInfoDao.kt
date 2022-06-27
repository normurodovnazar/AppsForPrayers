package com.normurodov_nazar.quran.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.normurodov_nazar.quran.models.SurahInfo

@Dao
interface SurahInfoDao {
    @Query("SELECT * FROM SurahInfo")
    fun getAllSurahs(): List<SurahInfo>?
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addSurahs(surahs: List<SurahInfo>)
}