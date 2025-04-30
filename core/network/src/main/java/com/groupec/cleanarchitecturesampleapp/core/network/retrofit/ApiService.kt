package com.groupec.cleanarchitecturesampleapp.core.network.retrofit

import com.groupec.cleanarchitecturesampleapp.core.network.model.AttachmentResponse
import com.groupec.cleanarchitecturesampleapp.core.network.retrofit.common.Constants
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

interface ApiService {
    @GET(Constants.GET_ATTACHMENTS)
    suspend fun getAttachments(): Response<AttachmentResponse>

    @Multipart
    @POST(Constants.SAVE_ATTACHMENT)
    suspend fun saveAttachment(
        @Part filePart: MultipartBody.Part?
    ): Response<Unit>

    @DELETE(Constants.DELETE_ATTACHMENT)
    suspend fun deleteAttachment(@Path("attachmentid") attachmentid: Int): Response<Unit>

    @GET
    @Streaming
    suspend fun downloadFile(@Url fileUrl: String): Response<ResponseBody>
}