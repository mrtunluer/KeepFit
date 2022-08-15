package com.mertdev.weighttracking.ui.home.chart

import android.content.Context
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineDataSet
import com.mertdev.weighttracking.R

object ChartStyle {

    fun styleChart(lineChart: LineChart) = lineChart.apply {
        xAxis.isEnabled = false

        axisLeft.apply {
            initAxis(this, context)
        }

        axisRight.apply {
            initAxis(this, context)
        }

        setTouchEnabled(true)
        isDragEnabled = false
        setScaleEnabled(false)
        setPinchZoom(false)
        description = null
        legend.isEnabled = false
    }

    fun styleLineDataSet(context: Context, lineDataSet: LineDataSet) = lineDataSet.apply {
        color = ContextCompat.getColor(context, R.color.light_blue)
        valueTextColor = ContextCompat.getColor(context, R.color.white)
        circleHoleColor = ContextCompat.getColor(context, R.color.secondary_blue)
        setCircleColor(ContextCompat.getColor(context, R.color.secondary_blue))
        highLightColor = ContextCompat.getColor(context, R.color.blue)
        setDrawValues(false)
        setDrawVerticalHighlightIndicator(false)
        setDrawHorizontalHighlightIndicator(true)
        setDrawCircles(true)
        highlightLineWidth = context.resources.getDimension(R.dimen.chartLineWidth)
        isHighlightEnabled = true
        lineWidth = context.resources.getDimension(R.dimen.chartLineWidth)
        mode = LineDataSet.Mode.HORIZONTAL_BEZIER
    }

    private fun initAxis(axis: YAxis, context: Context) = with(axis) {
        textColor = ContextCompat.getColor(context, R.color.white)
        textSize = context.resources.getDimension(R.dimen.chartTextSize)
        isGranularityEnabled = false
        setDrawAxisLine(true)
        setDrawGridLines(false)
        axisLineColor = ContextCompat.getColor(context, R.color.blue)
        axisLineWidth = context.resources.getDimension(R.dimen.chartLineWidth)
    }

}