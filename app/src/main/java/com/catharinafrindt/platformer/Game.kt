package com.catharinafrindt.platformer

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.SystemClock.uptimeMillis
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random

const val STAGE_WIDTH = 1280
const val STAGE_HEIGHT = 672
var RNG = Random(uptimeMillis())
const val pixelsPerMeter = 100
lateinit var engine : Game

class Game(context: Context) : SurfaceView(context), Runnable, SurfaceHolder.Callback {
    private val tag = "Game"
    init {
        engine = this
        holder?.addCallback(this)
        holder?.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT)
    }


    private lateinit var gameThread : Thread
    @Volatile var isRunning : Boolean = false
    private val level: LevelManager = LevelManager(TestLevel())


    override fun run() {
        Log.d(tag, "run()")
        while(isRunning) {
            update()
            render()
        }
    }

    fun worldToScreenX(worldDistance : Float) = (worldDistance * pixelsPerMeter).toInt()
    fun worldToScreenY(worldDistance : Float) = (worldDistance * pixelsPerMeter).toInt()
    fun screenToWorldX(pixelDistance: Float) = pixelDistance / pixelsPerMeter
    fun screenToWorldY(pixelDistance: Float) = pixelDistance / pixelsPerMeter

    private fun render() {
        val canvas = holder?.lockCanvas() ?: return
        canvas.drawColor(Color.BLACK)
        val paint = Paint()
        level.entities.forEach { it.render(canvas, paint) }
        holder.unlockCanvasAndPost(canvas)
    }

    private fun update() {
        level.update(0.1f)
    }

    fun onPause() {
        Log.d(tag, "onPause()")
    }

    fun onResume() {
        Log.d(tag, "onResume()")
    }

    fun onDestroy() {
        Log.d(tag, "onDestroy()")
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(tag, "surfaceCreated()")
        isRunning = true
        gameThread = Thread(this)
        gameThread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d(tag, "surfaceChanged(width:$width, height:$height)")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(tag, "surfaceDestroyed()")
        isRunning = false
        gameThread.join()
    }

}