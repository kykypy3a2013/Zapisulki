package com.example.zapisulki.data.model

import java.time.LocalDate

enum class MediaCategory {
    MOVIES, SERIES, ANIME, GAMES, BOOKS
}

data class MediaItem(
    val id: Long? = null,
    val title: String,
    val category: MediaCategory,
    val note: String = "",
    val rating: Float? = null,
    val dateAdded: LocalDate = LocalDate.now(),
    val isCompleted: Boolean = false
)
// Убрали companion object с тестовыми данными