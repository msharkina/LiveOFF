package com.lefesafety.liveoff.ui.screen.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lefesafety.liveoff.domain.model.Category
import com.lefesafety.liveoff.domain.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CardCategoriesViewModel @Inject constructor(
    contentRepository: ContentRepository
) : ViewModel() {

    val categories: StateFlow<List<Category>> = contentRepository
        .getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}
