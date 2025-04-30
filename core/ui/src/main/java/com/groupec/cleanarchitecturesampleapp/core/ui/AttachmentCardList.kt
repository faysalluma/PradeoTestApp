package com.groupec.cleanarchitecturesampleapp.core.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.groupec.cleanarchitecturesampleapp.core.model.data.Attachment

@Composable
fun AttachmentCardList(
    attachments: List<Attachment>,
    onDownload: (Int, String) -> Unit,
    onDelete: (Int, String) -> Unit
) {
    LazyColumn {
        items(attachments) { attachment ->
            AttachmentCard(
                item = attachment,
                onDownload = onDownload,
                onDelete = onDelete
            )
        }
    }
}
