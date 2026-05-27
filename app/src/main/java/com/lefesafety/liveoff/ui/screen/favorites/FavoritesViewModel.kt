package com.lefesafety.liveoff.ui.screen.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lefesafety.liveoff.domain.model.ContentType
import com.lefesafety.liveoff.domain.model.Favorite
import com.lefesafety.liveoff.domain.model.UserNote
import com.lefesafety.liveoff.domain.repository.ContentRepository
import com.lefesafety.liveoff.domain.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoriteItem(
    val contentId: String,
    val contentType: ContentType,
    val title: String,
    val addedAt: Long
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val contentRepository: ContentRepository
) : ViewModel() {

    val selectedTab: MutableStateFlow<Int> = MutableStateFlow(0)

    private val favorites: StateFlow<List<Favorite>> = userDataRepository.getAllFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val titleMap: StateFlow<Map<String, String>> = combine(
        contentRepository.getAllCards(),
        contentRepository.getAllChecklists()
    ) { cards, checklists ->
        val map = mutableMapOf<String, String>()
        cards.forEach { card -> map[card.id] = card.title }
        checklists.forEach { checklist -> map[checklist.id] = checklist.title }
        map
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyMap()
    )

    val favoriteItems: StateFlow<List<FavoriteItem>> = combine(
        favorites,
        titleMap
    ) { favList, titles ->
        favList.map { fav ->
            FavoriteItem(
                contentId = fav.contentId,
                contentType = fav.contentType,
                title = titles[fav.contentId] ?: fav.contentId,
                addedAt = fav.addedAt
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    val notes: StateFlow<List<UserNote>> = userDataRepository.getAllNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun selectTab(index: Int) {
        selectedTab.value = index
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            userDataRepository.deleteNote(id)
        }
    }
}
