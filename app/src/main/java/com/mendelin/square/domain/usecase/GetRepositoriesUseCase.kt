package com.mendelin.square.domain.usecase

import com.google.gson.Gson
import com.mendelin.square.data.model.ErrorResponse
import com.mendelin.square.domain.Resource
import com.mendelin.square.domain.repository.GithubRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRepositoriesUseCase @Inject constructor(
    private val repository: GithubRepository,
    private val gson: Gson,
) {
    operator fun invoke(pageItems: Int, page: Int) = flow {
        try {
            val apiResponse = repository.getRepositories(pageItems, page)
            when {
                apiResponse.isSuccessful -> {
                    val body = apiResponse.body()
                    if (body != null) {
                        emit(Resource.Success(body))
                    } else {
                        emit(Resource.Error(message = "Null body exception"))
                    }
                }

                apiResponse.code() in 400..499 -> {
                    val json = apiResponse.errorBody()?.string()
                    val body = gson.fromJson(json, ErrorResponse::class.java)
                    if (body != null) {
                        emit(Resource.Error(message = "${body.message}\n${body.documentation_url}"))
                    } else {
                        emit(Resource.Error(message = "Null body exception on ${apiResponse.code()}"))
                    }
                }

                else -> {
                    emit(Resource.Error(message = apiResponse.message()))
                }
            }
        } catch (ex: Exception) {
            emit(Resource.Error(message = ex.localizedMessage ?: "Exception"))
        }
    }
}
