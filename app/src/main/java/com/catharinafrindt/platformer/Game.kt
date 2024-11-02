package com.catharinafrindt.platformer

import android.content.Context
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.os.SystemClock.uptimeMillis
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random

//const val pixelsPerMeter = 50
var RNG = Random(uptimeMillis())
lateinit var engine : Game
val NANOS_TO_SECOND = 1.0f / 1000000000.0f

class Game(context: Context) : SurfaceView(context), Runnable, SurfaceHolder.Callback {
    private val tag = "Game"
    init {
        engine = this
        holder?.addCallback(this)
        holder?.setFixedSize(screenWidth(), screenHeight())
    }


    private lateinit var gameThread : Thread
    @Volatile var isRunning : Boolean = false

    private val camera = Viewport(screenWidth(), screenHeight(), 0.0f, 8.0f)
    private val level: LevelManager = LevelManager(TestLevel())


    override fun run() {
        Log.d(tag, "run()")
        var lastFrame = System.nanoTime()
        while(isRunning) {
            val dt = (System.nanoTime() - lastFrame) * NANOS_TO_SECOND
            lastFrame = System.nanoTime()
            // handle input
            update(dt)
            render()
        }
    }

    fun worldToScreenX(worldDistance : Float) = camera.worldToScreenX(worldDistance)
    fun worldToScreenY(worldDistance : Float) = camera.worldToScreenY(worldDistance)
    fun screenHeight() = context.resources.displayMetrics.heightPixels
    fun screenWidth() = context.resources.displayMetrics.widthPixels
    fun levelHeight() = level.levelHeight

    private fun render() {
        val canvas = holder?.lockCanvas() ?: return
        canvas.drawColor(Color.BLACK)
        val paint = Paint()
        var transform = Matrix()
        var position: PointF
        val visible = buildVisibleSet()

        visible.forEach {
            transform.reset()
            position = camera.worldToScreen(it)
            transform.postTranslate(position.x, position.y)
            it.render(canvas, transform, paint)
        }
        holder.unlockCanvasAndPost(canvas)
    }

    private fun buildVisibleSet() : List<Entity> {
        return level.entities.filter { camera.inView(it) }
    }

    private fun update(dt: Float) {
        level.update(dt)
        camera.lookAt(level.player)
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
        Log.d(tag, "screen width${screenWidth()}, height:${screenHeight()}")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(tag, "surfaceDestroyed()")
        isRunning = false
        gameThread.join()
    }

}