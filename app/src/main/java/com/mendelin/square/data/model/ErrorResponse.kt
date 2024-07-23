package com.mendelin.square.data.model

import androidx.annotation.Keep

@Keep
data class ErrorResponse(
    val message: String,
    val documentation_url: String,
    val status: String,
)
