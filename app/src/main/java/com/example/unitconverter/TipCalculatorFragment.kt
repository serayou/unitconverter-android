package com.example.unitconverter

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
        binding.etTotal.setOnEditorActionListener { textView, action, keyEvent ->
            if(action == EditorInfo.IME_ACTION_DONE){
                totalDollar = binding.etTotal.text.toString().toDouble()
                calculateTipPercent()
                true
            }
            false
        }

        binding.btnCheck.setOnClickListener{
            totalDollar = binding.etTotal.text.toString().toDouble()
            calculateTipPercent()
            hideKeyboard()

        }
    }

    private fun calculateTipPercent(){
        binding.tvTip15Dollar.text = String.format("%.2f", totalDollar?.times(0.15))
        binding.tvTip18Dollar.text = String.format("%.2f", totalDollar?.times(0.18))
        binding.tvTip20Dollar.text = String.format("%.2f", totalDollar?.times(0.2))
    }

    private fun hideKeyboard(){
        val imm = mainActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mainActivity.window.decorView.applicationWindowToken, 0)
    }
}