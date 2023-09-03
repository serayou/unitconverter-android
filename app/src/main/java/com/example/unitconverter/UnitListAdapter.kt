package com.example.unitconverter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.databinding.ItemUnitListBinding

class UnitListAdapter(private val items : ArrayList<UnitListData>, private var listener : OnCalculateListener) : RecyclerView.Adapter<UnitListAdapter.UnitListContentHolder>(){

    interface OnCalculateListener{
        fun onCalculateUnit(type : String, calculateDirection : Boolean, input: String) : String?
    }

    private var calculateListener : OnCalculateListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitListContentHolder {
        calculateListener = listener
        val binding = ItemUnitListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UnitListContentHolder(binding)
    }

    override fun onBindViewHolder(holder: UnitListContentHolder, position: Int) {
        holder.unitConverterListName.text = items[position].listName
        holder.leftUnitName.text = items[position].leftUnitText
        holder.rightUnitName.text = items[position].rightUnitText

        holder.unitConverterLeftInput.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus){
                holder.unitConverterRightInput.text = null
            }
        }

        holder.unitConverterLeftInput.addTextChangedListener(object : TextWatcher {
            var result : String? = null

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                if(count == 0){
                    holder.unitConverterRightInput.text = null
                }
                if(calculateListener != null){
                    result = calculateListener?.onCalculateUnit(holder.unitConverterListName.text.toString(), true, holder.unitConverterLeftInput.text.toString())
                }
                holder.unitConverterRightInput.setText(result)
            }

            override fun afterTextChanged(p0: Editable?) {
            }
       })
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class UnitListContentHolder(private val binding: ItemUnitListBinding) : RecyclerView.ViewHolder(binding.root) {
        val unitConverterListName = binding.tvConverterListName
        val unitConverterLeftInput = binding.etConverterLeft
        val leftUnitName = binding.tvConverterLeftUnitText
        val unitConverterRightInput = binding.etConverterRight
        val rightUnitName = binding.tvConverterRightUnitText
        val dragAvailableBtn = binding.btnDragAvailable
    }
}