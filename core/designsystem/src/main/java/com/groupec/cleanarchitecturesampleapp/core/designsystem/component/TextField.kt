package com.groupec.cleanarchitecturesampleapp.core.designsystem.component

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import com.groupec.cleanarchitecturesampleapp.core.designsystem.R


@Composable
fun SelectFileTextField(
    modifier: Modifier = Modifier,
    fileName: String?,
    onSelectFile: (Boolean) -> Unit
) {

    OutlinedTextField(
        value = fileName ?: stringResource(R.string.select_file),
        onValueChange = { },
        label = { Text("Select File") },
        placeholder = { Text(stringResource(R.string.no_file_selected)) },
        trailingIcon = {
            Icon(
                Icons.Default.AttachFile,
                contentDescription = stringResource(R.string.select_file)
            )
        },
        readOnly = true,
        modifier = modifier
            .pointerInput(fileName) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        onSelectFile(true)
                    }
                }
            }
    )
}
