package com.catharinafrindt.platformer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint


class Enemy(spriteName: String, x: Float, y: Float) : Entity() {
    var bitmap: Bitmap

    init {
        this.x = x
        this.y = y
        width = 1.0f
        height = 1.0f
        bitmap = engine.bitmapPool.createBitmap(spriteName, width, height)
    }

    override fun render(canvas: Canvas, transform: Matrix, paint: Paint) {
        canvas.drawBitmap(bitmap, transform, paint)
    }

}