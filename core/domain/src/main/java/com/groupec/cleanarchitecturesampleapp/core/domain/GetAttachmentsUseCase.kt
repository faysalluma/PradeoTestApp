package com.groupec.cleanarchitecturesampleapp.core.domain

import com.groupec.cleanarchitecturesampleapp.core.data.repository.AttachmentRepository
import com.groupec.cleanarchitecturesampleapp.core.model.data.Attachment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAttachmentsUseCase @Inject constructor(private val attachmentRepository: AttachmentRepository) {
    operator fun invoke(): Flow<List<Attachment>> = attachmentRepository.getAttachments()
}