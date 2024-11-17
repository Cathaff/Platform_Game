package com.catharinafrindt.platformer

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint

const val PLAYER_RUN_SPEED = 6.0f //meters per second
val PLAYER_JUMP_FORCE: Float = -(GRAVITY / 2f) //whatever feels good!
const val LEFT = 1.0f
const val RIGHT = -1.0f

class Player(spriteName: String, x: Float, y: Float) :
    DynamicEntity(spriteName, x, y) {
    var facing = LEFT

    init {
        width = 0.9f
        height = 0.9f
        engine.bitmapPool.remove(bitmap)
        bitmap = engine.bitmapPool.createBitmap(spriteName, width, height)
    }

    override fun update(dt: Float) {
        val controls: InputManager = engine.getControls()
        val direction: Float = controls._horizontalFactor
        velX = direction * PLAYER_RUN_SPEED
        facing = getFacingDirection(direction)
        if (controls._isJumping && isOnGround) {
            velY = PLAYER_JUMP_FORCE
        }
        super.update(dt) //parent will integrate our velocity and time with our position
    }

    private fun getFacingDirection(direction: Float): Float {
        if (direction < 0.0f) {
            return LEFT
        } else if (direction > 0.0f) {
            return RIGHT
        }
        return facing
    }

    override fun render(canvas: Canvas, transform: Matrix, paint: Paint) {
        transform.preScale(facing, 1.0f)
        if (facing == RIGHT) {
            val offset = engine.worldToScreenX(width)
            transform.postTranslate(offset, 0.0f)
        }
        super.render(canvas, transform, paint)
    }
}