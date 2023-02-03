package com.activityartapp.presentation.common.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun ColumnSmallSpacing(
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        content = content,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.spacedBy(
            space = spacing.small,
            alignment = verticalAlignment
        )
    )
}
