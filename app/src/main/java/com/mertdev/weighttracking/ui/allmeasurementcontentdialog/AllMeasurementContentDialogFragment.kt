package com.mertdev.weighttracking.ui.allmeasurementcontentdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.databinding.AllValueDialogBinding
import com.mertdev.weighttracking.ui.measurementcontent.ContentStatisticsAdapter
import com.mertdev.weighttracking.uimodel.MeasurementUiModel
import com.mertdev.weighttracking.utils.SwipeGesture
import com.mertdev.weighttracking.utils.enums.DataStatus
import com.mertdev.weighttracking.utils.extensions.safeNavigate
import com.mertdev.weighttracking.utils.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllMeasurementContentDialogFragment : BottomSheetDialogFragment() {

    private val binding: AllValueDialogBinding by viewBinding()
    private val contentStatisticsAdapter = ContentStatisticsAdapter()
    private val viewModel: AllMeasurementContentViewModel by viewModels()
    private var measurementUiModel = MeasurementUiModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.all_value_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewLifecycleOwner.lifecycleScope.launch {
            collectUiState()
        }

        contentStatisticsAdapter.setOnItemClickListener { measurementContent ->
            val measurementUiModel = measurementUiModel.copy(measurementContent = measurementContent)
            goToAddMeasurementContentDialogFragment(measurementUiModel)
        }

    }

    private suspend fun collectUiState() {
        viewModel.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { dataStatus ->
                when (dataStatus) {
                    is DataStatus.Loading -> binding.progressBar.isVisible = true
                    is DataStatus.Success -> dataStatus.data?.let { onSuccessForUiState(it) }
                    is DataStatus.Error -> onErrorForUiState()
                }
            }
    }

    private fun onErrorForUiState() {
        binding.progressBar.isVisible = false
        requireContext().showToast(getString(R.string.error))
    }

    private fun onSuccessForUiState(data: MeasurementUiModel) = with(data) {
        binding.progressBar.isVisible = false
        contentStatisticsAdapter.submitList(allMeasurementContent.asReversed())
        measurementUiModel = this
    }

    private fun goToAddMeasurementContentDialogFragment(measurementUiModel: MeasurementUiModel) {
        findNavController().safeNavigate(
            AllMeasurementContentDialogFragmentDirections.actionAllMeasurementContentDialogFragmentToAddMeasurementContentDialogFragment(
                measurementUiModel
            )
        )
    }

    private fun initView() = with(binding) {
        progressBar.isVisible = false

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

}