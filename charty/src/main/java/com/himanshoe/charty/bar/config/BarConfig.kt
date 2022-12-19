package com.himanshoe.charty.bar.config

data class BarConfig(
    val hasRoundedCorner: Boolean = false
)

object BarConfigDefaults {

    fun barConfigDimesDefaults() = BarConfig(
        hasRoundedCorner = false
    )
}
