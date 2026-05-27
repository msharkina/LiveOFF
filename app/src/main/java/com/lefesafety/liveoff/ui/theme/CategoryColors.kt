package com.lefesafety.liveoff.ui.theme

import androidx.compose.ui.graphics.Color

object CategoryColors {
    private val colorMap = mapOf(
        "#ff8c00" to AccentOrange,
        "#3498db" to AccentBlue,
        "#e74c3c" to AccentRed,
        "#2ecc71" to AccentGreen,
        "#ff2200" to AccentSosRed,
        "#a29bfe" to AccentPurple
    )
    fun fromHex(hex: String): Color = colorMap[hex.lowercase()] ?: AccentOrange
}
