package com.example.autotapper

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent

class AutoTapService : AccessibilityService() {

    private val handler = Handler(Looper.getMainLooper())
    private var running = false

    private val tapX = 500f
    private val tapY = 1500f
    private val intervalMs = 100L  // 100 ms between taps

    private val tapRunnable = object : Runnable {
        override fun run() {
            if (!running) return
            performTap(tapX, tapY)
            handler.postDelayed(this, intervalMs)
        }
    }

    override fun onServiceConnected() {
        instance = this
        isServiceEnabled = true
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        isServiceEnabled = false
        instance = null
    }

    fun startAutoTap() {
        if (running) return
        running = true
        handler.post(tapRunnable)
    }

    fun stopAutoTap() {
        running = false
        handler.removeCallbacks(tapRunnable)
    }

    private fun performTap(x: Float, y: Float) {
        val path = Path().apply { moveTo(x, y) }
        val stroke = GestureDescription.StrokeDescription(path, 0, 1)
        val gesture = GestureDescription.Builder().addStroke(stroke).build()
        dispatchGesture(gesture, null, null)
    }

    companion object {
        var instance: AutoTapService? = null
        var isServiceEnabled: Boolean = false
    }
}
