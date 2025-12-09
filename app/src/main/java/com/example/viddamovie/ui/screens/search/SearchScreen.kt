package com.example.viddamovie.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    Column(modifier = Modifier.fillMaxSize()) {
        SearchTopBar(
            searchQuery = searchQuery,
            isSearchingMovies = isSearchingMovies,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onToggleMediaType = viewModel::toggleMediaType
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
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
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
        trailingIcon = {
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
    ) {
        // No search suggestions
    }
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
