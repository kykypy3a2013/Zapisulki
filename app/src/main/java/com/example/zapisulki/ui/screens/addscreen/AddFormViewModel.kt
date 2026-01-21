package com.example.zapisulki.ui.screens.addscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AddFormState(
    val title: String = "",
    val category: String = "Кино",
    val note: String = ""
)

class AddFormViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddFormState())
    val uiState: StateFlow<AddFormState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateCategory(category: String) {
        _uiState.value = _uiState.value.copy(category = category)
    }

    fun updateNote(note: String) {
        _uiState.value = _uiState.value.copy(note = note)
    }

    // TODO: Метод saveItem будет реализован позже
}