package com.lefesafety.liveoff.ui.screen.checklists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lefesafety.liveoff.domain.model.Category
import com.lefesafety.liveoff.domain.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ChecklistCategoriesViewModel @Inject constructor(
    contentRepository: ContentRepository
) : ViewModel() {

    val categories: StateFlow<List<Category>> = combine(
        contentRepository.getAllCategories(),
        contentRepository.getAllChecklists()
    ) { allCategories, allChecklists ->
        val categoryIdsWithChecklists = allChecklists.map { it.categoryId }.toSet()
        allCategories.filter { it.id in categoryIdsWithChecklists }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )
}
