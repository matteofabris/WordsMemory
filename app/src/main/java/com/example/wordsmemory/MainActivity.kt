package com.example.wordsmemory

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.wordsmemory.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.sqrt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        requestedOrientation = if (isTablet()) {
            Log.i("Orientation", "Tablet mode")
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            Log.i("Orientation", "Phone mode")
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    @Suppress("DEPRECATION")
    private fun isTablet(): Boolean {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val yInches = metrics.heightPixels / metrics.ydpi
        val xInches = metrics.widthPixels / metrics.xdpi
        val diagonalInches = sqrt((xInches * xInches + yInches * yInches).toDouble())

        if (diagonalInches >= 7) return true
        return false
    }
}