package com.normurodov_nazar.quran.fragments

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.normurodov_nazar.quran.interfaces.MyDatabase
import com.normurodov_nazar.quran.interfaces.QuranApi
import com.normurodov_nazar.quran.models.ResponseType
import com.normurodov_nazar.quran.models.Result
import com.normurodov_nazar.quran.models.SurahInfo
import com.normurodov_nazar.quran.models.SurahList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SurahListViewModel : ViewModel() {
    private val noData = "NoData"
    private val url = "http://api.alquran.cloud"
    private var resultSurah: MutableLiveData<Result>? = null
    private var db: MyDatabase? = null

    private fun getDataBase(context: Context): MyDatabase {
        if (db == null)
            db = Room.databaseBuilder(
                context,
                MyDatabase::class.java,
                "surahs"
            ).build()
        return db as MyDatabase
    }

    private fun getSurahsFromDatabase(context: Context): LiveData<Result> {
        print("invoked","getSurahsFromDatabase")
        postInitialResult()
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = getDataBase(context).getSurahDao()
            val surahs = userDao.getAllSurahs()
            if (surahs == null) postError(noData) else {
                if (surahs.isEmpty()) postError(noData) else postSurahs(surahs)
            }
        }
        return resultSurah as LiveData<Result>
    }



    fun addSurahsToDatabase(context: Context, surahList: List<SurahInfo>){
        CoroutineScope(Dispatchers.IO).launch {
            val surahDao = getDataBase(context).getSurahDao()
            surahDao.addSurahs(surahList)
            Log.e("added",surahList.size.toString())
        }
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

    private fun getSurahListFromApi(): LiveData<Result> {
        print("invoked","getSurahListFromApi")
        postInitialResult()
        getApi().getSurahList().enqueue(object : Callback<SurahList> {
            override fun onResponse(call: Call<SurahList>, response: Response<SurahList>) {
                if (response.isSuccessful) {
                    postSurahs(response.body()?.data)
                } else {
                    postError(null)
                }
            }

            override fun onFailure(call: Call<SurahList>, t: Throwable) {
                postError(t.localizedMessage)
            }
        })
        return resultSurah as LiveData<Result>
    }

    private fun print(a:String, b:String){
        Log.e(a,b)
    }

    fun getSurahList(context: Context, lifecycleOwner: LifecycleOwner): LiveData<Result> {
        print("invoked","getSurahList")
        postInitialResult()
        getSurahsFromDatabase(context).observe(lifecycleOwner){
            if (it.responseType== ResponseType.error && it.errorMessage==noData){
                getSurahListFromApi().observe(lifecycleOwner){}
            }
        }
        return resultSurah as LiveData<Result>
    }



    private fun postInitialResult(){
        print("invoked","postInitialResult")
        if (resultSurah==null) resultSurah = MutableLiveData()
        resultSurah?.postValue(Result(ResponseType.loading))
    }

    private fun postError(s: String?){
        print("invoked","postError")
        resultSurah?.postValue(Result(s, ResponseType.error))
    }


    fun postSurahs(surahs: List<SurahInfo>?){
        print("invoked","postSurahs")
        if (surahs==null) postError(null) else resultSurah?.postValue(
            Result(surahs,
                ResponseType.surahs)
        )
    }

}