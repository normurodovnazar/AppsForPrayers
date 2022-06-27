package com.normurodov_nazar.quran.interfaces

import com.normurodov_nazar.quran.models.ReadingSurah

interface DeleteItem {
    fun onClickDelete(readingSurah: ReadingSurah)
}