package com.mendelin.square.repositories_screen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mendelin.square.data.model.RepositoryModel
import com.mendelin.square.data.model.toRepositoryInfo
import com.mendelin.square.domain.Resource
import com.mendelin.square.domain.model.RepositoryInfo
import com.mendelin.square.domain.usecase.GetRepositoriesUseCase
import com.mendelin.square.util.Constants
import kotlinx.coroutines.flow.lastOrNull
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class SquarePageSource @Inject constructor(
    private val useCase: GetRepositoriesUseCase,
    private val callback: PageStatusCallback
) : PagingSource<Int, RepositoryInfo>() {
    override fun getRefreshKey(state: PagingState<Int, RepositoryInfo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryInfo> {
        return try {
            val currentPage = params.key ?: 1

            callback.onLoading()
            val response = useCase(Constants.ITEMS_PER_PAGE, currentPage).lastOrNull()
            Timber.d("Current page = $currentPage")

            when (response) {
                is Resource.Success -> {
                    getSuccessLoadResult(response, currentPage)
                }

                is Resource.Error -> {
                    val message = response.message ?: "Unknown error"
                    callback.onError(message)
                    LoadResult.Error(Exception(message))
                }

                null -> {
                    val message = "Received a null response!"
                    callback.onError(message)
                    LoadResult.Error(Exception(message))
                }
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    private fun getSuccessLoadResult(
        response: Resource<List<RepositoryModel>>,
        currentPage: Int
    ): LoadResult<Int, RepositoryInfo> =
        when (response.data?.isNotEmpty()) {
            true -> {
                val repositories =
                    response.data.map(RepositoryModel::toRepositoryInfo)

                callback.onSuccess()
                LoadResult.Page(
                    data = repositories,
                    prevKey = if (currentPage <= 1) null else currentPage.minus(1),
                    nextKey = currentPage.plus(1)
                )
            }

            false -> {
                callback.onSuccess()
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

            null -> {
                val message = response.message ?: "Received null data in response!"
                callback.onError(message)
                LoadResult.Error(Exception(message))
            }
        }
}
