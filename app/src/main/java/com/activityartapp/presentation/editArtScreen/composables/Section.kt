package com.activityartapp.presentation.editArtScreen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.activityartapp.presentation.common.type.Body
import com.activityartapp.presentation.common.type.TitleTwo
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun ColumnScope.Section(
    header: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    TitleTwo(text = header)
    Body(text = description)
    content()
}