package com.activityartapp.presentation.common.type

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.activityartapp.R

@Composable
fun TitleFour(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color? = null
) {
    Text(
        text = text,
        color = textColor ?: colorResource(id = R.color.light_text_primary),
        style = MaterialTheme.typography.h4,
        modifier = modifier
    )
}