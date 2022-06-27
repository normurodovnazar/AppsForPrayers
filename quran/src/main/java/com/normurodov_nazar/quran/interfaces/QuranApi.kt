package com.normurodov_nazar.quran.interfaces

import com.normurodov_nazar.quran.models.AyahList
import com.normurodov_nazar.quran.models.SurahList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranApi {
    @GET("v1/surah")
    fun getSurahList() : Call<SurahList>

    @GET("/v1/surah/{t}")
    fun getAyahs(@Path("t")i: Int) : Call<AyahList>
}