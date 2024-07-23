package com.mendelin.square.repositories_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mendelin.square.domain.model.RepositoryInfo
import com.mendelin.square.domain.usecase.GetRepositoriesUseCase
import com.mendelin.square.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SquareViewModel @Inject constructor(
    private val useCase: GetRepositoriesUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SquareUiState())
    val uiState = _uiState.asStateFlow()

    private val _repoState = MutableStateFlow<PagingData<RepositoryInfo>>(PagingData.empty())
    val repoState = _repoState.asStateFlow()

    init {
        fetchRepositories()
    }

    private fun pagingData(): Flow<PagingData<RepositoryInfo>> =
        Pager(PagingConfig(pageSize = Constants.ITEMS_PER_PAGE, enablePlaceholders = true)) {
            SquarePageSource(
                useCase = useCase,
                callback = object : PageStatusCallback {
                    override fun onLoading() {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                            )
                        }
                    }

                    override fun onSuccess() {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                            )
                        }
                    }

                    override fun onError(message: String) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = message,
                            )
                        }
                    }
                })
        }.flow.cachedIn(viewModelScope)

    fun fetchRepositories() {
        viewModelScope.launch {
            pagingData().flowOn(ioDispatcher)
                .collectLatest { data ->
                    data.let { }
                    _repoState.value = data
                }
        }
    }
}
