package com.example.zapisulki.ui.screens.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zapisulki.data.repository.MediaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: MediaRepository = MediaRepository()
) : ViewModel() {

    // Состояние UI - теперь просто пустой поток
    val allItems = repository.getAllItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Действия
    fun addItem(item: com.example.zapisulki.data.model.MediaItem) {
        viewModelScope.launch {
            repository.addItem(item)
        }
    }

    fun deleteItem(itemId: Long) {
        viewModelScope.launch {
            repository.deleteItem(itemId)
        }
    }

    fun updateItem(item: com.example.zapisulki.data.model.MediaItem) {
        viewModelScope.launch {
            repository.updateItem(item)
        }
    }
}