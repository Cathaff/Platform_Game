package com.catharinafrindt.platformer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log


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

    fun getBound() : RectF {
        return RectF(x, y, x + width, y + height)
    }

    override fun onCollision(that: Entity) {
        super.onCollision(that)
        Log.d("tag", "once")

    }

//    override fun render(canvas: Canvas, transform: Matrix, paint: Paint) {
//        transform.preScale(facing, 1.0f)
//        if (facing == RIGHT) {
//            val offset = engine.worldToScreenX(width)
//            transform.postTranslate(offset, 0.0f)
//        }
//        super.render(canvas, transform, paint)
//    }
}