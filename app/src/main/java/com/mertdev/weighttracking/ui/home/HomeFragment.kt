package com.mertdev.weighttracking.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.databinding.FragmentHomeBinding
import com.mertdev.weighttracking.ui.home.model.UiModel
import com.mertdev.weighttracking.utils.enums.DataStatus
import com.mertdev.weighttracking.utils.extensions.round
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var uiModel: UiModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewLifecycleOwner.lifecycleScope.launch {
            collectUiModel()
        }

        binding.addBtn.setOnClickListener {
            try {
                // When opening the bottom sheet fragment in android, when you send a continuous fragment creation request,
                // you may encounter the 'cannot be found from the current destination' error.
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToAddWeightFragment(uiModel)
                )
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
            }
        }

    }

    private fun initView() {
        binding.swipeRefresh.isEnabled = false
    }

    private suspend fun collectUiModel() {
        viewModel.uiModel.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
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
        binding.targetWeightTxt.text = targetWeight.toString()
        binding.currentWeightTxt.text = currentWeight.toString()
        binding.firstWeightTxt.text = firstWeight.toString()
        binding.weightUnitTxt.text = weightUnit
        setRemainderWeight(this)
        setProgressLoading(this)
        uiModel = this
    }

    private fun setRemainderWeight(data: UiModel) {
        binding.remainingTxt.text =
            getString(R.string.remaining).plus(" " + data.currentWeight?.let {
                data.targetWeight?.minus(it)?.round(2)?.absoluteValue
            })
    }

    private fun setProgressLoading(data: UiModel) {
        val progressMax = data.firstWeight?.let { data.targetWeight?.minus(it)?.absoluteValue }
        val progress = data.firstWeight?.let { data.currentWeight?.minus(it)?.absoluteValue }
        if (progressMax != null && progress != null) {
            binding.horizontalProgress.max = progressMax.roundToInt()
            binding.horizontalProgress.progress = progress.roundToInt()
        }
    }

}