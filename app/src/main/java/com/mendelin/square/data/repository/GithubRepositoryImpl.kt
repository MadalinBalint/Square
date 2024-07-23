package com.mendelin.square.data.repository

import com.mendelin.square.data.model.RepositoryModel
import com.mendelin.square.data.remote.GithubApi
import com.mendelin.square.domain.repository.GithubRepository
import retrofit2.Response
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(private val api: GithubApi) :
    GithubRepository {
    override suspend fun getRepositories(
        perPage: Int,
        page: Int
    ): Response<List<RepositoryModel>> =
        api.getRepositories(
            perPage = perPage,
            page = page,
        )
}
