package com.normurodov_nazar.quran.fragments

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.normurodov_nazar.quran.interfaces.MyDatabase
import com.normurodov_nazar.quran.models.ReadingSurah
import com.normurodov_nazar.quran.models.ResponseType
import com.normurodov_nazar.quran.models.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReadingListViewModel : ViewModel() {
    private val noData = "NoData"
    private var resultSurah: MutableLiveData<Result>? = null
    private var resultDelete: MutableLiveData<Boolean>? = null
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

    fun deleteFromDatabase(context: Context,readingSurah: ReadingSurah): LiveData<Boolean> {
        if (resultDelete==null) resultDelete = MutableLiveData()
        CoroutineScope(Dispatchers.IO).launch {
            getDataBase(context).getReadListDao().deleteFromReading(readingSurah)
            resultDelete!!.postValue(false)
        }
        return resultDelete as LiveData<Boolean>
    }

    fun getReadingList(context: Context): LiveData<Result>{
        postInitialResult()
        CoroutineScope(Dispatchers.IO).launch {
            val readListDao = getDataBase(context).getReadListDao()
            val readList = readListDao.getReadingList()
            if (readList==null) postError(noData) else
                if (readList.isEmpty()) postError(noData) else postSurahsInside(readList)
        }
        return resultSurah as LiveData<Result>
    }

    private fun print(a:String, b:String){
        Log.e(a,b)
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

    private fun postSurahsInside(surahs: List<ReadingSurah>?){
        print("invoked","postSurahs")
        if (surahs==null) postError(noData) else resultSurah?.postValue(
            Result(surahs, ResponseType.readingList,"")
        )
    }
}