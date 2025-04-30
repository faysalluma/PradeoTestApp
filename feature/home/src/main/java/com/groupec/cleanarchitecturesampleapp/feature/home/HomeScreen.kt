package com.groupec.cleanarchitecturesampleapp.feature.home

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.groupec.cleanarchitecturesampleapp.core.Constants
import com.groupec.cleanarchitecturesampleapp.core.FormUIState
import com.groupec.cleanarchitecturesampleapp.core.UIState
import com.groupec.cleanarchitecturesampleapp.core.designsystem.component.AppAlertInfoDialog
import com.groupec.cleanarchitecturesampleapp.core.designsystem.component.AppCustomDialog
import com.groupec.cleanarchitecturesampleapp.core.designsystem.component.AppLoadingScreen
import com.groupec.cleanarchitecturesampleapp.core.designsystem.component.EmptyScreen
import com.groupec.cleanarchitecturesampleapp.core.designsystem.component.ErrorScreen
import com.groupec.cleanarchitecturesampleapp.core.designsystem.component.SnackbarVisualsWithState
import com.groupec.cleanarchitecturesampleapp.core.ui.AttachmentCardList
import com.groupec.cleanarchitecturesampleapp.core.ui.SelectFileCard

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uri = remember { mutableStateOf<Uri?>(null) }
    val attachmentsState by viewModel.attachmentUiState.collectAsStateWithLifecycle()
    val saveAttachmentState by viewModel.saveAttachmentUiState.collectAsState()
    val deleteAttachmentState by viewModel.deleteAttachmentUiState.collectAsState()
    val isLoading = saveAttachmentState is FormUIState.Loading // For showing circular loading bar when file uploading

    // For showing confirm dialog
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var attachmentIdLibelle by remember { mutableStateOf(Pair(0, "")) }

    // Download attachment
    val downloadAttachmentState by viewModel.downloadAttachmentUiState.collectAsState()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            viewModel.downloadFile(Constants.UPLOAD_URL.plus(attachmentIdLibelle.second))
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // When adding attachment
    when (saveAttachmentState) {
        is FormUIState.Success -> {
            LaunchedEffect(Unit) {
                viewModel.getAttachments() // Refresh attachments list
                snackbarHostState.showSnackbar(
                    SnackbarVisualsWithState(
                        message = context.getString(R.string.success_message)
                    )
                )
            }
        }

        is FormUIState.Error -> {
            LaunchedEffect(Unit) {
                snackbarHostState.showSnackbar(
                    SnackbarVisualsWithState(
                        message =(saveAttachmentState as FormUIState.Error).message,
                        isError = true
                    )
                )
                viewModel.resetFlow()
            }
        }

        else -> {}
    }

    // When deleting attachment
    when (deleteAttachmentState) {
        is FormUIState.Success -> {
            LaunchedEffect(Unit) {
                snackbarHostState.currentSnackbarData?.dismiss()
                viewModel.getAttachments() // Refresh attachments list
                snackbarHostState.showSnackbar(
                    SnackbarVisualsWithState(
                        message = context.getString(R.string.delete_message)
                    )
                )
            }
        }

        is FormUIState.Error -> {
            LaunchedEffect(Unit) {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    SnackbarVisualsWithState(
                        message =(deleteAttachmentState as FormUIState.Error).message,
                        isError = true
                    )
                )
                viewModel.resetFlow()
            }
        }
        else -> {}
    }

    // When download attachment
    when (downloadAttachmentState) {
        is FormUIState.Loading -> {
            AppCustomDialog {
                AppLoadingScreen(
                    text = stringResource(R.string.downloads_in_progress),
                    modifier = Modifier.wrapContentSize()
                )
            }
        }

        is FormUIState.Success -> {
            LaunchedEffect(Unit) {
                val file = (downloadAttachmentState as FormUIState.Success).data
                viewModel.showDownloadNotification(context, file)
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    SnackbarVisualsWithState(
                        message = context.getString(R.string.download_message)
                    )
                )
                viewModel.resetFlow()
            }
        }

        is FormUIState.Error -> {
            LaunchedEffect(Unit) {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    SnackbarVisualsWithState(
                        message =(downloadAttachmentState as FormUIState.Error).message,
                        isError = true
                    )
                )
                viewModel.resetFlow()
            }
        }
        else -> {}
    }

    // Screen Components
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        // Select file card
        SelectFileCard(
            isLoading = isLoading,
            uri = uri.value,
            onSetUri = {
                uri.value = it
            },
            saveAttachment = {
                viewModel.saveAttachment(uri.value)
            }
        )

        // List of attachments
        Box {
            when (attachmentsState) {
                is UIState.Loading -> AppLoadingScreen()
                is UIState.Empty -> EmptyScreen()
                is UIState.Success -> AttachmentCardList(
                    attachments = (attachmentsState as UIState.Success).data,
                    onDownload = { id, filename ->
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        } else {
                            attachmentIdLibelle = Pair(id, filename)
                            viewModel.downloadFile(Constants.UPLOAD_URL.plus(filename))
                        }
                    },
                    onDelete = { id, name ->
                        showDeleteDialog = true
                        attachmentIdLibelle = Pair(id, name)
                    }
                )

                is UIState.Error -> ErrorScreen((attachmentsState as UIState.Error).message)
            }

            if (showDeleteDialog) {
                AppAlertInfoDialog(
                    setShowDialog = {
                        showDeleteDialog = it
                    },
                    title = stringResource(com.groupec.cleanarchitecturesampleapp.core.ui.R.string.confirm_delete_message, attachmentIdLibelle.second),
                    onConfirmButton = {
                        viewModel.deleteAttachment(attachmentIdLibelle.first)
                    },
                    onDismissButton = {
                        // Nothing to do
                    }
                )
            }
        }
    }
}