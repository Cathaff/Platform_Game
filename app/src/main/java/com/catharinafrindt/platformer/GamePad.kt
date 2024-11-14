package com.catharinafrindt.platformer

import android.util.Log
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent

class Gamepad(val activity: MainActivity) : InputManager(), GamepadListener {

    override fun onStart() {
        super.onStart()
        activity.setGamepadListener(this)
    }

    override fun onStop() {
        activity.setGamepadListener(null)
    }

    override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
        Log.d("Gamepad", "Horizontal Axis: $horizontalFactor, Vertical Axis: $verticalFactor")
        if (event.source == InputDevice.SOURCE_JOYSTICK || event.source == InputDevice.SOURCE_GAMEPAD) {
            return false //we don't consume this event
        }
        horizontalFactor = getInputFactor(event, MotionEvent.AXIS_X, MotionEvent.AXIS_HAT_X)
        verticalFactor = getInputFactor(event, MotionEvent.AXIS_Y, MotionEvent.AXIS_HAT_Y)
        return true //we did consume this event
    }

    private fun getInputFactor(event: MotionEvent, axis: Int, fallbackAxis: Int): Float {
        val device = event.device
        val source = event.source
        var result = event.getAxisValue(axis)
        var range = device.getMotionRange(axis, source)
        if (Math.abs(result) <= range.flat) {
            result = event.getAxisValue(fallbackAxis)
            range = device.getMotionRange(fallbackAxis, source)
            if (Math.abs(result) <= range.flat) {
                result = 0.0f
            }
        }
        return result
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val action = event.action
        val keyCode = event.keyCode
        Log.d("Gamepad", "KeyCode: $keyCode, Action: $action")
        var wasConsumed = false
        if (action == MotionEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                verticalFactor -= 1.0f
                wasConsumed = true
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                verticalFactor += 1.0f
                wasConsumed = true
            }
            if (keyCode == KeyEvent.KEYCODE_A) {
                horizontalFactor -= 1.0f
                wasConsumed = true
            } else if (keyCode == KeyEvent.KEYCODE_D) {
                horizontalFactor += 1.0f
                wasConsumed = true
            }
            if (isJumpKey(keyCode)) {
                isJumping = true
                wasConsumed = true
            }
        } else if (action == MotionEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                verticalFactor += 1.0f
                wasConsumed = true
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                verticalFactor -= 1.0f
                wasConsumed = true
            }
            if (keyCode == KeyEvent.KEYCODE_A) {
                horizontalFactor += 1.0f
                wasConsumed = true
            } else if (keyCode == KeyEvent.KEYCODE_D) {
                horizontalFactor -= 1.0f
                wasConsumed = true
            }
            if (isJumpKey(keyCode)) {
                isJumping = false
                wasConsumed = true
            }
        }
        return wasConsumed
    }

    private fun isJumpKey(keyCode: Int): Boolean {
        return keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_W || keyCode == KeyEvent.KEYCODE_SPACE
    }
}