package com.catharinafrindt.platformer

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.Button

@SuppressLint("ClickableViewAccessibility")
class TouchController(view: View) : InputManager(), View.OnTouchListener {

    init {
        // Set touch listener for gamepad buttons
        listOf(
            R.id.gamepad_up,
            R.id.gamepad_down,
            R.id.gamepad_left,
            R.id.gamepad_right,
            R.id.gamepad_jump
        )
            .forEach { buttonId ->
                view.findViewById<Button>(buttonId).setOnTouchListener(this)
            }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val action = event.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN -> handleActionDown(v.id)
            MotionEvent.ACTION_UP -> handleActionUp(v.id)
        }
        return false
    }

    private fun handleActionDown(buttonId: Int) {
        when (buttonId) {
            R.id.gamepad_up -> verticalFactor -= 1
            R.id.gamepad_down -> verticalFactor += 1
            R.id.gamepad_left -> horizontalFactor -= 1
            R.id.gamepad_right -> horizontalFactor += 1
            R.id.gamepad_jump -> isJumping = true
        }
    }

    private fun handleActionUp(buttonId: Int) {
        when (buttonId) {
            R.id.gamepad_up -> verticalFactor += 1
            R.id.gamepad_down -> verticalFactor -= 1
            R.id.gamepad_left -> horizontalFactor += 1
            R.id.gamepad_right -> horizontalFactor -= 1
            R.id.gamepad_jump -> isJumping = false
        }
    }
}