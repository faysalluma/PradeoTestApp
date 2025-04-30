package com.groupec.cleanarchitecturesampleapp.core.domain

import com.groupec.cleanarchitecturesampleapp.core.data.repository.AttachmentRepository
import javax.inject.Inject
import com.groupec.cleanarchitecturesampleapp.core.Result

class DeleteAttachmentUseCase @Inject constructor(private val attachmentRepository: AttachmentRepository) {
    suspend operator fun invoke(attachmentId: Int) : Result<Unit> = attachmentRepository.deleteAttachment(attachmentId)
}