package com.catharinafrindt.platformer

const val PLAYER_RUN_SPEED = 6.0f //meters per second
val PLAYER_JUMP_FORCE: Float = -(GRAVITY / 2f) //whatever feels good!

class Player(spriteName: String, x: Float, y: Float) :
    DynamicEntity(spriteName, x, y) {

    init {
        width = 0.7f
        height = 0.7f
        bitmap = loadBitmap(spriteName)
    }

    override fun update(dt: Float) {
        val controls: InputManager = engine.getControls()
        val direction: Float = controls.horizontalFactor
        velX = direction * PLAYER_RUN_SPEED

        if (controls.isJumping && isOnGround) {
            velY = PLAYER_JUMP_FORCE
        }
        super.update(dt) //parent will integrate our velocity and time with our position
    }
}