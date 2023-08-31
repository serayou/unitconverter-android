package com.example.unitconverter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.databinding.UnitConverterFragmentBinding

class UnitConverterFragment : Fragment() {

    private lateinit var binding: UnitConverterFragmentBinding
    private lateinit var mainActivity: MainActivity

    private lateinit var unitList : ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UnitConverterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onStart() {
        super.onStart()
        initResource()
    }

    private fun initResource(){
        unitList = ArrayList<String>()
        unitList.add("환율")
        unitList.add("온도")
        unitList.add("무게")
        unitList.add("거리")
        unitList.add("길이")

        binding.unitConverterListView.layoutManager = LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false)
        val unitListAdapter = UnitListAdapter(unitList)
        binding.unitConverterListView.adapter = unitListAdapter


    }
}