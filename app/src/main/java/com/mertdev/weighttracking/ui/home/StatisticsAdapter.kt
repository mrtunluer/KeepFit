package com.mertdev.weighttracking.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mertdev.weighttracking.data.entity.Weight
import com.mertdev.weighttracking.databinding.WeightItemBinding

class StatisticsAdapter : RecyclerView.Adapter<StatisticsAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Weight>() {
        override fun areItemsTheSame(oldItem: Weight, newItem: Weight): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Weight, newItem: Weight): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<Weight>) {
        differ.submitList(list)
    }

    private fun getItem(position: Int): Weight {
        return differ.currentList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            WeightItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weight = getItem(position)
        holder.binding.apply {
            weightTxt.text = weight.value.toString()
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(weight)
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(val binding: WeightItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((Weight) -> Unit)? = null

    fun setOnItemClickListener(listener: (Weight) -> Unit) {
        onItemClickListener = listener
    }

}