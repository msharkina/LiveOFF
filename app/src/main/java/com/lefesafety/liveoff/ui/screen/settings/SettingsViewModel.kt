package com.lefesafety.liveoff.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lefesafety.liveoff.data.local.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    val nightMode: StateFlow<Boolean> = settingsDataStore.nightMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    val animationsEnabled: StateFlow<Boolean> = settingsDataStore.animationsEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    fun toggleNightMode() {
        viewModelScope.launch {
            settingsDataStore.setNightMode(!nightMode.value)
        }
    }

    fun toggleAnimations() {
        viewModelScope.launch {
            settingsDataStore.setAnimationsEnabled(!animationsEnabled.value)
        }
    }
}
