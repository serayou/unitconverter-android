package com.example.unitconverter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.unitconverter.databinding.TipCalculatorFragmentBinding

class TipCalculatorFragment : Fragment() {

    private lateinit var binding: TipCalculatorFragmentBinding
    private lateinit var mainActivity: MainActivity

    private var totalDollar : Double? = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TipCalculatorFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onStart() {
        super.onStart()
        setResource()
    }

    private fun setResource() {
        binding.btnCheck.setOnClickListener{
            totalDollar = binding.etTotal.text.toString().toDouble()
            calculateTipPercent()
        }
    }

    private fun calculateTipPercent(){
        binding.tvTip15Dollar.text = String.format("%.2f", totalDollar?.times(0.15))
        binding.tvTip18Dollar.text = String.format("%.2f", totalDollar?.times(0.18))
        binding.tvTip20Dollar.text = String.format("%.2f", totalDollar?.times(0.2))
    }
}