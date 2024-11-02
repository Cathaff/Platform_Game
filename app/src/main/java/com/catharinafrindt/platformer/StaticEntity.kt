package com.catharinafrindt.platformer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import kotlin.math.ceil

open class StaticEntity(spriteName: String, x: Float, y: Float) : Entity() {
    val bitmap : Bitmap

    init {
        this.x = x
        this.y = y
        width = 1.0f
        height = 1.0f
        val widthInPixels = ceil(engine. worldToScreenX(width))
        val heightInPixels = ceil(engine.worldToScreenY(height))
        bitmap = BitmapUtils.loadScaledBitmap(engine.context,
            spriteName,
            widthInPixels.toInt(),
            heightInPixels.toInt())
    }

    override fun render(canvas: Canvas, transform: Matrix, paint: Paint) {
        canvas.drawBitmap(bitmap, transform, paint)
    }
}
