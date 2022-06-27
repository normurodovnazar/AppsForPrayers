package com.normurodov_nazar.quran.interfaces

import com.normurodov_nazar.quran.models.Ayah
import com.normurodov_nazar.quran.models.AyahData

interface AyahItemClick {
    fun onAyahClick(ayah: Ayah,ayahData: AyahData,forInfo: Boolean)
}