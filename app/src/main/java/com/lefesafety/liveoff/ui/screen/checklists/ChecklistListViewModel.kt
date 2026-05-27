package com.lefesafety.liveoff.ui.screen.checklists

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.lefesafety.liveoff.domain.model.Checklist
import com.lefesafety.liveoff.domain.repository.ContentRepository
import com.lefesafety.liveoff.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ChecklistListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    contentRepository: ContentRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Screen.ChecklistList>()
    val categoryId: String = route.categoryId

    val checklists: StateFlow<List<Checklist>> = contentRepository
        .getChecklistsByCategory(categoryId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}
