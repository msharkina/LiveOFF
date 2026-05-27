package com.lefesafety.liveoff.ui.screen.morse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lefesafety.liveoff.domain.morse.MorseCodec
import com.lefesafety.liveoff.ui.components.MorseOutputMode
import com.lefesafety.liveoff.ui.components.MorsePlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MorseAlphabetViewModel @Inject constructor(
    private val morsePlayer: MorsePlayer
) : ViewModel() {

    private val codec = MorseCodec()

    val alphabet: List<Pair<Char, String>> = codec.getAlphabet()

    fun playMorse(morseCode: String) {
        viewModelScope.launch {
            morsePlayer.stop()
            morsePlayer.play(morseCode, MorseOutputMode.SOUND)
        }
    }

    override fun onCleared() {
        super.onCleared()
        morsePlayer.stop()
    }
}
