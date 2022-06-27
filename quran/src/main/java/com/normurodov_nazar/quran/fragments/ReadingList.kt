package com.normurodov_nazar.quran.fragments

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.normurodov_nazar.quran.R
import com.normurodov_nazar.quran.activities.AyahActivity
import com.normurodov_nazar.quran.adapters.ReadingAdapter
import com.normurodov_nazar.quran.databinding.FragmentReadingListBinding
import com.normurodov_nazar.quran.interfaces.DeleteItem
import com.normurodov_nazar.quran.interfaces.SurahItemClick
import com.normurodov_nazar.quran.models.ReadingSurah
import com.normurodov_nazar.quran.models.ResponseType

class ReadingList : Fragment() {
    private var surahs: List<ReadingSurah>? = null
    private var adapter: ReadingAdapter? = null
    private var showingInfo: Boolean = false

    private lateinit var model: ReadingListViewModel
    private lateinit var b: FragmentReadingListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentReadingListBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(this).get(ReadingListViewModel::class.java)
        showReadingList()
    }

    private fun showReadingList() {
        if (surahs == null && adapter == null) context?.let {
            model.getReadingList(requireContext()).observe(viewLifecycleOwner) {
                when (it.responseType) {
                    ResponseType.error -> showErrorPage(it.errorMessage)
                    ResponseType.loading -> showLoadingPage()
                    ResponseType.readingList -> {
                        surahs = it.readingList
                        if (surahs == null) showErrorPage(null) else {
                            adapter =
                                ReadingAdapter(requireContext(), surahs!!, object : SurahItemClick {
                                    override fun onItemClick(surah: Any, isInfo: Boolean) {
                                        if (!isInfo) onClickSurah(surah as ReadingSurah) else
                                            showSurahInfo(surah as ReadingSurah)
                                    }
                                },object : DeleteItem{
                                    override fun onClickDelete(readingSurah: ReadingSurah) {
                                        model.deleteFromDatabase(context!!,readingSurah).observe(viewLifecycleOwner){
                                            Toast.makeText(context,R.string.deleted,Toast.LENGTH_LONG).show()
                                        }
                                    }
                                })
                            setSurahAdapter(adapter!!)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun onClickSurah(readingSurah: ReadingSurah) {
        val intent = Intent(context, AyahActivity::class.java)
        intent.putExtra("number", readingSurah.number)
        intent.putExtra("name", readingSurah.name)
        intent.putExtra("englishName", readingSurah.englishName)
        intent.putExtra("englishNameTranslation", readingSurah.englishNameTranslation)
        intent.putExtra("numberOfAyahs", readingSurah.numberOfAyahs)
        intent.putExtra("revelationType", readingSurah.revelationType)
        intent.putExtra("num", readingSurah.ayahNumber)
        startActivity(intent)
    }

    fun onKeyDown(keyCode: Int): Boolean? {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            showErrorPage("ReadingList")
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

    private fun showSurahInfo(r: ReadingSurah) {
        showingInfo = true
        b.surahInfo.visibility = View.VISIBLE
        b.recycler.visibility = View.GONE
        b.progressBar.visibility = View.GONE
        b.errorPage.visibility = View.GONE
        b.surahInfo.text = getString(
            R.string.surahInfo,
            r.number.toString(),
            r.name,
            r.englishName,
            r.englishNameTranslation,
            r.numberOfAyahs.toString(),
            r.revelationType
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

    private fun setSurahAdapter(adapter: ReadingAdapter) {
        showQuranPage()
        b.recycler.layoutManager = LinearLayoutManager(context)
        b.recycler.adapter = adapter
    }
}