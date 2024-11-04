package com.catharinafrindt.platformer

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF


class Enemy(spriteName: String, x: Float, y: Float) :
    DynamicEntity(spriteName, x, y) {
    var facing = LEFT

    init {
        width = 0.9f
        height = 0.9f
        engine.bitmapPool.remove(bitmap)
       // bitmap = engine.bitmapPool.createBitmap(spriteName, width, height)
    }

    fun getBound() : RectF {
        return RectF(x, y, x + width, y + height)
    }

    override fun render(canvas: Canvas, transform: Matrix, paint: Paint) {
//        transform.preScale(facing, 1.0f)
//        if (facing == RIGHT) {
//            val offset = engine.worldToScreenX(width)
//            transform.postTranslate(offset, 0.0f)
//        }
        super.render(canvas, transform, paint)
    }
}