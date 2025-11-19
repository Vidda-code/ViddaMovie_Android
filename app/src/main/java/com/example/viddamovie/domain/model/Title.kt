package com.example.viddamovie.domain.model

data class Title(
    val id: Int?,
    val title: String?,
    val name: String?,
    val overview: String?,
    val posterPath: String?
) {

    object PreviewTitles {
        val items = listOf(
            Title(
                id = 1,
                title = "Fast And Furious",
                name = "Fast And Furious",
                overview = "A movie about Fast And Furious",
                posterPath = "https://example.com/test1.jpg"
            ),
            Title(
                id = 2,
                title = "Pulp Fiction",
                name = "Pulp Fiction",
                overview = "A movie about Pulp Fiction",
                posterPath = "https://example.com/test2.jpg"
            ),
            Title(
                id = 3,
                title = "The Dark Knight",
                name = "The Dark Knight",
                overview = "A movie about The Dark Knight",
                posterPath = "https://example.com/test3.jpg"
            )
        )
    }
}
