package com.normurodov_nazar.quran.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.normurodov_nazar.quran.R
import com.normurodov_nazar.quran.interfaces.SurahItemClick
import com.normurodov_nazar.quran.models.SurahInfo

class SurahAdapter(val context: Context, val surahList: List<SurahInfo>, private val surahItemClick: SurahItemClick) : RecyclerView.Adapter<SurahAdapter.MyHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.surah_view,parent,false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(surahList[position],surahItemClick)
    }

    override fun getItemCount(): Int = surahList.size

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var numberOfSurah = itemView.findViewById<TextView>(R.id.numberOfSurah)
        private var nameOfSurah = itemView.findViewById<TextView>(R.id.surahName)
        private var info = itemView.findViewById<TextView>(R.id.info)
        private var infoBtn = itemView.findViewById<ImageButton>(R.id.infoBtn)

        fun setData(surah: SurahInfo, surahItemClick: SurahItemClick){
            itemView.setOnClickListener {
                surahItemClick.onItemClick(surah,false)
            }
            infoBtn.setOnClickListener{
                surahItemClick.onItemClick(surah,true)
            }
            numberOfSurah.text = surah.number.toString()
            nameOfSurah.text = surah.name
            info.text = itemView.context.getString(
                R.string.info,
                surah.numberOfAyahs.toString(),
                surah.revelationType
            )
        }
    }
}