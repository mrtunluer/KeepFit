package com.mertdev.weighttracking.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.data.entity.Weight
import com.mertdev.weighttracking.utils.extensions.showDateWithoutYear

@SuppressLint("ViewConstructor")
class WeightChartMarkerView(context: Context, private var weightList: List<Weight>) :
    MarkerView(context, R.layout.marker_view) {

    private var valueTxt: TextView? = null
    private var dateTxt: TextView? = null

    init {
        valueTxt = findViewById(R.id.value_txt)
        dateTxt = findViewById(R.id.date_txt)
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.x?.let {
            val weight = weightList[it.toInt()]
            valueTxt?.text = weight.value.toString()
            dateTxt?.text = weight.date?.showDateWithoutYear()
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat())
    }

}