package com.mendelin.square.repositories_screen

import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.mendelin.square.domain.usecase.GetRepositoriesUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SquareViewModelTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var viewModel: SquareViewModel
    private lateinit var useCase: GetRepositoriesUseCase

    @Before
    fun setUp() {
        hiltRule.inject()

        useCase = mockk<GetRepositoriesUseCase>()
        viewModel = SquareViewModel(useCase)
    }

    @Test
    fun squareViewModel_uiState_isEmpty() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()

            assertThat(state).isInstanceOf(SquareUiState::class.java)
            assertThat(state.errorMessage).isNull()
            assertThat(state.isLoading).isFalse()

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun squareViewModel_repoState_isPagingData() = runTest {
        viewModel.repoState.test {
            val state = awaitItem()

            assertThat(state).isInstanceOf(PagingData::class.java)
            cancelAndConsumeRemainingEvents()
        }
    }
}
