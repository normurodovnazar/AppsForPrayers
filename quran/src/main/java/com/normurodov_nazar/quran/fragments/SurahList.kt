package com.normurodov_nazar.quran.fragments

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.normurodov_nazar.quran.R
import com.normurodov_nazar.quran.activities.AyahActivity
import com.normurodov_nazar.quran.adapters.SurahAdapter
import com.normurodov_nazar.quran.databinding.FragmentSurahListBinding
import com.normurodov_nazar.quran.interfaces.SurahItemClick
import com.normurodov_nazar.quran.models.ResponseType
import com.normurodov_nazar.quran.models.SurahInfo

class SurahList : Fragment() {

    private lateinit var model: SurahListViewModel
    private lateinit var b: FragmentSurahListBinding
    private var surahs: List<SurahInfo>? = null
    private var adapter: SurahAdapter? = null
    private var showingInfo: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentSurahListBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(this).get(SurahListViewModel::class.java)
        showSurahList()
        b.retryBtn.setOnClickListener{
            showSurahList()
        }
    }

    private fun showSurahList() {
        if (surahs == null && adapter == null) context?.let { it ->
            model.getSurahList(it, this).observe(viewLifecycleOwner) {
                when (it.responseType) {
                    ResponseType.loading -> showLoadingPage()
                    ResponseType.error -> showErrorPage(it.errorMessage)
                    ResponseType.surahs -> {
                        surahs = it.surahList
                        if (surahs == null) showErrorPage(null) else {
                            adapter = SurahAdapter(requireContext(), surahs!!, object : SurahItemClick {
                                override fun onItemClick(surah: Any, isInfo: Boolean) {
                                    if (!isInfo) onClickSurah(surah as SurahInfo) else {
                                        showSurahInfo(surah as SurahInfo)
                                    }
                                }
                            })
                            setSurahAdapter(adapter!!)
                            model.addSurahsToDatabase(requireContext(), surahs!!)
                        }
                    }
                    else -> showErrorPage(null)
                }
            }
        } else adapter?.let { setSurahAdapter(it) }
    }

    private fun onClickSurah(surahInfo: SurahInfo) {
        val intent = Intent(context, AyahActivity::class.java)
        intent.putExtra("number", surahInfo.number)
        intent.putExtra("name",surahInfo.name)
        intent.putExtra("englishName", surahInfo.englishName)
        intent.putExtra("englishNameTranslation", surahInfo.englishNameTranslation)
        intent.putExtra("numberOfAyahs", surahInfo.numberOfAyahs)
        intent.putExtra("revelationType", surahInfo.revelationType)
        startActivity(intent)
    }

    fun onKeyDown(keyCode: Int): Boolean? {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            showErrorPage("SurahList")
            return true
        }
        return null
    }

    fun onBackPressed(): Boolean? {
        return if (showingInfo) {
            showQuranPage()
            showingInfo = false
            true
        } else null
    }

    private fun showSurahInfo(s: SurahInfo) {
        showingInfo = true
        b.surahInfo.visibility = View.VISIBLE
        b.recycler.visibility = View.GONE
        b.progressBar.visibility = View.GONE
        b.errorPage.visibility = View.GONE
        b.surahInfo.text = getString(
            R.string.surahInfo,
            s.number.toString(),
            s.name,
            s.englishName,
            s.englishNameTranslation,
            s.numberOfAyahs.toString(),
            s.revelationType
        )
    }

    private fun showErrorPage(error: String?) {
        b.surahInfo.visibility = View.GONE
        b.recycler.visibility = View.GONE
        b.progressBar.visibility = View.GONE
        b.errorPage.visibility = View.VISIBLE
        if (error != null) b.errorText.text = error else b.errorText.text =
            getString(R.string.unknown)
    }

    private fun showQuranPage() {
        b.surahInfo.visibility = View.GONE
        b.recycler.visibility = View.VISIBLE
        b.progressBar.visibility = View.GONE
        b.errorPage.visibility = View.GONE
    }

    private fun showLoadingPage() {
        b.surahInfo.visibility = View.GONE
        b.recycler.visibility = View.GONE
        b.progressBar.visibility = View.VISIBLE
        b.errorPage.visibility = View.GONE
    }

    private fun setSurahAdapter(adapter: SurahAdapter) {
        showQuranPage()
        b.recycler.layoutManager = LinearLayoutManager(context)
        b.recycler.adapter = adapter
    }
}