package com.lefesafety.liveoff.ui.screen.sos

import android.content.Context
import android.os.Vibrator
import android.os.VibratorManager
import android.os.VibrationEffect
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SosUiState(
    val isAlarmPlaying: Boolean = false,
    val isFlashlightOn: Boolean = false,
    val isQuietMode: Boolean = false
)

@HiltViewModel
class SosViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val flashlightController: FlashlightController,
    private val alarmPlayer: AlarmPlayer
) : ViewModel() {

    private val _uiState = MutableStateFlow(SosUiState())
    val uiState: StateFlow<SosUiState> = _uiState.asStateFlow()

    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun toggleAlarm() {
        val current = _uiState.value
        if (current.isQuietMode) return
        if (current.isAlarmPlaying) {
            alarmPlayer.stopAlarm()
            _uiState.update { it.copy(isAlarmPlaying = false) }
        } else {
            alarmPlayer.startAlarm()
            _uiState.update { it.copy(isAlarmPlaying = true) }
        }
    }

    fun toggleFlashlight() {
        val current = _uiState.value
        if (current.isFlashlightOn) {
            flashlightController.turnOff()
            _uiState.update { it.copy(isFlashlightOn = false) }
        } else {
            flashlightController.turnOn()
            _uiState.update { it.copy(isFlashlightOn = true) }
        }
    }

    fun enableQuietMode() {
        val current = _uiState.value
        if (!current.isQuietMode) {
            // Stop alarm if playing
            if (current.isAlarmPlaying) {
                alarmPlayer.stopAlarm()
            }
            _uiState.update { it.copy(isQuietMode = true, isAlarmPlaying = false) }
            // Start SOS vibration pattern (· · · — — — · · ·)
            startSosVibration()
        } else {
            vibrator.cancel()
            _uiState.update { it.copy(isQuietMode = false) }
        }
    }

    private fun startSosVibration() {
        // SOS pattern: 3 short, 3 long, 3 short
        // [delay, vibrate, pause, vibrate, ...] in ms
        val dot = 150L
        val dash = 450L
        val symbolGap = 150L
        val letterGap = 400L
        val repeatDelay = 1000L

        val pattern = longArrayOf(
            0,
            dot, symbolGap, dot, symbolGap, dot, // S (· · ·)
            letterGap,
            dash, symbolGap, dash, symbolGap, dash, // O (— — —)
            letterGap,
            dot, symbolGap, dot, symbolGap, dot, // S (· · ·)
            repeatDelay
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, 0)
        }
    }

    override fun onCleared() {
        super.onCleared()
        alarmPlayer.stopAlarm()
        flashlightController.turnOff()
        vibrator.cancel()
    }
}
