package com.catharinafrindt.platformer

import android.content.res.Resources
import android.util.Log
import android.view.MotionEvent
import android.view.View

class VirtualJoystick(view: View) : InputManager() {
    private val TAG = "VirtualJoystick"
    protected var maxStickTravel = dpToPixels(48 * 2).toFloat() //arbritrary! 96DP = twice the minimum hit target.
    protected var stickCenterX = 0f //the stick center is placed wherever the user put their finger down
    protected var stickCenterY = 0f //we can then calculate how far they've slide the stick from that position

    private val joystickRegion: View
    private val joystickStick: View
    private val buttonRegion: View

    init {
        joystickRegion = view.findViewById<View>(R.id.joystick_region)
        joystickStick = view.findViewById<View>(R.id.joystick_stick)
        buttonRegion = view.findViewById<View>(R.id.button_region)

        joystickRegion.setOnTouchListener(JoystickTouchListener())
        buttonRegion.setOnTouchListener(ActionButtonTouchListener())

        Log.d(TAG, "Max joystickStick travel (pixels): $maxStickTravel")
    }

    private inner class ActionButtonTouchListener : View.OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            val action = event.actionMasked
            if (action == MotionEvent.ACTION_DOWN) {
                _isJumping = true
            } else if (action == MotionEvent.ACTION_UP) {
                _isJumping = false
            }
            return true
        }
    }


    private inner class JoystickTouchListener : View.OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            val action = event.actionMasked
            if (action == MotionEvent.ACTION_DOWN) {
                stickCenterX = event.getX(0)
                stickCenterY = event.getY(0)
                updateStickPosition(stickCenterX, stickCenterY)
            } else if (action == MotionEvent.ACTION_UP) {
                _horizontalFactor = 0.0f
                _verticalFactor = 0.0f
                returnStickToCenter()
            } else if (action == MotionEvent.ACTION_MOVE) {
                //get the proportion to the maxStickTravel
                _horizontalFactor = (event.getX(0) - stickCenterX) / maxStickTravel
                _verticalFactor = (event.getY(0) - stickCenterY) / maxStickTravel
                clampInputs() //nothing stops the user from pulling the joystickStick across the entire screen. So lets clamp the inputs. :)

                val clampedX =stickCenterX + (_horizontalFactor * maxStickTravel)
                val clampedY = stickCenterY + (_verticalFactor * maxStickTravel)

                updateStickPosition(clampedX, clampedY)
            }
            return true
        }
    }

    private fun updateStickPosition(x: Float, y: Float) {
        val regionCenterX = (joystickRegion.width / 2).toFloat()
        val regionCenterY = (joystickRegion.height / 2).toFloat()

        val translationX = x - regionCenterX - (joystickStick.width / 2)
        val translationY = y - regionCenterY - (joystickStick.height / 2)

        joystickStick.translationX = translationX
        joystickStick.translationY = translationY
    }

    private fun returnStickToCenter() {
        joystickStick.animate()
            .translationX(0f)
            .translationY(0f)
            .setDuration(100)
            .start()
    }

    fun pixelsToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    fun dpToPixels(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}