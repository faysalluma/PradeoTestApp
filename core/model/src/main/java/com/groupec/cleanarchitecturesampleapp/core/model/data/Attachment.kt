package com.groupec.cleanarchitecturesampleapp.core.model.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Attachment (
    val id: Int,
    val name: String,
    val datecreation: String,
    val uri: Uri ? = null
) : Parcelable