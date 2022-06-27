package com.normurodov_nazar.quran.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.normurodov_nazar.quran.R
import com.normurodov_nazar.quran.interfaces.DeleteItem
import com.normurodov_nazar.quran.interfaces.SurahItemClick
import com.normurodov_nazar.quran.models.ReadingSurah

class ReadingAdapter(val context: Context, val readingList: List<ReadingSurah>,
                     private val surahItemClick: SurahItemClick, private val deleteItem: DeleteItem):
    RecyclerView.Adapter<ReadingAdapter.MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.surah_view,parent,false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(readingList[position],surahItemClick, deleteItem)
    }

    override fun getItemCount(): Int = readingList.size

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var numberOfSurah = itemView.findViewById<TextView>(R.id.numberOfSurah)
        private var nameOfSurah = itemView.findViewById<TextView>(R.id.surahName)
        private var info = itemView.findViewById<TextView>(R.id.info)
        private var infoBtn = itemView.findViewById<ImageButton>(R.id.infoBtn)
        private var deleteBtn = itemView.findViewById<ImageButton>(R.id.deleteBtn)

        fun setData(s: ReadingSurah, surahItemClick: SurahItemClick,deleteItem: DeleteItem){
            deleteBtn.visibility = View.VISIBLE
            deleteBtn.setOnClickListener {
                deleteItem.onClickDelete(s)
            }
            itemView.setOnClickListener{
                surahItemClick.onItemClick(s,false)
            }
            infoBtn.setOnClickListener{
                surahItemClick.onItemClick(s,true)
            }
            numberOfSurah.text = s.number.toString()
            nameOfSurah.text = s.name
            info.text = itemView.context.getString(
                R.string.info,
                s.numberOfAyahs.toString(),
                s.revelationType
            )
        }
    }
}