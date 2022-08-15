package com.mertdev.weighttracking.ui.home.chart

import android.content.Context
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.mertdev.weighttracking.data.entity.Weight
import com.mertdev.weighttracking.utils.Constants.EMPTY

object InitChart {
    fun setChart(
        entryList: List<BarEntry>,
        context: Context,
        chart: LineChart,
        weightList: List<Weight>
        ){
        val lineDataSet = LineDataSet(entryList, EMPTY)
        ChartStyle.styleLineDataSet(context, lineDataSet)
        ChartStyle.styleChart(chart)
        chart.data = LineData(lineDataSet)
        chart.invalidate()
    }
}