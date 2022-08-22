package com.mertdev.weighttracking.ui.measurement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.data.entity.Measurement
import com.mertdev.weighttracking.databinding.FragmentMeasurementBinding
import com.mertdev.weighttracking.uimodel.UiModel
import com.mertdev.weighttracking.utils.SwipeGesture
import com.mertdev.weighttracking.utils.enums.DataStatus
import com.mertdev.weighttracking.utils.extensions.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MeasurementFragment : Fragment(R.layout.fragment_measurement) {

    private val binding: FragmentMeasurementBinding by viewBinding()
    private val viewModel: MeasurementViewModel by viewModels()
    private val measurementAdapter = MeasurementsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewLifecycleOwner.lifecycleScope.launch {
            collectUiState()
        }

        binding.addBtn.setOnClickListener {
            goToAddMeasurementDialogFragment()
        }

        measurementAdapter.setOnMenuClickListener { measurement ->
            goToAddMeasurementDialogFragment(measurement)
        }

        measurementAdapter.setOnItemClickListener { measurement ->
            findNavController().safeNavigate(
                MeasurementFragmentDirections.actionMeasurementFragmentToMeasurementContentFragment(
                    measurement.id
                )
            )
        }

    }

    private fun initView() = with(binding) {
        swipeRefresh.isEnabled = false

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.rv_divider_layer
            )!!
        )

        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = measurementAdapter
            addItemDecoration(itemDecoration)
        }

        val swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteMeasurement(
                    measurementAdapter.currentList[viewHolder.absoluteAdapterPosition].id
                )
            }
        }

        ItemTouchHelper(swipeGesture).attachToRecyclerView(binding.recyclerView)
    }

    private suspend fun collectUiState() {
        viewModel.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { dataStatus ->
                when (dataStatus) {
                    is DataStatus.Loading -> onLoading()
                    is DataStatus.Error -> onError()
                    is DataStatus.Success -> dataStatus.data?.let { onSuccess(it) }
                }
            }
    }

    private fun onLoading() {
        binding.swipeRefresh.isRefreshing = true
        binding.errorTxt.isVisible = false
    }

    private fun onError() {
        binding.swipeRefresh.isRefreshing = false
        binding.errorTxt.isVisible = true
    }

    private fun onSuccess(data: UiModel) = with(data) {
        binding.swipeRefresh.isRefreshing = false
        binding.errorTxt.isVisible = false
        measurementAdapter.submitList(allMeasurements)
        emptyLayoutState(this)
    }

    private fun emptyLayoutState(uiModel: UiModel) = with(binding.emptyLayout) {
        root.isVisible = uiModel.isShowMeasurementEmptyLayout == true
        addBtn.setOnClickListener {
            goToAddMeasurementDialogFragment()
        }
    }

    private fun goToAddMeasurementDialogFragment(measurement: Measurement? = null) {
        findNavController().safeNavigate(
            MeasurementFragmentDirections.actionMeasurementFragmentToAddMeasurementDialogFragment(
                measurement
            )
        )
    }

}