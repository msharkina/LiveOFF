package com.lefesafety.liveoff.ui.screen.cards

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.lefesafety.liveoff.domain.model.Card
import com.lefesafety.liveoff.domain.model.Category
import com.lefesafety.liveoff.domain.model.ContentType
import com.lefesafety.liveoff.domain.repository.ContentRepository
import com.lefesafety.liveoff.domain.repository.UserDataRepository
import com.lefesafety.liveoff.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val contentRepository: ContentRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Screen.CardDetail>()
    val cardId: String = route.cardId

    val card: StateFlow<Card?> = contentRepository
        .getCardById(cardId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val _category = MutableStateFlow<Category?>(null)
    val category: StateFlow<Category?> = _category.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _isRead = MutableStateFlow(false)
    val isRead: StateFlow<Boolean> = _isRead.asStateFlow()

    init {
        viewModelScope.launch {
            _isFavorite.value = userDataRepository.isFavorite(cardId, ContentType.CARD)
            _isRead.value = userDataRepository.isCardRead(cardId)
        }
        viewModelScope.launch {
            card.collect { c ->
                if (c != null) {
                    _category.value = contentRepository.getCategoryById(c.categoryId)
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            userDataRepository.toggleFavorite(cardId, ContentType.CARD)
            _isFavorite.value = userDataRepository.isFavorite(cardId, ContentType.CARD)
        }
    }

    fun markAsRead() {
        viewModelScope.launch {
            userDataRepository.markCardAsRead(cardId)
            _isRead.value = true
        }
    }
}
