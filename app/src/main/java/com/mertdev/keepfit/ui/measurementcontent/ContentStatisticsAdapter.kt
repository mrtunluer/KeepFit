package com.mertdev.keepfit.ui.measurementcontent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mertdev.keepfit.data.entity.MeasurementContent
import com.mertdev.keepfit.databinding.StatisticsItemBinding
import com.mertdev.keepfit.utils.extensions.showDateWithDay

class ContentStatisticsAdapter :
    ListAdapter<MeasurementContent, ContentStatisticsAdapter.ViewHolder>(DifferCallback) {

    object DifferCallback : DiffUtil.ItemCallback<MeasurementContent>() {
        override fun areItemsTheSame(
            oldItem: MeasurementContent, newItem: MeasurementContent
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MeasurementContent, newItem: MeasurementContent
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StatisticsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
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

        fun bind(content: MeasurementContent) {
            binding.dateTxt.text = content.date?.showDateWithDay()
            binding.weightTxt.text = content.value.toString()
            binding.noteTxt.text = content.note
            noteVisibility(content.note)
        }

        private fun noteVisibility(note: String?) {
            binding.noteTxt.isVisible = !note.isNullOrEmpty()
        }

    }

    private var onItemClickListener: ((MeasurementContent) -> Unit)? = null

    fun setOnItemClickListener(listener: (MeasurementContent) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}