package com.catharinafrindt.platformer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

class StaticEntity(spriteName: String, x: Float, y: Float) : Entity() {
    val bitmap : Bitmap

    init {
        this.x = x
        this.y = y
        width = 1.0f
        height = 1.0f
        val widthInPixels = engine.worldToScreenX(width)
        val heightInPixels = engine.worldToScreenY(height)
        bitmap = BitmapUtils.loadScaledBitmap(engine.context,
            spriteName,
            widthInPixels,
            heightInPixels)
    }

    override fun render(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(bitmap,
            engine.worldToScreenX(x).toFloat(),
            engine.worldToScreenY(y).toFloat(),
            paint)
    }
}
