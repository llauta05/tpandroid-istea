package com.example.tp_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        showSettingsFragment()

    }

    private fun showSettingsFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, SettingsFragment(), "SettingsFragment")
            .commit()
    }
}
