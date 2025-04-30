package com.groupec.cleanarchitecturesampleapp.core.ui

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.groupec.cleanarchitecturesampleapp.core.designsystem.component.AppLoadingScreen
import com.groupec.cleanarchitecturesampleapp.core.designsystem.component.DefaultButton
import com.groupec.cleanarchitecturesampleapp.core.designsystem.component.SelectFileTextField
import com.groupec.cleanarchitecturesampleapp.core.designsystem.component.UnderlinedTextButton
import java.io.File

@Composable
fun SelectFileCard(
    isLoading: Boolean = false,
    uri: Uri? = null, // target url to preview
    onSetUri: (Uri?) -> Unit = {}, // selected / taken uri
    saveAttachment: () -> Unit
) {
    val context = LocalContext.current
    var fileName by remember { mutableStateOf<String?>(null) }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                onSetUri(it)
                fileName = getFileName(context = context, uri = it)
            }
        }
    )

    var showGalleryImage by remember { mutableStateOf(false) }
    if (showGalleryImage) {
        // Launch picker
        filePicker.launch("*/*")
        showGalleryImage = false
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        UnderlinedTextButton(text = stringResource(R.string.cancel_upload)) {
            onSetUri.invoke(null)
            fileName = null
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SelectFileTextField(
                modifier = Modifier.weight(1f).padding(end = 12.dp),
                fileName = fileName,
                onSelectFile = {
                    showGalleryImage = it
                }
            )
            DefaultButton(
                modifier = Modifier.weight(1f),
                enabled = fileName?.isNotEmpty()  ?: false,
                text = stringResource(R.string.upload_to_server)
            ) {
                saveAttachment()
                fileName = null
            }

            if (isLoading) {
                AppLoadingScreen(
                    modifier = Modifier.wrapContentWidth()
                )
            }
        }
    }
}

fun getFileName(context: Context, uri: Uri): String? {
    return when (uri.scheme) {
        "content" -> {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst() && nameIndex != -1) {
                    cursor.getString(nameIndex)
                } else null
            }
        }
        "file" -> {
            File(uri.path ?: return null).name
        }
        else -> null
    }
}