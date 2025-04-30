package com.groupec.cleanarchitecturesampleapp.core.data.repository

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import com.groupec.cleanarchitecturesampleapp.core.Result
import com.groupec.cleanarchitecturesampleapp.core.UploadUtility.Companion.getRealPathFromURI
import com.groupec.cleanarchitecturesampleapp.core.data.model.toAttachmentList
import com.groupec.cleanarchitecturesampleapp.core.model.data.Attachment
import com.groupec.cleanarchitecturesampleapp.core.network.retrofit.ApiService
import com.groupec.cleanarchitecturesampleapp.core.network.retrofit.common.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.OutputStream
import java.net.URLConnection
import javax.inject.Inject
import javax.inject.Singleton
import android.provider.MediaStore
import android.content.ContentValues
import java.io.IOException

@Singleton
class AttachmentRepositoryImpl @Inject constructor(
    private val context: Context,
    private val apiService: ApiService
) :
    AttachmentRepository {
    override fun getAttachments(): Flow<List<Attachment>> = flow {
        val result = safeApiCall(
            apiCall = { apiService.getAttachments() },
            transform = { response ->
                response.toAttachmentList()
            }
        )
        emit(result)
    }.flowOn(Dispatchers.IO)

    override suspend fun saveAttachment(uri: Uri?): Result<Unit> {
        // Image part
        val imagePart = uri?.let{
            prepareImageForUpload(context, it)
        }

        return runCatching {
            val response =  apiService.saveAttachment(imagePart)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(HttpException(response))
            }
        }.getOrElse {
            Result.Error(it)
        }
    }

    override suspend fun deleteAttachment(attachmentId: Int): Result<Unit> {
        val result  = safeApiCall(
            apiCall = { apiService.deleteAttachment(attachmentId) },
            transform = {
                Result.Success(Unit)
            }
        )
        return result
    }


    override suspend fun downloadFile(url: String): Result<File> {
        return try {
            val response = apiService.downloadFile(url)
            if (!response.isSuccessful) {
                return Result.Error(Exception("HTTP error ${response.code()}"))
            }

            val fileName = url.substringAfterLast("/")
            val mimeType = URLConnection.guessContentTypeFromName(fileName) ?: "application/octet-stream"

            val outputStream: OutputStream
            val file: File

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, mimeType)
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                    ?: return Result.Error(IOException("MediaStore insertion failed"))

                outputStream = resolver.openOutputStream(uri) ?: return Result.Error(IOException("Stream null"))

                response.body()?.byteStream()?.use { input ->
                    outputStream.use { it.write(input.readBytes()) }
                }

                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)

                file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)

            } else {
                // Android 9 et moins : avec permission WRITE_EXTERNAL_STORAGE
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                file = File(downloadsDir, fileName)
                outputStream = file.outputStream()

                response.body()?.byteStream()?.use { input ->
                    outputStream.use { it.write(input.readBytes()) }
                }
            }

            Result.Success(file)

        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    fun prepareImageForUpload(context: Context, imageUri: Uri): MultipartBody.Part? {
        val filePath = getRealPathFromURI(context, imageUri) ?: return null // Get file path
        val file = File(filePath)
        val requestBody = file.asRequestBody("*/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            "filePart", // Nom du champ attendu par l'API
            file.name,
            requestBody
        )
    }
}