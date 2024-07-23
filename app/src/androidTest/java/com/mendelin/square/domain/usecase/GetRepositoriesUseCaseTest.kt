package com.mendelin.square.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.mendelin.square.data.fake.FakeGithubRepositoryImpl
import com.mendelin.square.domain.Resource
import com.mendelin.square.domain.repository.GithubRepository
import com.mendelin.square.util.Constants
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class GetRepositoriesUseCaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var gson: Gson

    private lateinit var repository: GithubRepository
    private lateinit var useCase: GetRepositoriesUseCase

    @Before
    fun setUp() {
        hiltRule.inject()

        repository = FakeGithubRepositoryImpl()
        useCase = GetRepositoriesUseCase(repository, gson)
    }

    @Test
    fun getRepositoriesUseCase_call_successful() = runTest {
        useCase(Constants.ITEMS_PER_PAGE, 1).test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat(result.data).isNotNull()
            assertThat(result.data?.size).isEqualTo(Constants.ITEMS_PER_PAGE)

            val first = result.data?.firstOrNull()
            assertThat(first).isNotNull()
            assertThat(first?.id).isEqualTo(0)
            assertThat(first?.name).isEqualTo("kotlin0")

            cancelAndConsumeRemainingEvents()
        }
    }
}
