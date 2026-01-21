package com.example.zapisulki.data.repository

import com.example.zapisulki.data.model.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MediaRepository {
    // Пустой список вместо тестовых данных
    private val _mediaItems = MutableStateFlow<List<MediaItem>>(emptyList())
    val mediaItems: Flow<List<MediaItem>> = _mediaItems.asStateFlow()

    // Получить все элементы
    fun getAllItems(): Flow<List<MediaItem>> = _mediaItems

    // Получить элементы по категории
    fun getItemsByCategory(category: String): Flow<List<MediaItem>> = _mediaItems

    // Добавить новый элемент
    suspend fun addItem(item: MediaItem) {
        val newId = (_mediaItems.value.maxByOrNull { it.id ?: 0L }?.id ?: 0L) + 1L
        val newItem = item.copy(id = newId)

        _mediaItems.update { currentList ->
            currentList + newItem
        }
    }

    // Удалить элемент
    suspend fun deleteItem(itemId: Long) {
        _mediaItems.update { currentList ->
            currentList.filter { it.id != itemId }
        }
    }

    // Обновить элемент
    suspend fun updateItem(updatedItem: MediaItem) {
        _mediaItems.update { currentList ->
            currentList.map { item ->
                if (item.id == updatedItem.id) updatedItem else item
            }
        }
    }
}