package com.example.unitconverter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.databinding.ItemUnitListBinding

class UnitListAdapter(private val items : ArrayList<String>) : RecyclerView.Adapter<UnitListAdapter.UnitListContentHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitListContentHolder {
        val binding = ItemUnitListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UnitListContentHolder(binding)
    }

    override fun onBindViewHolder(holder: UnitListContentHolder, position: Int) {
        holder.unitName.text = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class UnitListContentHolder(private val binding: ItemUnitListBinding) : RecyclerView.ViewHolder(binding.root) {
        val unitName = binding.tvUnitName
    }
}