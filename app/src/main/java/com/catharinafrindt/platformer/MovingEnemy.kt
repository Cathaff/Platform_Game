package com.catharinafrindt.platformer

class MovingEnemy(spriteName: String, x: Float, y: Float) : DynamicEntity(spriteName, x, y) {
    private val startY = y
    private var movingUp = true
    private val ENEMY_SPEED = 1.5f
    private val PATROL_RANGE = 2f

    override fun update(dt: Float) {
        if (movingUp) {
            y -= ENEMY_SPEED * dt
            if (y < startY - PATROL_RANGE) {
                movingUp = false
            }
        } else {
            y += ENEMY_SPEED * dt
            if (y > startY + PATROL_RANGE) {
                movingUp = true
            }
        }
    }
}