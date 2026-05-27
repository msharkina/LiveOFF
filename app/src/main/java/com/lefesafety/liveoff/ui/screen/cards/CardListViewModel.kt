package com.lefesafety.liveoff.ui.screen.cards

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.lefesafety.liveoff.domain.model.Card
import com.lefesafety.liveoff.domain.model.Category
import com.lefesafety.liveoff.domain.repository.ContentRepository
import com.lefesafety.liveoff.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val contentRepository: ContentRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Screen.CardList>()
    val categoryId: String = route.categoryId

    val cards: StateFlow<List<Card>> = contentRepository
        .getCardsByCategory(categoryId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _category = MutableStateFlow<Category?>(null)
    val category: StateFlow<Category?> = _category.asStateFlow()

    init {
        viewModelScope.launch {
            _category.value = contentRepository.getCategoryById(categoryId)
        }
    }
}
