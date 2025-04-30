package com.groupec.cleanarchitecturesampleapp.core.data.repository


import android.net.Uri
import com.groupec.cleanarchitecturesampleapp.core.Result
import com.groupec.cleanarchitecturesampleapp.core.model.data.Attachment
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AttachmentRepository {
    fun getAttachments() : Flow<List<Attachment>>
    suspend fun saveAttachment(uri: Uri?) : Result<Unit>
    suspend fun deleteAttachment(attachmentId: Int) : Result<Unit>
    suspend fun downloadFile(url: String) : Result<File>
}