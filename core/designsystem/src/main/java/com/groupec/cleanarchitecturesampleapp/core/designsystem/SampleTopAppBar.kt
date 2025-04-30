package com.groupec.cleanarchitecturesampleapp.core.designsystem

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.groupec.cleanarchitecturesampleapp.core.designsystem.icon.AppIcons
import com.groupec.cleanarchitecturesampleapp.core.designsystem.theme.CleanArchitectureSampleAppTheme
import com.groupec.cleanarchitecturesampleapp.core.designsystem.theme.Green
import com.groupec.cleanarchitecturesampleapp.core.designsystem.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleTopAppBar(
    titleBar: String,
    onNavigationClick: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Green,
            titleContentColor = White,
            navigationIconContentColor = White,
            actionIconContentColor = White
        ),
        title = { Text(
            text = titleBar,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        ) },
        navigationIcon = {
            if (onNavigationClick != null) {
                IconButton(onClick = { onNavigationClick() }) {
                    Icon(
                        imageVector = AppIcons.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}


@Preview("Top App Bar")
@Composable
private fun NiaTopAppBarPreview() {
    CleanArchitectureSampleAppTheme {
        SampleTopAppBar(
            titleBar = "My top bar",
            onNavigationClick = {}
        )
    }
}