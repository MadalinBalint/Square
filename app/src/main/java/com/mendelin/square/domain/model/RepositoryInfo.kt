package com.mendelin.square.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RepositoryInfo(
    val id: Int,
    val ownerAvatar: String,
    val ownerName: String,
    val repositoryName: String,
    val repositoryTitle: String,
    val repositoryDesc: String,
    val repositoryUrl: String,

    val language: String?,
    val licenseType: String?,
    val licenseUrl: String?,
    val topics: String?,
) : Parcelable
