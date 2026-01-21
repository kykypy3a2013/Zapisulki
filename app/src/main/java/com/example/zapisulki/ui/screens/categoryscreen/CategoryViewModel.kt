package com.example.zapisulki.ui.screens.categoryscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.zapisulki.data.model.MediaCategory
import com.example.zapisulki.data.repository.MediaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CategoryViewModel(
    categoryTitle: String,
    private val repository: MediaRepository = MediaRepository()
) : ViewModel() {

    private val _categoryItems = MutableStateFlow<List<com.example.zapisulki.data.model.MediaItem>>(emptyList())
    val categoryItems: StateFlow<List<com.example.zapisulki.data.model.MediaItem>> = _categoryItems

    init {
        loadCategoryItems(categoryTitle)
    }

    private fun loadCategoryItems(categoryTitle: String) {
        viewModelScope.launch {
            // Определяем категорию по названию
            val category = when (categoryTitle) {
                "Кино" -> MediaCategory.MOVIES
                "Сериалы" -> MediaCategory.SERIES
                "Аниме" -> MediaCategory.ANIME
                "Игры" -> MediaCategory.GAMES
                "Книги" -> MediaCategory.BOOKS
                else -> MediaCategory.MOVIES
            }

            // Подписываемся на все элементы и фильтруем по категории
            repository.getAllItems().collect { allItems ->
                val filtered = allItems.filter { it.category == category }
                _categoryItems.value = filtered
            }
        }
    }

    companion object {
        fun provideFactory(categoryTitle: String) = viewModelFactory {
            initializer {
                CategoryViewModel(categoryTitle)
            }
        }
    }
}