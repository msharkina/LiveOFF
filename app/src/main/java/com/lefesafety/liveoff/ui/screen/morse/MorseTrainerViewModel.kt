package com.lefesafety.liveoff.ui.screen.morse

import androidx.lifecycle.ViewModel
import com.lefesafety.liveoff.domain.morse.MorseCodec
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class TrainerUiState(
    val currentLetter: Char = 'А',
    val currentMorse: String = ".-",
    val userInput: String = "",
    val checkResult: CheckResult = CheckResult.NONE,
    val correctCount: Int = 0,
    val totalCount: Int = 0
)

enum class CheckResult { NONE, CORRECT, WRONG }

@HiltViewModel
class MorseTrainerViewModel @Inject constructor() : ViewModel() {

    private val codec = MorseCodec()
    private val alphabet = codec.getAlphabet()

    private val _uiState = MutableStateFlow(randomLetterState())
    val uiState: StateFlow<TrainerUiState> = _uiState.asStateFlow()

    fun appendDot() {
        if (_uiState.value.checkResult == CheckResult.NONE) {
            _uiState.update { it.copy(userInput = it.userInput + ".") }
        }
    }

    fun appendDash() {
        if (_uiState.value.checkResult == CheckResult.NONE) {
            _uiState.update { it.copy(userInput = it.userInput + "-") }
        }
    }

    fun checkAnswer() {
        val state = _uiState.value
        if (state.userInput.isEmpty()) return
        val isCorrect = state.userInput.trim() == state.currentMorse.trim()
        _uiState.update {
            it.copy(
                checkResult = if (isCorrect) CheckResult.CORRECT else CheckResult.WRONG,
                correctCount = if (isCorrect) it.correctCount + 1 else it.correctCount,
                totalCount = it.totalCount + 1
            )
        }
    }

    fun nextLetter() {
        _uiState.value = randomLetterState(
            correctCount = _uiState.value.correctCount,
            totalCount = _uiState.value.totalCount
        )
    }

    private fun randomLetterState(correctCount: Int = 0, totalCount: Int = 0): TrainerUiState {
        val (letter, morse) = alphabet.random()
        return TrainerUiState(
            currentLetter = letter,
            currentMorse = morse,
            userInput = "",
            checkResult = CheckResult.NONE,
            correctCount = correctCount,
            totalCount = totalCount
        )
    }
}
