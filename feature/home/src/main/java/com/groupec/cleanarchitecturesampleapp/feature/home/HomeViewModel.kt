package com.groupec.cleanarchitecturesampleapp.feature.home

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.groupec.cleanarchitecturesampleapp.core.FormUIState
import com.groupec.cleanarchitecturesampleapp.core.Result
import com.groupec.cleanarchitecturesampleapp.core.UIState
import com.groupec.cleanarchitecturesampleapp.core.asResult
import com.groupec.cleanarchitecturesampleapp.core.domain.DeleteAttachmentUseCase
import com.groupec.cleanarchitecturesampleapp.core.domain.DownloadAttachmentsUseCase
import com.groupec.cleanarchitecturesampleapp.core.domain.GetAttachmentsUseCase
import com.groupec.cleanarchitecturesampleapp.core.domain.SaveAttachmentUseCase
import com.groupec.cleanarchitecturesampleapp.core.model.data.Attachment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.net.URLConnection
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAttachmentsUseCase: GetAttachmentsUseCase,
    private val saveAttachmentUseCase: SaveAttachmentUseCase,
    private val deleteAttachmentUseCase: DeleteAttachmentUseCase,
    private val downloadAttachmentsUseCase: DownloadAttachmentsUseCase
) : ViewModel() {

    private val _attachmentUiState = MutableStateFlow<UIState<List<Attachment>>>(UIState.Loading)
    val attachmentUiState: StateFlow<UIState<List<Attachment>>> = _attachmentUiState.asStateFlow()

    private val _saveAttachmentUiState = MutableStateFlow<FormUIState<*>>(FormUIState.Idle)
    val saveAttachmentUiState : StateFlow<FormUIState<*>> = _saveAttachmentUiState.asStateFlow()

    private val _deleteAttachmentUiState = MutableStateFlow<FormUIState<*>>(FormUIState.Idle)
    val deleteAttachmentUiState: StateFlow<FormUIState<*>> = _deleteAttachmentUiState.asStateFlow()

    private val _downloadAttachmentUiState = MutableStateFlow<FormUIState<File>>(FormUIState.Idle)
    val downloadAttachmentUiState : StateFlow<FormUIState<File>> = _downloadAttachmentUiState.asStateFlow()

    init {
        getAttachments()
    }

    fun getAttachments() {
        viewModelScope.launch {
            getAttachmentsUseCase()
                .asResult()
                .collect { result ->
                    _attachmentUiState.value = when (result) {
                        is Result.Loading -> UIState.Loading
                        is Result.Success -> {
                            if (result.data.isEmpty()) {
                                UIState.Empty
                            } else {
                                UIState.Success(result.data)
                            }
                        }

                        is Result.Error -> UIState.Error(
                            result.exception.message ?: "Retrofit Unknown error"
                        )
                    }
                }
        }
    }

    fun saveAttachment(uri: Uri?) {
        _saveAttachmentUiState.value = FormUIState.Loading
        viewModelScope.launch {
            when (val result = saveAttachmentUseCase(uri)) {
                is Result.Success -> {
                    _saveAttachmentUiState.value = FormUIState.Success(Unit)
                }
                is Result.Error -> {
                    _saveAttachmentUiState.value = FormUIState.Error(result.exception.message ?: "Error when adding attachment")
                }
                else -> {}
            }
        }
    }

    fun deleteAttachment(id: Int) {
        _deleteAttachmentUiState.value = FormUIState.Loading
        viewModelScope.launch {
            when (val result = deleteAttachmentUseCase(id)) {
                is Result.Success -> {
                    _deleteAttachmentUiState.value = FormUIState.Success(Unit)
                }

                is Result.Error -> {
                    _deleteAttachmentUiState.value =
                        FormUIState.Error(result.exception.message ?: "Error when deleting attachment")
                }

                else -> {}
            }
        }
    }

    fun downloadFile(url: String) {
        _downloadAttachmentUiState.value = FormUIState.Loading
        viewModelScope.launch {
            when (val result = downloadAttachmentsUseCase(url)) {
                is Result.Success -> {
                    _downloadAttachmentUiState.value = FormUIState.Success(result.data)
                }

                is Result.Error -> {
                    _downloadAttachmentUiState.value =
                        FormUIState.Error(result.exception.message ?: "Error when download attachment")
                }
                else -> {}
            }
        }
    }

    fun showDownloadNotification(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, URLConnection.guessContentTypeFromName(file.name))
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "download_channel")
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle("Téléchargement terminé")
            .setContentText(file.name)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification)
    }

    fun resetFlow() {
        _saveAttachmentUiState.value = FormUIState.Idle
        _deleteAttachmentUiState.value = FormUIState.Idle
        _downloadAttachmentUiState.value = FormUIState.Idle
    }
}
