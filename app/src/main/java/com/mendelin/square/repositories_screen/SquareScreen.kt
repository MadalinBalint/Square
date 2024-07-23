package com.mendelin.square.repositories_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.mendelin.square.R
import com.mendelin.square.domain.model.RepositoryInfo
import com.mendelin.square.util.Constants

@Composable
fun SquareScreen(
    viewModel: SquareViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val repoState = viewModel.repoState.collectAsLazyPagingItems()

    Scaffold(
        content = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    when {
                        repoState.itemCount > 0 ->
                            SquareScreenList(repoState = repoState)

                        uiState.errorMessage != null ->
                            SquareScreenErrorMessage(
                                emptyList = repoState.itemCount == 0,
                                uiState = uiState,
                                viewModel = viewModel
                            )

                        !uiState.isLoading && repoState.itemCount == 0 ->
                            SquareScreenNoRepositories()
                    }
                }

                if (uiState.isLoading) {
                    SquareScreenProgress()
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ColumnScope.SquareScreenList(repoState: LazyPagingItems<RepositoryInfo>) {
    val navigator = rememberListDetailPaneScaffoldNavigator<RepositoryInfo>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        modifier = Modifier.weight(1f),
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                ListLayout(
                    list = repoState,
                    onItemClick = { item ->
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Detail,
                            item
                        )
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                // Show the detail pane content if selected item is available
                navigator.currentDestination?.content?.let {
                    DetailsLayout(it)
                }
            }
        },
    )
}

@Composable
fun ColumnScope.SquareScreenErrorMessage(
    emptyList: Boolean,
    uiState: SquareUiState,
    viewModel: SquareViewModel
) {
    if (emptyList) Spacer(modifier = Modifier.weight(1f))
    Column(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
            .align(Alignment.CenterHorizontally)
    ) {
        Text(
            text = "Error: ${uiState.errorMessage}",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.fetchRepositories() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(id = R.string.retry_button))
        }
    }
}

@Composable
fun ColumnScope.SquareScreenNoRepositories() {
    Spacer(modifier = Modifier.weight(1f))
    Text(
        modifier = Modifier
            .padding(24.dp)
            .align(Alignment.CenterHorizontally),
        text = stringResource(R.string.empty_list),
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun SquareScreenProgress() {
    CircularProgressIndicator(
        modifier = Modifier
            .width(96.dp)
            .testTag(Constants.CIRCULAR_PROGRESS_TAG),
        strokeWidth = 8.dp,
        color = MaterialTheme.colorScheme.secondary,
        trackColor = Color(0xff4CBB17),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListLayout(list: LazyPagingItems<RepositoryInfo>, onItemClick: (RepositoryInfo) -> Unit) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .semantics { testTag = Constants.LIST_TAG },
        state = listState

    ) {
        items(list.itemCount, key = list.itemKey { it.id }) { index ->
            Row(Modifier.animateItemPlacement()) {
                ItemLayout(item = list[index]!!, onItemClick)
            }
        }
    }
}

@Composable
fun ItemLayout(item: RepositoryInfo, onItemClick: (RepositoryInfo) -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .border(
                BorderStroke(width = 1.dp, color = Color.Black),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onItemClick(item) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OwnerName(name = item.ownerName)
            RepositoryName(name = item.repositoryName)
            RepositoryTitle(title = item.repositoryTitle)
        }
    }
}

@Composable
fun DetailsLayout(item: RepositoryInfo) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            OwnerAvatarImage(imageUrl = item.ownerAvatar, size = 196.dp)
        }
        Column(
            modifier = Modifier
                .padding(24.dp)

        ) {
            if (item.repositoryDesc.isNotBlank()) {
                RepositoryDescription(description = item.repositoryDesc)
            }

            if (item.licenseType != null && item.licenseUrl != null) {
                Spacer(modifier = Modifier.height(12.dp))
                LicenseType(licenseType = item.licenseType)
                LicenseUrl(licenseUrl = item.licenseUrl)
            }

            item.language?.let { language ->
                Spacer(modifier = Modifier.height(12.dp))
                Language(language = language)
            }

            item.topics?.let { topics ->
                Spacer(modifier = Modifier.height(12.dp))
                Topics(topics = topics)
            }

            Spacer(modifier = Modifier.height(12.dp))
            RepositoryUrl(url = item.repositoryUrl)
        }
    }
}
