package com.mendelin.square.domain

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.mendelin.square.data.model.FakeModel
import com.mendelin.square.data.model.ErrorResponse
import com.mendelin.square.data.model.toRepositoryInfo
import com.mendelin.square.data.remote.GithubApi
import com.mendelin.square.domain.repository.GithubRepository
import com.mendelin.square.data.repository.GithubRepositoryImpl
import com.mendelin.square.util.Constants
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RepositoryTest {
    @MockK
    lateinit var githubApi: GithubApi

    private lateinit var objectUnderTest: GithubRepository
    private lateinit var gson: Gson

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        objectUnderTest = GithubRepositoryImpl(githubApi)
        gson = GsonBuilder().setStrictness(Strictness.LENIENT)
            .setDateFormat(Constants.DATE_FORMAT)
            .create()
    }

    @Test
    fun repositoryModel_toRepository_has_correct_data() {
        /* Given */
        val id = 1
        val model = FakeModel.getRepositoryModel(id)

        /* When */
        val result = model.toRepositoryInfo()

        /* Then */
        assertThat(result.id).isEqualTo(id)
        assertThat(result.ownerAvatar).isEqualTo("https://avatars.githubusercontent.com/u/878437?v=4")
        assertThat(result.ownerName).isEqualTo("JetBrains$id")
        assertThat(result.repositoryName).isEqualTo("kotlin$id")
        assertThat(result.repositoryTitle).isEqualTo("JetBrains$id/kotlin$id")
        assertThat(result.repositoryDesc).isEqualTo("The Kotlin Programming Language.")
        assertThat(result.repositoryUrl).isEqualTo("https://github.com/JetBrains/kotlin")

        assertThat(result.language).isEqualTo("Kotlin")
        assertThat(result.licenseType).isEqualTo("MIT License")
        assertThat(result.licenseUrl).isEqualTo("https://api.github.com/licenses/mit")
        assertThat(result.topics).isEqualTo("compiler, gradle-plugin, intellij-plugin, kotlin, kotlin-library, maven-plugin, programming-language, wasm, webassembly")
    }

    @Test
    fun getRepositories_called_and_returns_success_contains_valid_data() =
        runTest {
            /* Given */
            val expectedResponse = listOf(FakeModel.getRepositoryModel(1))
            coEvery { githubApi.getRepositories(any(), any()) } returns Response.success(
                expectedResponse
            )

            /* When */
            val actualResponse = objectUnderTest.getRepositories(10, 1)

            /* Then */
            assertThat(actualResponse.isSuccessful).isTrue()
            assertThat(actualResponse.body()).isEqualTo(expectedResponse)
        }

    @Test
    fun getRepositories_called_and_returns_404_contains_error() =
        runTest {
            /* Given */
            val expectedResponse = ErrorResponse(
                message = "Only the first 1000 search results are available",
                documentation_url = "https://docs.github.com/v3/search/",
                status = "404"
            )
            coEvery {
                githubApi.getRepositories(any(), any())
            } returns Response.error(
                404,
                "{\"message\":\"Only the first 1000 search results are available\",\"documentation_url\":\"https://docs.github.com/v3/search/\",\"status\":\"404\"}".toResponseBody()
            )

            /* When */
            val actualResponse = objectUnderTest.getRepositories(10, 60)

            /* Then */
            assertThat(actualResponse.isSuccessful).isFalse()
            assertThat(actualResponse.code()).isEqualTo(404)

            val errorBody = actualResponse.errorBody()?.string()
            assertThat(errorBody).isNotEmpty()

            val body = gson.fromJson(errorBody, ErrorResponse::class.java)
            assertThat(body).isNotNull()
            assertThat(body).isEqualTo(expectedResponse)
        }
}
