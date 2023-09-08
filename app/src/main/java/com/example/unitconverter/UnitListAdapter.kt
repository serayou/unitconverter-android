package com.example.unitconverter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.databinding.ItemUnitListBinding
import java.util.*
import kotlin.collections.ArrayList

class UnitListAdapter(
    private val items : ArrayList<UnitListData>,
    private var calculateListener : OnCalculateListener,
    private var dragListener : OnDragListener
) : RecyclerView.Adapter<UnitListAdapter.UnitListContentHolder>(), UnitListItemTouchHelperCallback.OnItemMoveListener{

    interface OnCalculateListener{
        fun onCalculateUnit(type : String, calculateDirection : Boolean, input: String) : String?
    }

    interface OnDragListener{
        fun onStartDrag(viewHolder: UnitListContentHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitListContentHolder {
        val binding = ItemUnitListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UnitListContentHolder(binding)
    }

    override fun onBindViewHolder(holder: UnitListContentHolder, position: Int) {
        var hasLeftInputFocus = false
        var hasRightInputFocus = false

        holder.unitConverterListName.text = items[position].listName
        holder.leftUnitName.text = items[position].leftUnitText
        holder.rightUnitName.text = items[position].rightUnitText

        holder.unitConverterLeftInput.setOnFocusChangeListener { view, hasFocus ->
            hasLeftInputFocus = hasFocus
        }

        holder.unitConverterRightInput.setOnFocusChangeListener { view, hasFocus ->
            hasRightInputFocus = hasFocus
        }

        holder.unitConverterLeftInput.addTextChangedListener(object : TextWatcher {
            var result : String? = null

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                if(hasRightInputFocus) return
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

        holder.unitConverterRightInput.addTextChangedListener(object : TextWatcher {
            var result : String? = null

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                if(hasLeftInputFocus) return
                if(count == 0){
                    holder.unitConverterLeftInput.text = null
                }
                if(calculateListener != null){
                    result = calculateListener?.onCalculateUnit(holder.unitConverterListName.text.toString(), false, holder.unitConverterRightInput.text.toString())
                }
                holder.unitConverterLeftInput.setText(result)
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        holder.dragAvailableBtn.setOnClickListener {
            if(dragListener != null){
                dragListener?.onStartDrag(holder)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onFinishItemMove() {
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