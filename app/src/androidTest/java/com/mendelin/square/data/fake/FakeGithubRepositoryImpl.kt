package com.mendelin.square.data.fake

import com.mendelin.square.data.model.FakeModel.getRepositoryModel
import com.mendelin.square.data.model.RepositoryModel
import com.mendelin.square.domain.repository.GithubRepository
import com.mendelin.square.util.Constants
import retrofit2.Response

class FakeGithubRepositoryImpl : GithubRepository {
    override suspend fun getRepositories(
        perPage: Int,
        page: Int
    ): Response<List<RepositoryModel>> {
        return Response.success(
            List(Constants.ITEMS_PER_PAGE) { getRepositoryModel(it) },
        )
    }
}
