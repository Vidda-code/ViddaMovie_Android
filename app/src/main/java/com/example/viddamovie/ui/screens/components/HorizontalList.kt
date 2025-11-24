package com.example.viddamovie.ui.screens.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.viddamovie.domain.model.Title

@Composable
fun HorizontalList(
    header: String,
    titles: List<Title>,
    onTitleClick: (Title) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Header text
        Text(
            text = header,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Horizontal scrolling list
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = titles,
                key = { it.id }  // Stable key for better performance
            ) { title ->
                PosterImage(
                    posterUrl = title.posterPath,
                    contentDescription = title.displayTitle,
                    onClick = { onTitleClick(title) }
                )
            }
        }
    }
}

@Composable
private fun PosterImage(
    posterUrl: String?,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = posterUrl,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .width(120.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    )
}

@Preview(showBackground = true)
@Composable
private fun HorizontalListPreview() {
    HorizontalList(
        header = "Trending Movies",
        titles = Title.previewList,
        onTitleClick = {}
    )
}