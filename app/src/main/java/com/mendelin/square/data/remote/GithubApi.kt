package com.mendelin.square.data.remote

import com.mendelin.square.data.model.RepositoryModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("orgs/square/repos")
    suspend fun getRepositories(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): Response<List<RepositoryModel>>
}
