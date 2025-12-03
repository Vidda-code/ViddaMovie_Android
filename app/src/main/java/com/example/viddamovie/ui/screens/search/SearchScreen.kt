package com.example.viddamovie.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.viddamovie.domain.model.Title
import com.example.viddamovie.ui.screens.components.ErrorMessage
import com.example.viddamovie.ui.screens.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isSearchingMovies by viewModel.isSearchingMovies.collectAsState()

    Scaffold(
        topBar = {
            SearchTopBar(
                searchQuery = searchQuery,
                isSearchingMovies = isSearchingMovies,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onToggleMediaType = viewModel::toggleMediaType
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    LoadingIndicator()
                }

                errorMessage != null -> {
                    ErrorMessage(message = errorMessage ?: "Unknown error")
                }

                else -> {
                    SearchResultsGrid(
                        titles = searchResults,
                        onTitleClick = { title ->
                            navController.navigate("detail/${title.id}/${title.mediaType}")
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopBar(
    searchQuery: String,
    isSearchingMovies: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onToggleMediaType: () -> Unit
) {
    TopAppBar(
        title = {
            SearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                onSearch = { /* Handled by debouncing */ },
                active = false,
                onActiveChange = { },
                placeholder = {
                    Text(
                        if (isSearchingMovies) {
                            "Search for a Movie"
                        } else {
                            "Search for a TV Show"
                        }
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                // No search suggestions
            }
        },
        actions = {
            IconButton(onClick = onToggleMediaType) {
                Icon(
                    imageVector = if (isSearchingMovies) {
                        Icons.Default.Movie
                    } else {
                        Icons.Default.Tv
                    },
                    contentDescription = "Toggle media type"
                )
            }
        }
    )
}

@Composable
private fun SearchResultsGrid(
    titles: List<Title>,
    onTitleClick: (Title) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = titles,
            key = { it.id }
        ) { title ->
            SearchResultItem(
                title = title,
                onClick = { onTitleClick(title) }
            )
        }
    }
}

@Composable
private fun SearchResultItem(
    title: Title,
    onClick: () -> Unit
) {
    AsyncImage(
        model = title.posterPath,
        contentDescription = title.displayTitle,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f / 3f)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    )
}