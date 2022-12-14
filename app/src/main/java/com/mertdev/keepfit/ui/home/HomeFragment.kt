package com.mertdev.keepfit.ui.home

import android.os.Build
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.BarEntry
import com.mertdev.keepfit.R
import com.mertdev.keepfit.databinding.FragmentHomeBinding
import com.mertdev.keepfit.utils.chart.InitChart
import com.mertdev.keepfit.uimodel.WeightUiModel
import com.mertdev.keepfit.utils.Constants.NOTIFICATION_PERMISSION
import com.mertdev.keepfit.utils.SwipeGesture
import com.mertdev.keepfit.utils.enums.DataStatus
import com.mertdev.keepfit.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel: HomeViewModel by viewModels()
    private var weightUiModel = WeightUiModel()
    private val weightStatisticsAdapter = WeightStatisticsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        permissionsRequest()

        viewLifecycleOwner.lifecycleScope.launch {
            collectUiState()
        }

        binding.addBtn.setOnClickListener {
            goToAddWeightDialogFragment(weightUiModel)
        }

        weightStatisticsAdapter.setOnItemClickListener { weight ->
            val weightUiModel = weightUiModel.copy(weight = weight)
            goToAddWeightDialogFragment(weightUiModel)
        }

        binding.allWeightBtn.setOnClickListener {
            goToAllWeightDialogFragment()
        }

        binding.bmiCard.setOnClickListener {
            goToBmiDialogFragment(weightUiModel)
        }

    }

    private fun initView() = with(binding) {
        swipeRefresh.isEnabled = false
        horizontalProgress.max = 100

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(
            AppCompatResources.getDrawable(
                requireContext(), R.drawable.rv_divider_layer
            )!!
        )

        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = weightStatisticsAdapter
            addItemDecoration(itemDecoration)
        }

        val swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteWeight(
                    weightStatisticsAdapter.currentList[viewHolder.absoluteAdapterPosition].id
                )
            }
        }

        ItemTouchHelper(swipeGesture).attachToRecyclerView(binding.recyclerView)
    }

    private fun emptyLayoutState(weightUiModel: WeightUiModel) = with(binding.emptyLayout) {
        root.isVisible = weightUiModel.isShowEmptyLayout == true
        addBtn.setOnClickListener {
            goToAddWeightDialogFragment(weightUiModel)
        }
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

    private fun onLoadingForUiState() {
        binding.swipeRefresh.isRefreshing = true
        binding.errorTxt.isVisible = false
    }

    private fun onErrorForUiState() {
        binding.swipeRefresh.isRefreshing = false
        binding.errorTxt.isVisible = true
    }

    private fun onSuccessForUiState(data: WeightUiModel) = with(data) {
        binding.swipeRefresh.isRefreshing = false
        binding.errorTxt.isVisible = false
        binding.targetWeightTxt.text = targetWeight.toString()
        binding.currentWeightTxt.text = currentWeight.toString()
        binding.firstWeightTxt.text = firstWeight.toString()
        binding.weightUnitTxt.text = weightUnit
        binding.maxWeightTxt.text = maxWeight.toString()
        binding.minWeightTxt.text = minWeight.toString()
        binding.avgWeightTxt.text = avgWeight?.round(1).toString()
        weightStatisticsAdapter.submitList(lastSevenWeight)
        emptyLayoutState(this)
        setMathematicalOperations(this)
        setChart(this)
        weightUiModel = this
    }

    private fun setMathematicalOperations(data: WeightUiModel) = with(MathematicalOperations) {
        setRemainderWeight(data, binding, requireContext())
        setHorizontalProgressLoading(data, binding)
        calculateBmi(data, binding)
        calculateIdealWeight(data, binding)
        calculateHealthyWeightRange(data, binding)
    }

    private fun setChart(data: WeightUiModel) = with(data) {
        numberOfChartData?.let {
            val listByNumberOfChartData = allWeights.takeLast(it)

            val entryList = listByNumberOfChartData.mapIndexed { index, weight ->
                BarEntry(index.toFloat(), weight.value ?: 0f)
            }

            InitChart.setChart(
                entryList, requireContext(), binding.chart, weightList = listByNumberOfChartData
            )
        }
    }

    private fun permissionsRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (!isGranted) requireContext().showToast(getString(R.string.not_allowed_notification))
            }.launch(NOTIFICATION_PERMISSION)
        }
    }

    private fun goToAddWeightDialogFragment(weightUiModel: WeightUiModel) {
        findNavController().safeNavigate(
            HomeFragmentDirections.actionHomeFragmentToAddWeightDialogFragment(
                weightUiModel
            )
        )
    }

    private fun goToAllWeightDialogFragment() {
        findNavController().safeNavigate(
            HomeFragmentDirections.actionHomeFragmentToAllWeightDialogFragment()
        )
    }

    private fun goToBmiDialogFragment(weightUiModel: WeightUiModel) {
        findNavController().safeNavigate(
            HomeFragmentDirections.actionHomeFragmentToBmiDialogFragment(weightUiModel)
        )
    }

}