package com.groupec.cleanarchitecturesampleapp.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Now in Android icons. Material icons are [ImageVector]s, custom icons are drawable resource IDs.
 */
object AppIcons {
    val ArrowBack = Icons.AutoMirrored.Filled.ArrowBack
    val MoreVert = Icons.Default.MoreVert
    val Delete = Icons.Default.Cancel
    val Download = Icons.Default.Download
}