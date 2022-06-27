package com.normurodov_nazar.quran.interfaces

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.normurodov_nazar.quran.adapters.Converters
import com.normurodov_nazar.quran.models.Ayah
import com.normurodov_nazar.quran.models.AyahData
import com.normurodov_nazar.quran.models.ReadingSurah
import com.normurodov_nazar.quran.models.SurahInfo

@Database(entities = [SurahInfo::class,AyahData::class,Ayah::class,ReadingSurah::class],version = 2)
@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase(){
    abstract fun getSurahDao(): SurahInfoDao
    abstract fun getAyahDao(): AyahListDao
    abstract fun getReadListDao(): ReadingListDao
}