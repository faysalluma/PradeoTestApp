package com.groupec.cleanarchitecturesampleapp.core.domain

import com.groupec.cleanarchitecturesampleapp.core.data.repository.AttachmentRepository
import com.groupec.cleanarchitecturesampleapp.core.Result
import java.io.File
import javax.inject.Inject

class DownloadAttachmentsUseCase @Inject constructor(private val attachmentRepository: AttachmentRepository) {
    suspend operator fun invoke(url: String): Result<File> = attachmentRepository.downloadFile(url)
}