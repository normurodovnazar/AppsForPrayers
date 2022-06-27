package com.normurodov_nazar.quran.activities

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.normurodov_nazar.quran.R
import com.normurodov_nazar.quran.adapters.FragmentAdapter
import com.normurodov_nazar.quran.databinding.ActivityMainBinding
import com.normurodov_nazar.quran.fragments.ReadingList
import com.normurodov_nazar.quran.fragments.SurahList

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    private lateinit var fragmentAdapter: FragmentAdapter
    private var fragments = listOf(SurahList(),ReadingList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        fragmentAdapter = FragmentAdapter(supportFragmentManager, lifecycle,fragments)
        b.viewPager.adapter = fragmentAdapter
        TabLayoutMediator(b.tabLayout, b.viewPager) { tab, position ->
            run {
                if (position == 0) tab.text = getString(R.string.surahList) else tab.text =
                    getString(R.string.readingList)
            }
        }.attach()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if(b.tabLayout.selectedTabPosition==0){
            (fragments[0] as SurahList).onKeyDown(keyCode) ?: super.onKeyDown(
                keyCode,
                event
            )
        }else{
            (fragments[1] as ReadingList).onKeyDown(keyCode) ?: super.onKeyDown(
                keyCode,
                event
            )
        }
    }

    override fun onBackPressed() {
        if (b.tabLayout.selectedTabPosition==0) (fragments[0] as SurahList).onBackPressed() ?: return super.onBackPressed() else
            (fragments[1] as ReadingList).onBackPressed() ?: return super.onBackPressed()
    }
}