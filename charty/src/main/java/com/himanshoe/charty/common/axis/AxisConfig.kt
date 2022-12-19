package com.himanshoe.charty.common.axis

import androidx.compose.ui.graphics.Color

data class AxisConfig(
    val showAxis: Boolean,
    val isAxisDashed: Boolean,
    val showUnitLabels: Boolean,
    val showXLabels: Boolean,
    val xAxisColor: Color = Color.LightGray,
    val yAxisColor: Color = Color.LightGray,
    val textColor: Color,
)

object AxisConfigDefaults {

    fun axisConfigDefaults(isDarkMode: Boolean) = AxisConfig(
        xAxisColor = Color.LightGray,
        showAxis = true,
        isAxisDashed = true,
        showUnitLabels = true,
        showXLabels = true,
        yAxisColor = Color.LightGray,
        textColor = if (isDarkMode) Color.White else Color.Black
    )

    fun axisConfigDefaults2(isDarkMode: Boolean) = AxisConfig(
        xAxisColor = Color.LightGray,
        showAxis = false,
        isAxisDashed = true,
        showUnitLabels = true,
        showXLabels = true,
        yAxisColor = Color.LightGray,
        textColor = if (isDarkMode) Color.White else Color.Black
    )
}
