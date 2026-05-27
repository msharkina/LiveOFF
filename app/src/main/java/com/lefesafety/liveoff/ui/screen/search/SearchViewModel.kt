package com.lefesafety.liveoff.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lefesafety.liveoff.domain.model.Card
import com.lefesafety.liveoff.domain.model.Category
import com.lefesafety.liveoff.domain.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {

    val query: MutableStateFlow<String> = MutableStateFlow("")

    val selectedCategoryId: MutableStateFlow<String?> = MutableStateFlow(null)

    val categories: StateFlow<List<Category>> = contentRepository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val searchResults: StateFlow<List<Card>> = query
        .debounce(300L)
        .flatMapLatest { q ->
            if (q.isBlank()) flowOf(emptyList())
            else contentRepository.searchCards(q)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val filteredResults: StateFlow<List<Card>> = combine(
        searchResults,
        selectedCategoryId
    ) { results, categoryId ->
        if (categoryId == null) results
        else results.filter { it.categoryId == categoryId }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun onQueryChange(newQuery: String) {
        query.value = newQuery
    }

    fun onCategorySelected(categoryId: String?) {
        selectedCategoryId.value = categoryId
    }
}
