package com.catharinafrindt.platformer

open class InputManager {
    var verticalFactor = 0.0f
    var horizontalFactor = 0.0f
    var isJumping = false
    open fun onStart() {}
    open fun onStop() {}
    fun onPause() {}
    fun onResume() {}
}