package com.normurodov_nazar.quran.interfaces

import com.normurodov_nazar.quran.models.SurahInfo

interface SurahItemClick {
    fun onItemClick(surah: Any,isInfo: Boolean)
}