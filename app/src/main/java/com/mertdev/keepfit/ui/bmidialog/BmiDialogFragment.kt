package com.mertdev.keepfit.ui.bmidialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mertdev.keepfit.R
import com.mertdev.keepfit.databinding.FragmentBmiDialogBinding
import com.mertdev.keepfit.uimodel.WeightUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BmiDialogFragment : BottomSheetDialogFragment() {

    private val binding: FragmentBmiDialogBinding by viewBinding()
    private val args: BmiDialogFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_bmi_dialog, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weightUiModel = args.weightUiModel
        initView(weightUiModel)
    }

    private fun initView(weightUiModel: WeightUiModel) = with(binding) {
        bmiTxt.text = weightUiModel.bmi.toString()

        weightUiModel.bmi?.let { bmi ->
            if (bmi < 18.5) setBg(underweightLayout)
            else if (bmi in 18.5..24.9) setBg(normalLayout)
            else if (bmi in 25.0..29.9) setBg(overweightLayout)
            else if (bmi in 30.0..39.9) setBg(obeseLayout)
            else setBg(morbidlyObeseLayout)
        }

    }

    private fun setBg(layout: LinearLayout) {
        layout.setBackgroundResource(R.color.dark_blue)
    }

}