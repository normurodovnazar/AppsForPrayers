package com.normurodov_nazar.quran.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.normurodov_nazar.quran.R
import com.normurodov_nazar.quran.adapters.AyahAdapter
import com.normurodov_nazar.quran.databinding.ActivityAyahBinding
import com.normurodov_nazar.quran.interfaces.AyahItemClick
import com.normurodov_nazar.quran.models.*

class AyahActivity : AppCompatActivity() {
    private lateinit var model: AyahActivityModel
    private lateinit var b: ActivityAyahBinding
    private lateinit var i: Intent
    private lateinit var surahInfo: SurahInfo
    private var ayahNum: Int = -1
    private var infoPage = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAyahBinding.inflate(layoutInflater)
        setContentView(b.root)
        model = ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(AyahActivityModel::class.java)
        i = intent
        ayahNum = intent.getIntExtra("num",-1)-1
        Log.e("b", ayahNum.toString())
        surahInfo = SurahInfo(
            i.getIntExtra("number",0),
            i.getStringExtra("name")!!,
            i.getStringExtra("englishName")!!,
            i.getStringExtra("englishNameTranslation")!!,
            i.getIntExtra("numberOfAyahs",0),
            i.getStringExtra("revelationType")!!
        )
        showAyahs()
        b.retryBtn.setOnClickListener {
            showAyahs()
        }
    }

    private fun showAyahs() {
        model.getAyahList(surahInfo.number, this, this).observe(this) { i ->
            when (i.responseType) {
                ResponseType.error -> {
                    showErrorPage(i.errorMessage)
                }
                ResponseType.ayahs -> {
                    val ayahData = i.ayahData
                    if (ayahData != null) {
                        val list = ayahData.ayahs
                        val adapter = AyahAdapter(this, list, ayahData, object : AyahItemClick {
                            override fun onAyahClick(
                                ayah: Ayah,
                                ayahData: AyahData,
                                forInfo: Boolean
                            ) {
                                if (forInfo) {
                                    showInfoPage(ayah)
                                }else model.addAyahToReadingList(this@AyahActivity,surahInfo,ayah.numberInSurah).observe(this@AyahActivity){
                                    Toast.makeText(this@AyahActivity, R.string.saved, Toast.LENGTH_LONG).show()
                                }
                            }

                        })
                        setAyahAdapter(adapter)
                        model.addAyahDataToDatabase(this, ayahData)
                    } else showErrorPage(getString(R.string.unknown))
                }
                ResponseType.loading -> showLoadingPage()
                else -> showErrorPage(null)
            }
        }
    }

    override fun onBackPressed() {
        if (infoPage){
            showQuranPage()
            infoPage = false
        }else super.onBackPressed()
    }

    private fun showInfoPage(a: Ayah) {
        infoPage = true
        b.recycler.visibility = View.GONE
        b.progressBar.visibility = View.GONE
        b.errorPage.visibility = View.GONE
        b.infoPage.visibility = View.VISIBLE
        b.infoPage.text = getString(
            R.string.ayahInfo,
            a.number.toString(),
            a.numberInSurah.toString(),
            a.juz.toString(),
            a.manzil.toString(),
            a.page.toString(),
            a.ruku.toString(),
            a.hizbQuarter.toString(),
            if (a.sajda) "Yes" else "No"
        )
    }


    private fun setAyahAdapter(adapter: AyahAdapter) {
        showQuranPage()
        b.recycler.layoutManager = LinearLayoutManager(this)
        b.recycler.adapter = adapter
        Log.e("num", ayahNum.toString())
        waitAndScroll()
    }

    private fun waitAndScroll() {
        if (ayahNum!=-1){
            if (b.recycler.hasPendingAdapterUpdates()){
                model.delay().observe(this){
                    waitAndScroll()
                }
            }else b.recycler.scrollToPosition(ayahNum)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        return super.onKeyDown(keyCode, event)
    }

    private fun showErrorPage(error: String?) {
        b.infoPage.visibility = View.GONE
        b.recycler.visibility = View.GONE
        b.progressBar.visibility = View.GONE
        b.errorPage.visibility = View.VISIBLE
        if (error != null) b.errorText.text = error else b.errorText.text =
            getString(R.string.unknown)
    }

    private fun showQuranPage() {
        b.infoPage.visibility = View.GONE
        b.recycler.visibility = View.VISIBLE
        b.progressBar.visibility = View.GONE
        b.errorPage.visibility = View.GONE
    }

    private fun showLoadingPage() {
        b.infoPage.visibility = View.GONE
        b.recycler.visibility = View.GONE
        b.progressBar.visibility = View.VISIBLE
        b.errorPage.visibility = View.GONE
    }
}