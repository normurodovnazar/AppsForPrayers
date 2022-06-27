package com.normurodov_nazar.quran.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.normurodov_nazar.quran.R
import com.normurodov_nazar.quran.interfaces.AyahItemClick
import com.normurodov_nazar.quran.models.Ayah
import com.normurodov_nazar.quran.models.AyahData

class AyahAdapter(private val context: Context, private val ayahList: List<Ayah>,private val ayahData: AyahData,private val ayahItemClick: AyahItemClick): RecyclerView.Adapter<AyahAdapter.AyahHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyahHolder {
        return AyahHolder(LayoutInflater.from(context).inflate(R.layout.ayah_view,parent,false))
    }

    override fun onBindViewHolder(holder: AyahHolder, position: Int) {
        holder.setAyah(ayahList[position], ayahData,ayahItemClick)
    }

    override fun getItemCount(): Int = ayahList.size

    class AyahHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var ayahNumber: TextView = itemView.findViewById(R.id.ayahNumber)
        private var arabic: TextView = itemView.findViewById(R.id.arabicText)
        private var saveBtn: ImageButton = itemView.findViewById(R.id.save)
        private var infoBtn: ImageButton = itemView.findViewById(R.id.infoBtn)

        fun setAyah(ayah: Ayah,ayahData: AyahData,ayahItemClick: AyahItemClick){
            ayahNumber.text = ayah.numberInSurah.toString()
            arabic.text = ayah.text
            saveBtn.setOnClickListener{
                ayahItemClick.onAyahClick(ayah,ayahData,false)
            }
            infoBtn.setOnClickListener{
                ayahItemClick.onAyahClick(ayah,ayahData,true)
            }
        }
    }
}