package com.lefesafety.liveoff.ui.screen.sos

import android.content.Context
import android.hardware.camera2.CameraManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlashlightController @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val cameraId: String? = cameraManager.cameraIdList.firstOrNull()

    fun turnOn() { cameraId?.let { cameraManager.setTorchMode(it, true) } }
    fun turnOff() { cameraId?.let { cameraManager.setTorchMode(it, false) } }
}
