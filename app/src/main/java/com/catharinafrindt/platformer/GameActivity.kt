package com.catharinafrindt.platformer

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class GameActivity : AppCompatActivity() {
    private val TAG = "GameActivity"
    lateinit var game : Game
    private lateinit var joystickLayout: View
    private lateinit var touchControllerLayout: View
    private lateinit var input: CompositeControl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        game = findViewById<Game>(R.id.game)
        joystickLayout = findViewById(R.id.virtual_joystick)
        touchControllerLayout = findViewById(R.id.touch_controller)

        val chooseControl = 1

        when(chooseControl) {
            1 -> { touchControllerLayout.visibility = View.GONE; input = CompositeControl(VirtualJoystick(joystickLayout)) }
            2 -> { joystickLayout.visibility = View.GONE; input = CompositeControl(TouchController(touchControllerLayout)) }
            3 -> { touchControllerLayout.visibility = View.GONE; joystickLayout.visibility = View.GONE;
                input = CompositeControl(Accelerometer(this)) }
            else -> throw IllegalArgumentException("Invalid choice.")
        }

        game.setControls(input)
    }

    override fun onResume() {
        super.onResume()
        game.onResume()
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