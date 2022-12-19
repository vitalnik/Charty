package com.himanshoe.charty.bar.common.calculations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

internal fun getTopLeft(
    index: Int,
    barWidth: Float,
    size: Size,
    yValue: Float,
    yScalableFactor: Float,
    widthExtensionMultiplier: Float = 1.2f
) = Offset(
    x = index.times(barWidth.times(widthExtensionMultiplier)),
    y = size.height.minus(yValue.times(yScalableFactor))
)

internal fun getStackedTopLeft(
    index: Int,
    barWidth: Float,
    barHeight: Float,
    widthExtensionMultiplier: Float = 1.2f
) = Offset(
    x = index.times(barWidth.times(widthExtensionMultiplier)),
    y = (barHeight)
)

internal fun getTopRight(
    index: Int,
    barWidth: Float,
    size: Size,
    yValue: Float,
    yScaleFactor: Float,
    widthExtensionMultiplier: Float = 1.2f

) = Offset(
    x = index.plus(1).times(barWidth.times(widthExtensionMultiplier)),
    y = size.height.minus(yValue.times(yScaleFactor))
)
