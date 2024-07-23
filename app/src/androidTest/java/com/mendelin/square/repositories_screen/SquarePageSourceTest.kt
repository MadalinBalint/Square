package com.mendelin.square.repositories_screen

import androidx.paging.PagingSource
import com.google.gson.Gson
import com.mendelin.square.data.model.FakeModel.getRepositoryModel
import com.mendelin.square.data.model.RepositoryModel
import com.mendelin.square.data.model.toRepositoryInfo
import com.mendelin.square.data.remote.GithubApi
import com.mendelin.square.data.repository.GithubRepositoryImpl
import com.mendelin.square.domain.model.RepositoryInfo
import com.mendelin.square.domain.repository.GithubRepository
import com.mendelin.square.domain.usecase.GetRepositoriesUseCase
import com.mendelin.square.util.Constants
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import javax.inject.Inject

@HiltAndroidTest
class SquarePageSourceTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var api: GithubApi

    @Inject
    lateinit var gson: Gson

    private lateinit var useCase: GetRepositoriesUseCase
    private lateinit var repository: GithubRepository
    private lateinit var pagingSource: SquarePageSource

    @Before
    fun setUp() {
        hiltRule.inject()

        repository = GithubRepositoryImpl(api)
        useCase = GetRepositoriesUseCase(repository, gson)
        pagingSource = SquarePageSource(
            useCase = useCase,
            callback = object : PageStatusCallback {
                override fun onLoading() {}
                override fun onSuccess() {}
                override fun onError(message: String) {}
            }
        )
    }

    @Test
    fun squarePageSource_returns_success_load_result() = runTest {
        /* Given */
        coEvery { api.getRepositories(any(), any()) } returns getResponseSuccess()

        val params = PagingSource
            .LoadParams
            .Refresh(
                key = 0,
                loadSize = Constants.ITEMS_PER_PAGE,
                placeholdersEnabled = false
            )

        val expected = PagingSource
            .LoadResult
            .Page(
                data = getRepositoryList(),
                prevKey = null,
                nextKey = 1
            )

        /* When */
        val actual = pagingSource.load(params = params)

        /* Then */
        assertEquals(expected, actual)
    }

    @Test
    fun squarePageSource_returns_error_load_result() = runTest {
        /* Given */
        coEvery { api.getRepositories(any(), any()) } returns getResponseError(400)

        val params = PagingSource
            .LoadParams
            .Refresh(
                key = 0,
                loadSize = Constants.ITEMS_PER_PAGE,
                placeholdersEnabled = false
            )

        val expected = PagingSource
            .LoadResult
            .Error<Int, RepositoryInfo>(Exception("Error"))::class.java

        /* When */
        val actual = pagingSource.load(params = params)::class.java

        /* Then */
        assertEquals(expected, actual)
    }

    @Test
    fun squarePageSource_next_page_returns_success_load_result() = runTest {
        /* Given */
        coEvery { api.getRepositories(any(), any()) } returns getResponseSuccess()

        val params = PagingSource
            .LoadParams
            .Refresh(
                key = 1,
                loadSize = Constants.ITEMS_PER_PAGE,
                placeholdersEnabled = false
            )

        val expected = PagingSource
            .LoadResult
            .Page(
                data = getRepositoryList(),
                prevKey = null,
                nextKey = 2
            )

        /* When */
        val actual = pagingSource.load(params = params)

        /* Then */
        assertEquals(expected, actual)
    }

    /* Successful response */
    private fun getResponseSuccess() =
        Response.success(
            List(Constants.ITEMS_PER_PAGE) {
                getRepositoryModel(it)
            },
        )

    /* Error response */
    private fun getResponseError(code: Int) =
        Response.error<List<RepositoryModel>>(code, "{}".toResponseBody())

    private fun getRepositoryList() =
        List(Constants.ITEMS_PER_PAGE) {
            getRepositoryModel(it).toRepositoryInfo()
        }
}
