package kz.gvsx.tou.ui.news

import java.time.LocalDateTime

data class News(
    val imageUrl: String,
    val title: String,
    val dateTime: LocalDateTime,
    val linkToArticle: String
)
