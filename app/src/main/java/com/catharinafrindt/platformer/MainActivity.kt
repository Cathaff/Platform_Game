package com.catharinafrindt.platformer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val tag = "GameActivity"
    private lateinit var game: Game
    private var gamepadListener: GamepadListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        game = findViewById<Game>(R.id.game)
//        val input = TouchController(findViewById(R.id.touch_controller))
        val input = Gamepad(this)
        game.setControls(input)
    }

    fun setGamepadListener(listener: GamepadListener?) {
        gamepadListener = listener
    }

    override fun dispatchGenericMotionEvent(ev: MotionEvent): Boolean {
        if (gamepadListener != null) {
            if (gamepadListener!!.dispatchGenericMotionEvent(ev)) {
                return true
            }
        }
        return super.dispatchGenericMotionEvent(ev)
    }

    override fun dispatchKeyEvent(ev: KeyEvent): Boolean {
        if (gamepadListener != null) {
            if (gamepadListener!!.dispatchKeyEvent(ev)) {
                return true
            }
        }
        return super.dispatchKeyEvent(ev)
    }

    override fun onResume() {
        super.onResume()
        if (isGameControllerConnected()) {
            Toast.makeText(this, "Gamepad detected!", Toast.LENGTH_LONG).show();
        }
        game.onResume()
    }

    fun isGameControllerConnected(): Boolean {
        val deviceIds = InputDevice.getDeviceIds()
        for (deviceId in deviceIds) {
            val dev = InputDevice.getDevice(deviceId)
            val sources = dev!!.sources
            if (sources and InputDevice.SOURCE_KEYBOARD == InputDevice.SOURCE_KEYBOARD
            ) {
                return true
            }
        }
        return false
    }

    override fun onPause() {
        game.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        game.onDestroy()
        super.onDestroy()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) { //handle older SDKs, using the deprecated systemUiVisbility API
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        } else {
            // Tell the Window that our app is going to responsible for fitting for any system windows.
            // This is similar to: view.setSystemUiVisibility(LAYOUT_STABLE | LAYOUT_FULLSCREEN)
            WindowCompat.setDecorFitsSystemWindows(window, false)
            // the decor view is the true root of the Window's view hierarchy. It contains both the "decor" (i.e. the window's title (action bar) and also contains the app-supplied content view.
            //we need the root view so we can get its insetController
            val insetsController = window.decorView.windowInsetsController ?: return
            //Hide the keyboard (IME = "input method editor")
            insetsController.hide(WindowInsets.Type.ime())
            // set the modern equivalent to Sticky Immersive Mode:
            insetsController.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            // hide both the system bars (eg. the status and navigation bars):
            insetsController.hide(WindowInsets.Type.systemBars())
        }
    }

}