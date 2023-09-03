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

class UnitConverterFragment : Fragment(), UnitListAdapter.OnCalculateListener {

    private lateinit var binding: UnitConverterFragmentBinding
    private lateinit var mainActivity: MainActivity

    private lateinit var unitList : ArrayList<UnitListData>

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
        unitList = ArrayList<UnitListData>()
        unitList.add(UnitListData(getString(R.string.exchange_rate), "달러", "원"))
        unitList.add(UnitListData(getString(R.string.temperature), "화씨", "섭씨"))
        unitList.add(UnitListData(getString(R.string.mass), "lb", "kg"))
        unitList.add(UnitListData(getString(R.string.distance), "mile", "km"))
        unitList.add(UnitListData(getString(R.string.length), "inch", "cm"))

        binding.unitConverterListView.layoutManager = LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false)
        val unitListAdapter = UnitListAdapter(unitList, this)
        binding.unitConverterListView.adapter = unitListAdapter
    }

    override fun onCalculateUnit(type: String, calculateDirection: Boolean, input: String) : String? {
        if(input.isNullOrEmpty())
            return null

        return when(type){
            "환율"-> calculateDollar(calculateDirection, input)
            "온도"-> calculateTemperature(calculateDirection, input)
            "무게"-> calculatePound(calculateDirection, input)
            "거리"-> calculateMile(calculateDirection, input)
            "길이"-> calculateInch(calculateDirection, input)
            else -> null
        }
    }

    private fun calculateDollar(isToRight : Boolean, input : String) : String{
        val curCurrency : Double = mainActivity.currentCurrencyRate!!.toDouble()
        return if(isToRight){
            //달러 -> 원
            String.format("%.0f", input.toDouble() * curCurrency)
        }else{
            //원 -> 달러
            String.format("%.2f", input.toDouble() / curCurrency)
        }
    }

    private fun calculateTemperature(isToRight: Boolean, input: String) : String{
        return if(isToRight){
            //화씨 -> 섭씨
            String.format("%.0f", (input.toDouble() - 32.0) * (5.0 / 9.0))
        }else{
            //섭씨 -> 화씨
            String.format("%.0f", (input.toDouble() * (9.0 / 5.0)) + 32.0)
        }
    }

    private fun calculatePound(isToRight: Boolean, input: String) : String{
        return if(isToRight){
            //lb -> kg
            String.format("%.2f", input.toDouble().div(2.205))
        }else{
            //kg -> lb
            String.format("%.2f", input.toDouble().times(2.205))
        }
    }

    private fun calculateMile(isToRight: Boolean, input: String) : String{
        return if(isToRight){
            //mile -> km
            String.format("%.2f", input.toDouble().times(1.609))
        }else{
            //km -> mile
            String.format("%.2f", input.toDouble().div(1.609))
        }
    }

    private fun calculateInch(isToRight: Boolean, input: String) : String{
        return if(isToRight){
            //inch -> cm
            String.format("%.2f", input.toDouble().times(2.54))
        }else{
            //cm -> inch
            String.format("%.2f", input.toDouble().div(2.54))
        }
    }
}