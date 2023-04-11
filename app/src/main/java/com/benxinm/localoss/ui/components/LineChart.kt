package com.benxinm.localoss.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.benxinm.localoss.ui.theme.MainColor
import com.benxinm.localoss.ui.theme.Purple500
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.renderer.line.LineDrawer
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.line.SolidLineShader
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.line.renderer.point.NoPointDrawer
import com.github.tehras.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.SimpleYAxisDrawer

@Composable
fun MyLineChart(/*data: List<Int>, pieces: Int*/) {
    LineChart(
        linesChartData = listOf(
            LineChartData(
                points = listOf(
                    LineChartData.Point(
                        value = 1.0f,
                        ""
                    ),
                    LineChartData.Point(value = 2f, ""),
                    LineChartData.Point(value = 3f, ""),
                    LineChartData.Point(value = 4.5f, ""),
                    LineChartData.Point(value = 4f, ""),
                    LineChartData.Point(value = 16f,"")
                ), padBy = 1f, startAtZero = false, lineDrawer = SolidLineDrawer(color = MainColor, thickness = 2.dp)
            )
        ),
        xAxisDrawer = SimpleXAxisDrawer(labelTextColor = Color.LightGray, axisLineColor = Color.Transparent),
        yAxisDrawer = SimpleYAxisDrawer(labelTextColor = Color.LightGray,axisLineColor = Color.Transparent),
        pointDrawer = NoPointDrawer,
        labels = listOf("01-25","01-31","02-06","02-12","02-18","02-24"), modifier = Modifier.fillMaxSize()
    )
}