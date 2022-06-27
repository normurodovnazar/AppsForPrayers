package com.normurodov_nazar.quran.models

import com.google.gson.annotations.SerializedName

data class AyahList(

    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: AyahData
)
