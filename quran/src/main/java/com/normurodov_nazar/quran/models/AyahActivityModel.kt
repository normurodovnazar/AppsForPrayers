package com.normurodov_nazar.quran.models

import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.normurodov_nazar.quran.interfaces.MyDatabase
import com.normurodov_nazar.quran.interfaces.QuranApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AyahActivityModel: ViewModel(){
    private val noData = "NoData"
    private var db: MyDatabase? = null
    private val url = "http://api.alquran.cloud"
    private var resultAyah: MutableLiveData<Result>? = null
    private var resultSaved: MutableLiveData<Boolean>? = null
    private var delayEnd: MutableLiveData<Boolean>? = null

    private fun getDataBase(context: Context): MyDatabase {
        if (db == null)
            db = Room.databaseBuilder(
                context,
                MyDatabase::class.java,
                "surahs"
            ).build()
        return db as MyDatabase
    }

    fun postAyahs(ayahData: AyahData?){
        print("invoked","postAyahs")
        if (ayahData==null) postErrorAyah(null) else resultAyah?.postValue(Result(ayahData,ResponseType.ayahs,0))
    }
    private fun postErrorAyah(s: String?){
        print("invoked","postError")
        resultAyah?.postValue(Result(s, ResponseType.error))
    }
    private fun postInitialResultForAyah(){
        print("invoked","postInitialResult")
        if (resultAyah==null) resultAyah = MutableLiveData()
        resultAyah?.postValue(Result(ResponseType.loading))
    }
    fun getAyahList(i: Int, context: Context, lifecycleOwner: LifecycleOwner): LiveData<Result> {
        postInitialResultForAyah()
        getAyahsFromDatabase(context,i).observe(lifecycleOwner){
            if (it.responseType==ResponseType.error && it.errorMessage==noData){
                getAyahsOfSurahFromApi(i).observe(lifecycleOwner){}
            }
        }
        return resultAyah as LiveData<Result>
    }
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getApi(): QuranApi {
        return getRetrofit().create(QuranApi::class.java)
    }
    private fun getAyahsOfSurahFromApi(i: Int): LiveData<Result> {
        postInitialResultForAyah()
        getApi().getAyahs(i).enqueue(object : Callback<AyahList> {
            override fun onResponse(call: Call<AyahList>, response: Response<AyahList>) {
                print("res",response.toString())
                if (response.isSuccessful) {
                    postAyahs(response.body()?.data)
                } else postErrorAyah(null)
            }

            override fun onFailure(call: Call<AyahList>, t: Throwable) {
                postErrorAyah(t.localizedMessage)
            }
        })
        return resultAyah as LiveData<Result>
    }
    fun addAyahDataToDatabase(context: Context, ayahData: AyahData){
        CoroutineScope(Dispatchers.IO).launch {
            val ayahDao = getDataBase(context).getAyahDao()
            if (ayahData.englishTranslation == null) ayahData.englishTranslation = ""
            ayahDao.addAyahsToSurah(ayahData)
        }
    }
    fun addAyahToReadingList(context: Context,s: SurahInfo,ayahNumber: Int):LiveData<Boolean>{
        if (resultSaved==null) resultSaved = MutableLiveData()
        CoroutineScope(Dispatchers.IO).launch {
            val readingListDao = getDataBase(context).getReadListDao()
            val readingSurah = ReadingSurah(
                s.number,
                s.name,
                s.englishName,
                s.englishNameTranslation,
                s.numberOfAyahs,
                s.revelationType,
                ayahNumber
            )
            readingListDao.addToReadingList(readingSurah)
            resultSaved!!.postValue(true)
        }
        return resultSaved as LiveData<Boolean>
    }
    private fun getAyahsFromDatabase(context: Context, i: Int): LiveData<Result>{
        postInitialResultForAyah()
        CoroutineScope(Dispatchers.IO).launch {
            val ayahDao = getDataBase(context).getAyahDao()
            val ayahData = ayahDao.getAyahsInSurah(i)
            if (ayahData==null) postErrorAyah(noData) else{
                if (ayahData.ayahs.isEmpty()) postErrorAyah(noData) else{
                    print("ayahs",ayahData.numberOfAyahs.toString())
                    postAyahs(ayahData)
                }
            }
        }
        return resultAyah as LiveData<Result>
    }

    fun print(a:String,b:String){
        Log.e(a,b)
    }

    fun delay(): LiveData<Boolean> {
        if (delayEnd==null) delayEnd = MutableLiveData()
        CoroutineScope(Dispatchers.IO).launch {
            kotlinx.coroutines.delay(100)
            delayEnd!!.postValue(true)
        }
        return delayEnd as LiveData<Boolean>
    }
}