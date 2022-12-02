package com.mertdev.keepfit.ui.measurement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mertdev.keepfit.data.entity.Measurement
import com.mertdev.keepfit.databinding.MeasurementItemBinding
import com.mertdev.keepfit.utils.extensions.showDateWithDay

class MeasurementAdapter :
    ListAdapter<Measurement, MeasurementAdapter.ViewHolder>(DifferCallback) {

    object DifferCallback : DiffUtil.ItemCallback<Measurement>() {
        override fun areItemsTheSame(oldItem: Measurement, newItem: Measurement): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Measurement, newItem: Measurement): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MeasurementItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: MeasurementItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(measurement: Measurement) = with(binding) {
            binding.dateTxt.text = measurement.date?.showDateWithDay()
            binding.titleTxt.text = measurement.name.toString()

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(measurement)
                }
            }

            menuImg.setOnClickListener {
                onMenuClickListener?.let {
                    it(measurement)
                }
            }
        }

    }

    private var onItemClickListener: ((Measurement) -> Unit)? = null

    fun setOnItemClickListener(listener: (Measurement) -> Unit) {
        onItemClickListener = listener
    }

    private var onMenuClickListener: ((Measurement) -> Unit)? = null

    fun setOnMenuClickListener(listener: (Measurement) -> Unit) {
        onMenuClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}