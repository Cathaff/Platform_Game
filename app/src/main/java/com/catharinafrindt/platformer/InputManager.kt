package com.catharinafrindt.platformer

open class InputManager {
    var _verticalFactor = 0.0f
    var _horizontalFactor = 0.0f
    var _isJumping = false
    open fun onStart() {}
    open fun onStop() {}
    open fun onPause() {}
    open fun onResume() {}
    open fun update(dt: Float) {}
}