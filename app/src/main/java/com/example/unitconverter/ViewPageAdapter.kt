package com.example.unitconverter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPageAdapter (private val mContext : Context,
                       mFragmentManager: FragmentManager,
                       var totalTabs : Int) : FragmentPagerAdapter(mFragmentManager) {

    override fun getItem(position: Int): Fragment {

        return when(position){
            0 ->{
                UnitConverterFragment()
            }
            1 ->{
                TipCalculatorFragment()
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}