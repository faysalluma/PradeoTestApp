package com.groupec.cleanarchitecturesampleapp.core.network.model

import com.google.gson.annotations.SerializedName

data class AttachmentResponse(
    @SerializedName("attachments")
    val attachments : ArrayList<AttachmentItemResponse>
)
data class AttachmentItemResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("datecreation")
    val datecreation: String
)