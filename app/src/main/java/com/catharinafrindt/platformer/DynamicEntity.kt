package com.catharinafrindt.platformer

import androidx.core.math.MathUtils.clamp

private const val MAX_DELTA = 0.49f
const val GRAVITY = 40f

open class DynamicEntity(spriteName: String, x: Float, y: Float) : StaticEntity(spriteName, x, y) {
    var velX = 0f
    var velY = 0f
    var isOnGround = false

    override fun update(dt: Float) {
        super.update(dt)
        moveHorizontally(dt)
        moveVertically(dt)
        isOnGround = false
    }

    override fun onCollision(that: Entity) {
        var overlap = getOverlap(this, that)
        if (overlap == null) {
            return
        }
        if (overlap.x != 0.0f) {
            x += overlap.x
            velX = 0f
        }
        if (overlap.y != 0.0f) { // either hit head or feet
            y += overlap.y
            velY = 0f
            isOnGround = (overlap.y < 0)
        }
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