package com.catharinafrindt.platformer

import android.graphics.PointF
import kotlin.math.round

class Viewport(
    val screenWidth: Int,
    val screenHeight: Int,
    metersToShowX: Float,
    metersToShowY: Float
) : Entity() {

    private var pixelsPerMeterX: Float = 0.0f
    private var pixelsPerMeterY: Float = 0.0f
    private val screenCenterX = screenWidth / 2
    private val screenCenterY = screenHeight / 2

    init {
        require(metersToShowX > 0f || metersToShowY > 0f) { "One of the dimensions must be provided!" }
        setMetersToShow(metersToShowX, metersToShowY)
        lookAt(2f, 0f)
    }

    private fun setMetersToShow(metersToShowX: Float, metersToShowY: Float) {
        width = metersToShowX.takeIf { it > 0f } ?: (screenWidth.toFloat() / screenHeight * metersToShowY)
        height = metersToShowY.takeIf { it > 0f } ?: (screenHeight.toFloat() / screenWidth * metersToShowX)

        pixelsPerMeterX = screenWidth / width
        pixelsPerMeterY = screenHeight / height
    }

    // Centers the viewport on the specified world position or entity
    fun lookAt(x: Float, y: Float) {
        centerX = x
        centerY = y
    }
    fun lookAt(e: Entity) = lookAt(e.centerX, e.centerY)
    fun lookAt(pos: PointF) = lookAt(pos.x, pos.y)

    // Converts world coordinates to screen coordinates and returns the screen X value
    fun worldToScreenX(worldDistance: Float): Float = worldDistance * pixelsPerMeterX

    // Converts world coordinates to screen coordinates and returns the screen Y value
    fun worldToScreenY(worldDistance: Float): Float = worldDistance * pixelsPerMeterY

    // Converts world position (x, y) to screen position and returns a new PointF
    fun worldToScreen(worldPosX: Float, worldPosY: Float): PointF {
        val screenX = screenCenterX - (centerX - worldPosX) * pixelsPerMeterX
        val screenY = screenCenterY - (centerY - worldPosY) * pixelsPerMeterY
        return PointF(round(screenX), round(screenY))
    }

    // Overloads for convenience
    fun worldToScreen(worldPos: PointF): PointF = worldToScreen(worldPos.x, worldPos.y)
    fun worldToScreen(e: Entity): PointF = worldToScreen(e.x, e.y)

    // Checks if the entity is within the view
    fun inView(e: Entity) = isColliding(this, e)

    override fun toString() = "Viewport [$screenWidth px, $screenHeight px / $width m, $height m]"
}