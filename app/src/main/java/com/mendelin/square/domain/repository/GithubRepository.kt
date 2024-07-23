package com.mendelin.square.domain.repository

import com.mendelin.square.data.model.RepositoryModel
import retrofit2.Response

interface GithubRepository {
    suspend fun getRepositories(
        perPage: Int,
        page: Int
    ): Response<List<RepositoryModel>>
}
