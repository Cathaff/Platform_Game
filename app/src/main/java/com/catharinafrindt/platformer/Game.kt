package com.catharinafrindt.platformer

import android.content.Context
import android.graphics.Color
import android.os.SystemClock.uptimeMillis
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random

const val STAGE_WIDTH = 1280
const val STAGE_HEIGHT = 672
var RNG = Random(uptimeMillis())

class Game(context: Context) : SurfaceView(context), Runnable, SurfaceHolder.Callback {
    private val tag = "Game"
    private lateinit var gameThread : Thread
    @Volatile var isRunning : Boolean = false

    init {
        holder?.addCallback(this)
        holder?.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT)
    }


    override fun run() {
        Log.d(tag, "run()")
        while(isRunning) {
            update()
            render()
        }
    }

    private fun render() {
        val canvas = holder?.lockCanvas() ?: return
        canvas.drawColor(Color.BLACK)
        holder.unlockCanvasAndPost(canvas)
    }

    private fun update() {

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