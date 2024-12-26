package com.catharinafrindt.platformer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log

class Coin (spriteName: String, x: Float, y: Float) : Entity(){
    var bitmap : Bitmap
    private var removeCoin = false

    init {
        this.x = x
        this.y = y
        width = 0.5f
        height = 0.5f
        bitmap = engine.bitmapPool.createBitmap(spriteName, width, height)
    }

    override fun onCollision(ent: Entity) {
        super.onCollision(ent)
        if(ent is Player)
        {
            removeCoin = true
        }
    }

    override fun render(canvas: Canvas, transform: Matrix, paint: Paint)
    {
        if(!removeCoin)
        {
            canvas.drawBitmap(bitmap, transform, paint)
        }
    }
    
}