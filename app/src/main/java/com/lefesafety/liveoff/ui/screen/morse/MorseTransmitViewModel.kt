package com.lefesafety.liveoff.ui.screen.morse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lefesafety.liveoff.domain.morse.MorseCodec
import com.lefesafety.liveoff.ui.components.MorseOutputMode
import com.lefesafety.liveoff.ui.components.MorsePlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransmitUiState(
    val text: String = "",
    val selectedMode: MorseOutputMode = MorseOutputMode.SOUND,
    val isPlaying: Boolean = false
)

@HiltViewModel
class MorseTransmitViewModel @Inject constructor(
    private val morsePlayer: MorsePlayer
) : ViewModel() {

    private val codec = MorseCodec()

    private val _uiState = MutableStateFlow(TransmitUiState())
    val uiState: StateFlow<TransmitUiState> = _uiState.asStateFlow()

    private var playJob: Job? = null

    fun onTextChange(text: String) {
        _uiState.update { it.copy(text = text) }
    }

    fun onModeChange(mode: MorseOutputMode) {
        _uiState.update { it.copy(selectedMode = mode) }
    }

    fun transmit() {
        val state = _uiState.value
        if (state.text.isBlank()) return
        val morseCode = codec.encode(state.text)
        startPlay(morseCode, state.selectedMode)
    }

    fun transmitQuick(morseCode: String) {
        val mode = _uiState.value.selectedMode
        startPlay(morseCode, mode)
    }

    fun stop() {
        playJob?.cancel()
        morsePlayer.stop()
        _uiState.update { it.copy(isPlaying = false) }
    }

    private fun startPlay(morseCode: String, mode: MorseOutputMode) {
        playJob?.cancel()
        morsePlayer.stop()
        _uiState.update { it.copy(isPlaying = true) }
        playJob = viewModelScope.launch {
            try {
                morsePlayer.play(morseCode, mode)
            } finally {
                _uiState.update { it.copy(isPlaying = false) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        morsePlayer.stop()
    }
}
