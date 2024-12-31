package com.catharinafrindt.platformer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

const val STAGE_WIDTH = 1280
const val STAGE_HEIGHT = 672

class StatusRender(private val heartBitmap: Bitmap, private val coinBitmap: Bitmap) : HUD
{
     override fun renderHUD(canvas: Canvas, paint: Paint, level: LevelManager, isGameOver: Boolean) {
        if(!isGameOver)
        {
            setHearts(canvas, paint, level)
        }
        else
        {
            val centerX = STAGE_WIDTH / 2.0f
            val centerY = STAGE_HEIGHT / 2.0f
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 70f
            canvas.drawText("GAME OVER", centerX, centerY, paint)
            paint.textSize = 40f
            canvas.drawText("press to restart", centerX, centerY+50f, paint)
        }
    }

    private fun setHearts(canvas: Canvas, paint: Paint, level: LevelManager) {
        val heartLeft = 10f
        val heartSpacing = 2f
        val health = level.playerHealth
        for (i in 0 until health) {
            canvas.drawBitmap(
                heartBitmap,
                heartLeft + i * (heartBitmap.width + heartSpacing),
                10f,
                paint
            )
        }
        coinStatus(heartLeft, heartSpacing, canvas, paint, level)
    }

    private fun coinStatus(heartLeft: Float, heartSpacing: Float, canvas: Canvas, paint: Paint, level: LevelManager) {
        paint.color = Color.BLACK
        paint.textSize = 30f
        val health = level.playerHealth

        val posFromHeartX = heartLeft + (health * (heartBitmap.width + heartSpacing)) + 30f
        val posFromHeartY = heartBitmap.height / 2f

        canvas.drawBitmap(
            coinBitmap,
            posFromHeartX,
            posFromHeartY,
            paint
        )

        val textX = posFromHeartX + (coinBitmap.width + heartSpacing)
        val textY = 25f + posFromHeartY / 2f
        canvas.drawText("X ${level.collectedCoins}", textX, textY, paint)

        val rowSpacing = heartBitmap.height + 20f
        val rowUnderHeartX = heartLeft
        val rowUnderHeartY = 30f + rowSpacing
        canvas.drawText("Total Coins: ${level.totalCoins}", rowUnderHeartX, rowUnderHeartY, paint)
    }
}