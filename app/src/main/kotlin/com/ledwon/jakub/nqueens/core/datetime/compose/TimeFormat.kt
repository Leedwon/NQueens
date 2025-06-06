package com.ledwon.jakub.nqueens.core.datetime.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration

@ReadOnlyComposable
@Composable
fun formatMillis(millis: Long): String {
    val minutes = (millis / 1000) / 60
    val seconds = (millis / 1000) % 60
    val ms = millis % 1000
    return String.format(
        LocalConfiguration.current.locales[0],
        "%02d:%02d.%03d",
        minutes,
        seconds,
        ms
    )
}
