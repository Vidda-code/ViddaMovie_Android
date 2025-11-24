package com.example.viddamovie.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.viddamovie.domain.model.Title

@Composable
fun VerticalList(
    titles: List<Title>,
    onTitleClick: (Title) -> Unit,
    onDeleteClick: ((Title) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = titles,
            key = { it.id }
        ) { title ->
            if (onDeleteClick != null) {
                // Swipeable item with delete
                SwipeableListItem(
                    title = title,
                    onClick = { onTitleClick(title) },
                    onDelete = { onDeleteClick(title) }
                )
            } else {
                // Regular clickable item
                TitleListItem(
                    title = title,
                    onClick = { onTitleClick(title) }
                )
            }
        }
    }
}

@Composable
private fun TitleListItem(
    title: Title,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Poster image
        AsyncImage(
            model = title.posterPath,
            contentDescription = title.displayTitle,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(80.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        // Title text
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title.displayTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            title.releaseDate?.let { date ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableListItem(
    title: Title,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            // Red background with delete icon when swiping
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        enableDismissFromStartToEnd = false,  // Only swipe left-to-right
        modifier = modifier
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface
        ) {
            TitleListItem(
                title = title,
                onClick = onClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VerticalListPreview() {
    VerticalList(
        titles = Title.previewList,
        onTitleClick = {},
        onDeleteClick = {}
    )
}