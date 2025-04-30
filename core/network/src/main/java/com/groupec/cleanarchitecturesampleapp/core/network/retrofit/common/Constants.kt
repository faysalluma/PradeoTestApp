package com.groupec.cleanarchitecturesampleapp.core.network.retrofit.common

import com.groupec.cleanarchitecturesampleapp.core.Constants

class Constants {
    companion object {
        const val BASE_URL = Constants.BASE_URL

        // Endpoints
        const val GET_ATTACHMENTS = "getAttachments"
        const val SAVE_ATTACHMENT = "saveAttachment"
        const val DELETE_ATTACHMENT = "deleteAttachment/{attachmentid}"
    }
}