package com.mertdev.weighttracking.ui.allweightdialog

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
import com.mertdev.weighttracking.databinding.FragmentAllWeightDialogBinding
import com.mertdev.weighttracking.ui.home.WeightStatisticsAdapter
import com.mertdev.weighttracking.uimodel.WeightUiModel
import com.mertdev.weighttracking.utils.SwipeGesture
import com.mertdev.weighttracking.utils.enums.DataStatus
import com.mertdev.weighttracking.utils.extensions.safeNavigate
import com.mertdev.weighttracking.utils.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllWeightDialogFragment : BottomSheetDialogFragment() {

    private val binding: FragmentAllWeightDialogBinding by viewBinding()
    private val weightStatisticsAdapter = WeightStatisticsAdapter()
    private val viewModel: AllWeightViewModel by viewModels()
    private var weightUiModel = WeightUiModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_all_weight_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewLifecycleOwner.lifecycleScope.launch {
            collectUiState()
        }

        weightStatisticsAdapter.setOnItemClickListener { weight ->
            weightUiModel = weightUiModel.copy(weight = weight)
            goToAddWeightFragment(weightUiModel)
        }

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

    private fun onSuccessForUiState(data: WeightUiModel) = with(data) {
        binding.progressBar.isVisible = false
        weightStatisticsAdapter.submitList(allWeights.asReversed())
        weightUiModel = this
    }

    private fun goToAddWeightFragment(weightUiModel: WeightUiModel) {
        findNavController().safeNavigate(
            AllWeightDialogFragmentDirections.actionAllWeightFragmentToAddWeightDialogFragment(
                weightUiModel
            )
        )
    }
}