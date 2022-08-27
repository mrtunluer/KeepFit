package com.mertdev.weighttracking.ui.measurementcontent

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.data.entity.MeasurementContent
import com.mertdev.weighttracking.utils.extensions.showDateWithoutYear

@SuppressLint("ViewConstructor")
class MeasurementContentChartMarkerView(context: Context, private var measurementContentList: List<MeasurementContent>) :
    MarkerView(context, R.layout.marker_view) {

    private var valueTxt: TextView? = null
    private var dateTxt: TextView? = null

    init {
        valueTxt = findViewById(R.id.value_txt)
        dateTxt = findViewById(R.id.date_txt)
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.x?.let {
            val content = measurementContentList[it.toInt()]
            valueTxt?.text = content.value.toString()
            dateTxt?.text = content.date?.showDateWithoutYear()
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat())
    }

}