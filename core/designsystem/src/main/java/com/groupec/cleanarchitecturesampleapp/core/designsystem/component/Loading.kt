package com.groupec.cleanarchitecturesampleapp.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.groupec.cleanarchitecturesampleapp.core.designsystem.theme.Primary

@Composable
fun AppLoadingScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    text: String? = null,
    color: Color = Primary,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = color)
        text?.let { Text(text = it, modifier = Modifier.padding(top = 12.dp)) }
    }
}

