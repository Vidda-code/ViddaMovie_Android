package com.example.viddamovie.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HorizontalList(
    modifier: Modifier = Modifier,
    header: String,
    title: List<String>,
    onClick: (String) -> Unit
) {
    Column {
        Text(style = MaterialTheme.typography.titleLarge, text = header)
        LazyRow() {
            items(title.size) {}
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HorizontalListPreview() {
    HorizontalList(
        header = "Header",
        title = listOf("Title 1", "Title 2", "Title 3"),
        onClick = {}
    )
}