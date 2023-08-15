package com.example.unitconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    var tabLayout : TabLayout? = null
    var viewPager : ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initResource()
        setTab()
    }

    private fun initResource(){
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
    }

    private fun setTab(){
        tabLayout!!.addTab(tabLayout!!.newTab().setText(R.string.unit_converter))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(R.string.tip_calculator))

        val viewPageAdapter = ViewPageAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = viewPageAdapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager!!.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
}