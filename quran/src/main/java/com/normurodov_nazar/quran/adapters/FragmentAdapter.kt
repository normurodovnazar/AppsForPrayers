package com.normurodov_nazar.quran.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.normurodov_nazar.quran.fragments.ReadingList
import com.normurodov_nazar.quran.fragments.SurahList

class FragmentAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle,val fragments: List<Fragment>) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}