package com.mertdev.weighttracking.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mertdev.weighttracking.data.entity.Weight
import com.mertdev.weighttracking.databinding.StatisticsItemBinding
import com.mertdev.weighttracking.utils.extensions.showDateWithDay

class WeightStatisticsAdapter : ListAdapter<Weight, WeightStatisticsAdapter.ViewHolder>(DifferCallback) {

     object DifferCallback : DiffUtil.ItemCallback<Weight>() {
        override fun areItemsTheSame(oldItem: Weight, newItem: Weight): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Weight, newItem: Weight): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            StatisticsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(getItem(position))
            }
        }
    }

    inner class ViewHolder(private val binding: StatisticsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(weight: Weight) {
            binding.dateTxt.text = weight.date?.showDateWithDay()
            binding.weightTxt.text = weight.value.toString()
            binding.noteTxt.text = weight.note
            noteVisibility(weight.note)
        }

        private fun noteVisibility(note: String?) {
            binding.noteTxt.isVisible = !note.isNullOrEmpty()
        }

    }

    private var onItemClickListener: ((Weight) -> Unit)? = null

    fun setOnItemClickListener(listener: (Weight) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}