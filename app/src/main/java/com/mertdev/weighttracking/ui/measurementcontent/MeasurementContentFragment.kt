package com.mertdev.weighttracking.ui.measurementcontent

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.BarEntry
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.data.entity.Measurement
import com.mertdev.weighttracking.databinding.FragmentMeasurementContentBinding
import com.mertdev.weighttracking.uimodel.MeasurementUiModel
import com.mertdev.weighttracking.utils.SwipeGesture
import com.mertdev.weighttracking.utils.chart.InitChart
import com.mertdev.weighttracking.utils.enums.DataStatus
import com.mertdev.weighttracking.utils.extensions.round
import com.mertdev.weighttracking.utils.extensions.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MeasurementContentFragment : Fragment(R.layout.fragment_measurement_content) {

    private val binding: FragmentMeasurementContentBinding by viewBinding()
    private val viewModel: MeasurementContentViewModel by viewModels()
    private val args: MeasurementContentFragmentArgs by navArgs()
    private var measurementUiModel = MeasurementUiModel()
    private val contentStatisticsAdapter = ContentStatisticsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val measurement = args.measurement
        initView(measurement)

        viewLifecycleOwner.lifecycleScope.launch {
            collectUiState()
        }

        binding.backImg.setOnClickListener {
            findNavController().popBackStack()
        }

        contentStatisticsAdapter.setOnItemClickListener { measurementContent ->
            val measurementUiModel = measurementUiModel.copy(measurementContent = measurementContent)
            goToAddMeasurementContentDialogFragment(measurementUiModel)
        }

        binding.addBtn.setOnClickListener {
            goToAddMeasurementContentDialogFragment(measurementUiModel)
        }

    }

    private fun initView(measurement: Measurement) = with(binding) {
        swipeRefresh.isEnabled = false
        titleTxt.text = measurement.name

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
            adapter = contentStatisticsAdapter
            addItemDecoration(itemDecoration)
        }

        val swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteMeasurementContent(
                    contentStatisticsAdapter.currentList[viewHolder.absoluteAdapterPosition].id
                )
            }
        }

        ItemTouchHelper(swipeGesture).attachToRecyclerView(binding.recyclerView)
    }

    private suspend fun collectUiState() {
        viewModel.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { dataStatus ->
                when (dataStatus) {
                    is DataStatus.Loading -> onLoadingForUiState()
                    is DataStatus.Error -> onErrorForUiState()
                    is DataStatus.Success -> dataStatus.data?.let { onSuccessForUiState(it) }
                }
            }
    }

    private fun onLoadingForUiState() = with(binding) {
        errorTxt.isVisible = false
        swipeRefresh.isRefreshing = true
    }

    private fun onErrorForUiState() = with(binding) {
        errorTxt.isVisible = true
        swipeRefresh.isRefreshing = false
    }

    private fun onSuccessForUiState(data: MeasurementUiModel) = with(data) {
        binding.errorTxt.isVisible = false
        binding.swipeRefresh.isRefreshing = false
        binding.currentValueTxt.text = currentMeasurementContentValue.toString()
        binding.lengthUnitTxt.text = lengthUnit.toString()
        binding.maxValueTxt.text = maxMeasurementContentValue.toString()
        binding.minValueTxt.text = minMeasurementContentValue.toString()
        binding.avgValueTxt.text = avgMeasurementContentValue?.round(1).toString()
        contentStatisticsAdapter.submitList(lastSevenMeasurementContent)
        emptyLayoutState(this)
        setChart(this)
        measurementUiModel = this
    }

    private fun setChart(data: MeasurementUiModel) = with(data) {
        numberOfChartData?.let {
            val listByNumberOfChartData = allMeasurementContent.takeLast(it)

            val entryList = listByNumberOfChartData
                .mapIndexed { index, measurementContent ->
                    BarEntry(index.toFloat(), measurementContent.value ?: 0f)
                }

            InitChart.setChart(
                entryList,
                requireContext(),
                binding.chart,
                measurementContentList = listByNumberOfChartData
            )
        }
    }


    private fun emptyLayoutState(measurementUiModel: MeasurementUiModel) =
        with(binding.emptyLayout) {
            root.isVisible = measurementUiModel.isShowEmptyLayout == true
            addBtn.setOnClickListener {
                goToAddMeasurementContentDialogFragment(measurementUiModel)
            }
        }

    private fun goToAddMeasurementContentDialogFragment(measurementUiModel: MeasurementUiModel) {
        findNavController().safeNavigate(
            MeasurementContentFragmentDirections.actionMeasurementContentFragmentToAddMeasurementContentDialogFragment(
                measurementUiModel
            )
        )
    }


}