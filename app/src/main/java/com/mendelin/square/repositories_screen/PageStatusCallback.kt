package com.mendelin.square.repositories_screen

interface PageStatusCallback {
    fun onLoading()
    fun onSuccess()
    fun onError(message: String)
}
