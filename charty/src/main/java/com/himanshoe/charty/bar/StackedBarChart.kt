package com.himanshoe.charty.bar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import com.himanshoe.charty.bar.common.calculations.getStackedTopLeft
import com.himanshoe.charty.bar.common.calculations.getTopLeft
import com.himanshoe.charty.bar.common.calculations.getTopRight
import com.himanshoe.charty.bar.common.component.drawBarLabel
import com.himanshoe.charty.bar.config.BarConfig
import com.himanshoe.charty.bar.config.BarConfigDefaults
import com.himanshoe.charty.bar.model.StackedBarData
import com.himanshoe.charty.bar.model.isValid
import com.himanshoe.charty.bar.model.maxYValue
import com.himanshoe.charty.common.axis.AxisConfig
import com.himanshoe.charty.common.axis.AxisConfigDefaults
import com.himanshoe.charty.common.axis.drawYAxisWithLabels
import com.himanshoe.charty.common.dimens.ChartDimens
import com.himanshoe.charty.common.dimens.ChartDimensDefaults
import kotlin.math.ceil

@Composable
fun StackedBarChart(
    stackBarData: List<StackedBarData>,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    onBarClick: (StackedBarData) -> Unit = {},
    chartDimens: ChartDimens = ChartDimensDefaults.chartDimesDefaults(),
    axisConfig: AxisConfig = AxisConfigDefaults.axisConfigDefaults(isSystemInDarkTheme()),
    barConfig: BarConfig = BarConfigDefaults.barConfigDimesDefaults()
) {
    if (stackBarData.isValid(colors.count()).not()) {
        throw IllegalArgumentException("Colors count should be total to number of values in StackedBarData's yValue")
    }

    val maxYValue = stackBarData.maxYValue()
    var barWidth = 0F
    val clickedBar = remember { mutableStateOf(Offset(-10F, -10F)) }

    Canvas(
        modifier = modifier
            .drawBehind {
                if (axisConfig.showAxis) {
                    drawYAxisWithLabels(axisConfig, maxYValue, textColor = axisConfig.textColor)
                }
            }
            .padding(horizontal = chartDimens.padding)
            .pointerInput(Unit) {
                detectTapGestures(onPress = { offset ->
                    clickedBar.value = offset
                })
            }
    ) {
        barWidth =
            size.width.div(stackBarData.count().times(if (stackBarData.count() > 1) 1.2F else 1f))
        val yScalableFactor = size.height.div(maxYValue)

        stackBarData.reversed().forEachIndexed { index, stackBarDataIndividual ->
            drawIndividualStackedBar(
                index, stackBarDataIndividual, barWidth, yScalableFactor, barConfig, colors
            )
        }
        drawLabels(
            stackBarData,
            yScalableFactor,
            barWidth,
            axisConfig,
            clickedBar.value,
            onBarClick,
            axisConfig.textColor
        )
    }
}

private fun DrawScope.drawLabels(
    stackBarData: List<StackedBarData>,
    yScalableFactor: Float,
    width: Float,
    axisConfig: AxisConfig,
    clickedBarValue: Offset,
    onBarClick: (StackedBarData) -> Unit,
    labelTextColor: Color,
) {
    stackBarData.forEachIndexed { index, stackBarDataIndividual ->
        val barHeight = stackBarDataIndividual.yValue.sum().times(yScalableFactor)
        val barTopLeft = getTopLeft(
            index = index,
            barWidth = width,
            size = size,
            yValue = stackBarDataIndividual.yValue.sum(),
            yScalableFactor = yScalableFactor,
            widthExtensionMultiplier = if (stackBarData.count() > 1) 1.2F else 1f,
        )
        val barTopRight = getTopRight(
            index = index,
            barWidth = width,
            size = size,
            yValue = stackBarDataIndividual.yValue.sum(),
            yScaleFactor = yScalableFactor,
            widthExtensionMultiplier = if (stackBarData.count() > 1) 1.2F else 1f,
        )
        if (clickedBarValue.x in (barTopLeft.x..barTopRight.x)) {
            onBarClick(stackBarDataIndividual)
        }
        if (axisConfig.showXLabels) {

            val showLabel = (index % (ceil(stackBarData.count() / 20.0)).toInt()) == 0

            if (showLabel) {
                drawBarLabel(
                    stackBarDataIndividual.xValue,
                    width.times(if (stackBarData.count() > 1) 1.4F else 1f),
                    barHeight,
                    barTopLeft,
                    stackBarData.count(),
                    labelTextColor
                )
            }

        }
    }
}

private fun DrawScope.drawIndividualStackedBar(
    index: Int,
    stackBarData: StackedBarData,
    barWidth: Float,
    yScalableFactor: Float,
    barConfig: BarConfig,
    colors: List<Color>,
) {

    var individualHeight = 0F
    stackBarData.yValue.forEachIndexed { individualIndex, value ->
        val individualBarHeight = value.times(yScalableFactor)
        val topLeft = getStackedTopLeft(
            index,
            barWidth,
            individualHeight,
        )
        rotate(180F) {
            drawRoundRect(
                cornerRadius = CornerRadius(if (barConfig.hasRoundedCorner) individualBarHeight else 0F),
                topLeft = topLeft,
                color = colors[individualIndex],
                size = Size(barWidth, individualBarHeight)
            )
        }

        individualHeight += (individualBarHeight)
    }
}
