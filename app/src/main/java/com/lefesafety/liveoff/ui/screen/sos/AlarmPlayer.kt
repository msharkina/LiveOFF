package com.lefesafety.liveoff.ui.screen.sos

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.ToneGenerator
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var toneGenerator: ToneGenerator? = null
    private var alarmJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun startAlarm() {
        stopAlarm()
        toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME)
        alarmJob = scope.launch {
            repeat(3) { cycle ->
                if (!isActive) return@launch
                // Play 3 beeps per cycle
                repeat(3) {
                    if (!isActive) return@repeat
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 300)
                    delay(400)
                }
                // Pause between cycles (skip after the last cycle)
                if (cycle < 2 && isActive) {
                    delay(600)
                }
            }
        }
    }

    fun stopAlarm() {
        alarmJob?.cancel()
        alarmJob = null
        toneGenerator?.stopTone()
        toneGenerator?.release()
        toneGenerator = null
    }
}
