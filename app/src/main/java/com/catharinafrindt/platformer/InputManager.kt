package com.catharinafrindt.platformer

import androidx.core.math.MathUtils.clamp

open class InputManager {
    private val MIN = -1.0f
    private val MAX = 1.0f
    var _verticalFactor = 0.0f
    var _horizontalFactor = 0.0f
    var _isJumping = false

    protected open fun clampInputs() {
        _verticalFactor = clamp(_verticalFactor, MIN, MAX)
        _horizontalFactor = clamp(_horizontalFactor, MIN, MAX)
    }
    open fun onStart() {}
    open fun onStop() {}
    open fun onPause() {}
    open fun onResume() {}
    open fun update(dt: Float) {}
}