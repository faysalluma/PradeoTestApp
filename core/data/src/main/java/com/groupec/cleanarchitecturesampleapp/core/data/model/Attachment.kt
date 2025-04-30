package com.groupec.cleanarchitecturesampleapp.core.data.model

import com.groupec.cleanarchitecturesampleapp.core.model.data.Attachment
import com.groupec.cleanarchitecturesampleapp.core.network.model.AttachmentItemResponse
import com.groupec.cleanarchitecturesampleapp.core.network.model.AttachmentResponse


fun AttachmentResponse.toAttachmentList(): List<Attachment> = attachments.map { it.toAttachment() }

fun AttachmentItemResponse.toAttachment(): Attachment = Attachment(
    id = id,
    name = name,
    datecreation = datecreation
)