package com.groupec.cleanarchitecturesampleapp.core.domain

import android.net.Uri
import com.groupec.cleanarchitecturesampleapp.core.Result
import com.groupec.cleanarchitecturesampleapp.core.data.repository.AttachmentRepository
import javax.inject.Inject

class SaveAttachmentUseCase @Inject constructor(private val attachmentRepository: AttachmentRepository) {
    operator suspend fun invoke(uri : Uri?): Result<Unit> = attachmentRepository.saveAttachment(uri)
}