package com.normurodov_nazar.quran.models

data class SurahList(
    val code: Int,
    val status: String,
    val data: List<SurahInfo>
)
