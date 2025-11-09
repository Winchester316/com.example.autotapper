package com.example.autotapper

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toggleButton = findViewById<Button>(R.id.toggleButton)
        toggleButton.setOnClickListener {
            if (!AutoTapService.isServiceEnabled) {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                Toast.makeText(this, "Enable AutoTapper Accessibility Service", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (isRunning) {
                AutoTapService.instance?.stopAutoTap()
                toggleButton.text = getString(R.string.toggle_start)
            } else {
                AutoTapService.instance?.startAutoTap()
                toggleButton.text = getString(R.string.toggle_stop)
            }
            isRunning = !isRunning
        }
    }
}
