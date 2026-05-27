package com.lefesafety.liveoff.ui.screen.checklists

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.lefesafety.liveoff.domain.model.Checklist
import com.lefesafety.liveoff.domain.repository.ContentRepository
import com.lefesafety.liveoff.domain.repository.UserDataRepository
import com.lefesafety.liveoff.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChecklistDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    contentRepository: ContentRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Screen.ChecklistDetail>()
    val checklistId: String = route.checklistId

    val checklist: StateFlow<Checklist?> = contentRepository
        .getChecklistById(checklistId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val progress: StateFlow<Map<Int, Boolean>> = userDataRepository
        .getChecklistProgress(checklistId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyMap()
        )

    fun toggleItem(index: Int, isChecked: Boolean) {
        viewModelScope.launch {
            userDataRepository.setChecklistItemChecked(checklistId, index, isChecked)
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            userDataRepository.resetChecklistProgress(checklistId)
        }
    }
}
