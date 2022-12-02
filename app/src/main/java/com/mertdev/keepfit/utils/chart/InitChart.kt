package com.mertdev.keepfit.utils.chart

import android.content.Context
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.mertdev.keepfit.data.entity.MeasurementContent
import com.mertdev.keepfit.data.entity.Weight
import com.mertdev.keepfit.ui.home.WeightChartMarkerView
import com.mertdev.keepfit.ui.measurementcontent.MeasurementContentChartMarkerView
import com.mertdev.keepfit.utils.Constants.EMPTY

object InitChart {
    fun setChart(
        entryList: List<BarEntry>,
        context: Context,
        chart: LineChart,
        weightList: List<Weight>? = null,
        measurementContentList: List<MeasurementContent>? = null
    ) {
        val lineDataSet = LineDataSet(entryList, EMPTY)
        ChartStyle.styleLineDataSet(context, lineDataSet)
        ChartStyle.styleChart(chart)
        chart.data = LineData(lineDataSet)

        chart.marker = if (weightList != null)
            WeightChartMarkerView(context, weightList)
        else
            MeasurementContentChartMarkerView(context, measurementContentList!!)

        chart.invalidate()
    }
}