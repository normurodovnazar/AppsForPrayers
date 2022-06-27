package com.normurodov_nazar.quran.models

class Result {
    var errorMessage: String? = null
    var surahList: List<SurahInfo>? = null
    var readingList: List<ReadingSurah>? = null
    var responseType: ResponseType
    var ayahData: AyahData? = null
    constructor(readingList: List<ReadingSurah>?,responseType: ResponseType,s: String){
        this.readingList = readingList
        this.responseType = responseType
    }
    constructor(surahList: List<SurahInfo>?,responseType: ResponseType){
        this.surahList = surahList
        this.responseType = responseType
    }
    constructor(error: String?,responseType: ResponseType){
        this.errorMessage = error
        this.responseType = responseType
    }
    constructor(responseType: ResponseType){
        this.responseType = responseType
    }
    constructor(ayahData: AyahData?,responseType: ResponseType,i: Byte){
        this.ayahData = ayahData
        this.responseType = responseType
    }
}