package com.lefesafety.liveoff.ui.components

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.lefesafety.liveoff.ui.screen.sos.FlashlightController
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

enum class MorseOutputMode { SOUND, FLASHLIGHT, VIBRATION }

@Singleton
class MorsePlayer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val flashlightController: FlashlightController
) {
    private var isPlaying = false
    private val dotDuration = 200L

    fun stop() {
        isPlaying = false
        flashlightController.turnOff()
    }

    suspend fun play(morseCode: String, mode: MorseOutputMode) {
        isPlaying = true
        for (char in morseCode) {
            if (!isPlaying) break
            when (char) {
                '.' -> {
                    signalOn(mode, dotDuration)
                    delay(dotDuration)
                    signalOff(mode)
                    delay(dotDuration)
                }
                '-' -> {
                    signalOn(mode, dotDuration * 3)
                    delay(dotDuration * 3)
                    signalOff(mode)
                    delay(dotDuration)
                }
                ' ' -> delay(dotDuration * 2)
                '/' -> delay(dotDuration * 4)
            }
        }
        isPlaying = false
        signalOff(mode)
    }

    private fun signalOn(mode: MorseOutputMode, duration: Long) {
        when (mode) {
            MorseOutputMode.FLASHLIGHT -> flashlightController.turnOn()
            MorseOutputMode.SOUND -> {
                try {
                    val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 100)
                    toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, duration.toInt())
                } catch (_: Exception) { }
            }
            MorseOutputMode.VIBRATION -> {
                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    vm.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(duration)
                }
            }
        }
    }

    private fun signalOff(mode: MorseOutputMode) {
        if (mode == MorseOutputMode.FLASHLIGHT) flashlightController.turnOff()
    }
}
