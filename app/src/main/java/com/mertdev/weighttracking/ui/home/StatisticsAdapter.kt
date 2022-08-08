package com.mertdev.weighttracking.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mertdev.weighttracking.data.entity.Weight
import com.mertdev.weighttracking.databinding.WeightItemBinding
import com.mertdev.weighttracking.utils.Constants.LIMIT_FOR_STATISTICS
import com.mertdev.weighttracking.utils.extensions.showDate

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

    fun getItem(position: Int): Weight {
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
        holder.bind(getItem(position))

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(getItem(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return if (differ.currentList.size > 7)
            LIMIT_FOR_STATISTICS
        else
            differ.currentList.size
    }

    inner class ViewHolder(private val binding: WeightItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(weight: Weight) {
            binding.dateTxt.text = weight.date?.showDate(isShowDay = true)
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

}