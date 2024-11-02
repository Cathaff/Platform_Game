package com.catharinafrindt.platformer

import androidx.core.math.MathUtils.clamp

private const val MAX_DELTA = 0.49f
const val GRAVITY = 40f

class DynamicEntity(spriteName: String, x: Float, y: Float) : StaticEntity(spriteName, x, y) {
    var velX = 0f
    var velY = 0f

    override fun update(dt: Float) {
        super.update(dt)
        moveHorizontally(dt)
        moveVertically(dt)

    }

    private fun moveVertically(dt: Float) {
        velY += GRAVITY * dt
        val deltaY = clamp(velY * dt, -MAX_DELTA, MAX_DELTA)
        y += deltaY
        if (top > engine.levelHeight()) {
            bottom = 0f
        }
    }

    private fun moveHorizontally(dt: Float) {
        val deltaX = clamp(velX * dt, -MAX_DELTA, MAX_DELTA)
        x += deltaX
    }
}