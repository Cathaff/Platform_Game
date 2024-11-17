package com.catharinafrindt.platformer

import java.util.ArrayList

class CompositeControl(vararg inputs: InputManager) : InputManager() {
    private val _inputs = ArrayList<InputManager>()

    init {
        for (im in inputs) {
            _inputs.add(im)
        }
    }

    fun addInput(im: InputManager) {
        _inputs.add(im)
    }

    fun setInput(im: InputManager) {
        onPause()
        onStop()
        _inputs.clear()
        addInput(im)
    }

    override fun update(dt: Float) {
        _isJumping = false
        _horizontalFactor = 0f
        _verticalFactor = 0f
        for (im in _inputs) {
            im.update(dt)
            _isJumping = _isJumping || im._isJumping
            _horizontalFactor += im._horizontalFactor
            _verticalFactor += im._verticalFactor
        }
        clampInputs()
    }

    private fun clampInputs() {
        _horizontalFactor = _horizontalFactor.coerceIn(-1.0f, 1.0f)
        _verticalFactor = _verticalFactor.coerceIn(-1.0f, 1.0f)
    }


    override fun onStart() {
        for (im in _inputs) {
            im.onStart()
        }
    }

    override fun onStop() {
        for (im in _inputs) {
            im.onStop()
        }
    }

    override fun onPause() {
        for (im in _inputs) {
            im.onPause()
        }
    }

    override fun onResume() {
        for (im in _inputs) {
            im.onResume()
        }
    }
}