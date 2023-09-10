package com.example.unitconverter

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.unitconverter.databinding.TipCalculatorFragmentBinding

class TipCalculatorFragment : Fragment() {

    private lateinit var binding: TipCalculatorFragmentBinding
    private lateinit var mainActivity: MainActivity

    private var totalDollar : Double? = 0.0
    private var customInputType : String? = "dollarToPercent"

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
                if(binding.etTotal.text.toString().isNotEmpty()){
                    totalDollar = binding.etTotal.text.toString().toDouble()
                    calculateTipPercent()
                }
                true
            }
            false
        }

        binding.etTotal.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                if(binding.etTotal.text.toString().isNotEmpty()){
                    binding.btnRemoveAllText.visibility = View.VISIBLE
                }else{
                    binding.btnRemoveAllText.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.btnRemoveAllText.setOnClickListener{
            if(binding.etTotal.text.toString().isNotEmpty()){
                clearCalculator()
            }
        }

        binding.layoutBtnTip15.setOnClickListener {
            addTip(binding.tvTip15Dollar.text.toString())
        }

        binding.layoutBtnTip18.setOnClickListener {
            addTip(binding.tvTip18Dollar.text.toString())
        }

        binding.layoutBtnTip20.setOnClickListener {
            addTip(binding.tvTip20Dollar.text.toString())
        }

        binding.btnTipCustom.setOnClickListener {
            if(binding.etTotal.text.toString().isNullOrEmpty()){
                Toast.makeText(mainActivity, getString(R.string.msg_input_total), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            initCustomTip()
            setSelectTipLayoutVisibility(false)
        }

        binding.btnBackToSelectTip.setOnClickListener{
            setSelectTipLayoutVisibility(true)
            binding.tvTotalWithTip.text = null
        }

        binding.etTopCustomTip.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                if(count == 0){
                    binding.tvBottomCustomTip.text = null
                    binding.tvTotalWithTip.text = null
                }
                if(binding.etTopCustomTip.text.isNotEmpty()){
                    calculateCustomTip(customInputType, binding.etTopCustomTip.text.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.btnSwitch.setOnClickListener {
            binding.etTopCustomTip.text = null
            binding.tvBottomCustomTip.text = null
            if(binding.imageCustomTipTopDollar.visibility == View.VISIBLE){
                customInputType = "PercentToDollar"
                binding.imageCustomTipTopDollar.visibility = View.GONE
                binding.imageCustomTipTopPercent.visibility = View.VISIBLE
                binding.imageCustomTipBottomDollar.visibility = View.VISIBLE
                binding.imageCustomTipBottomPercent.visibility = View.GONE
            }else if(binding.imageCustomTipTopDollar.visibility == View.GONE){
                customInputType = "dollarToPercent"
                binding.imageCustomTipTopDollar.visibility = View.VISIBLE
                binding.imageCustomTipTopPercent.visibility = View.GONE
                binding.imageCustomTipBottomDollar.visibility = View.GONE
                binding.imageCustomTipBottomPercent.visibility = View.VISIBLE
            }
        }
    }

    private fun initCustomTip(){
        //Custom Tip
        customInputType = "dollarToPercent"
        binding.imageCustomTipTopDollar.visibility = View.VISIBLE
        binding.imageCustomTipTopPercent.visibility = View.GONE
        binding.imageCustomTipBottomDollar.visibility = View.GONE
        binding.imageCustomTipBottomPercent.visibility = View.VISIBLE

        binding.etTopCustomTip.text = null
        binding.tvBottomCustomTip.text = null
        binding.tvTotalWithTip.text = null
    }

    private fun calculateTipPercent(){
        binding.tvTotalWithTip.text = null

        binding.tvTip15Dollar.text = String.format("%.2f", totalDollar?.times(0.15))
        binding.tvTip18Dollar.text = String.format("%.2f", totalDollar?.times(0.18))
        binding.tvTip20Dollar.text = String.format("%.2f", totalDollar?.times(0.2))
    }

    private fun calculateCustomTip(calculateType : String?, input : String){
        if(calculateType.equals("dollarToPercent")){
            binding.tvBottomCustomTip.text = String.format("%.2f", (totalDollar?.let {
                input.toDouble().div(
                    it
                )
            })?.times(100.0))

            addTip(binding.etTopCustomTip.text.toString())
        }else if(calculateType.equals("PercentToDollar")){
            binding.tvBottomCustomTip.text = String.format("%.2f", totalDollar?.times(input.toDouble().div(100.0)))

            addTip(binding.tvBottomCustomTip.text.toString())
        }
    }

    private fun addTip(tip : String){
        tip.takeIf { it.isNotEmpty() }
            ?.toDoubleOrNull()
            ?.let { tipAmount ->
                binding.tvTotalWithTip.text = String.format("%.2f", totalDollar?.plus(tipAmount))
            } ?: Toast.makeText(mainActivity, getString(R.string.msg_input_total), Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard(){
        val imm = mainActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mainActivity.window.decorView.applicationWindowToken, 0)
    }

    private fun setSelectTipLayoutVisibility(visibility : Boolean){
        if(visibility){
            binding.layoutSelectTip.visibility = View.VISIBLE
            binding.layoutSelectCustomTip.visibility = View.GONE
        }else{
            binding.layoutSelectTip.visibility = View.GONE
            binding.layoutSelectCustomTip.visibility = View.VISIBLE
        }
    }

    fun resetCalculator(){
        setSelectTipLayoutVisibility(true)

        binding.etTotal.text = null
        binding.tvTotalWithTip.text = null

        binding.tvTip15Dollar.text = null
        binding.tvTip18Dollar.text = null
        binding.tvTip20Dollar.text = null

        totalDollar = 0.0
        initCustomTip()
    }

    private fun clearCalculator(){
        binding.etTotal.text = null
        binding.tvTotalWithTip.text = null

        binding.tvTip15Dollar.text = null
        binding.tvTip18Dollar.text = null
        binding.tvTip20Dollar.text = null

        binding.etTopCustomTip.text = null
        binding.tvBottomCustomTip.text = null

        totalDollar = 0.0
    }
}