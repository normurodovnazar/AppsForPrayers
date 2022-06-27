package com.normurodov_nazar.quran.interfaces

import androidx.room.*
import com.normurodov_nazar.quran.models.ReadingSurah

@Dao
interface ReadingListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToReadingList(readingSurah: ReadingSurah)
    @Query("SELECT * FROM ReadingSurah")
    fun getReadingList(): List<ReadingSurah>?
    @Delete
    fun deleteFromReading(readingSurah: ReadingSurah)
}