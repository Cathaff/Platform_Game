package com.catharinafrindt.platformer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.os.SystemClock.uptimeMillis
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random

var RNG = Random(uptimeMillis())
lateinit var engine : Game
val NANOS_TO_SECOND = 1.0f / 1000000000.0f

class Game(context: Context, attrs: AttributeSet? = null) : SurfaceView(context, attrs), Runnable,
    SurfaceHolder.Callback {
    private val tag = "Game"
//    lateinit var player: Player
    lateinit var enemy: Enemy
    lateinit var heartBitmap: Bitmap
    lateinit var halfHeartBitmap: Bitmap

    init {
        engine = this
        holder?.addCallback(this)
        holder?.setFixedSize(screenWidth(), screenHeight())
        heartBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.lifehearth_full)
        halfHeartBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.lifehearth_half)
    }

    private lateinit var gameThread : Thread
    @Volatile var isRunning : Boolean = false
    var inputs = InputManager() // a valid null-controller
    private val camera = Viewport(screenWidth(), screenHeight(), 0.0f, 8.0f)
    val bitmapPool = BitmapPool(this)
    private val level: LevelManager = LevelManager(TestLevel())
    fun worldToScreenX(worldDistance: Float) = camera.worldToScreenX(worldDistance)
    fun worldToScreenY(worldDistance: Float) = camera.worldToScreenY(worldDistance)
    fun screenHeight() = context.resources.displayMetrics.heightPixels
    fun screenWidth() = context.resources.displayMetrics.widthPixels
    fun levelHeight() = level.levelHeight
    fun setControls(control: InputManager) {
        inputs.onPause() //give the previous controller
        inputs.onStop() //a chance to clean up
        inputs = control
        inputs.onStart()
    }

    fun getControls() = inputs

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

    private fun render() {
        val canvas = holder?.lockCanvas() ?: return
        canvas.drawColor(Color.CYAN)
        val paint = Paint()
        val heartLeft = 10f
        val heartSpacing = 2f
        for (i in 0 until 3) {
            canvas.drawBitmap(
                heartBitmap,
                heartLeft + i * (heartBitmap.width + heartSpacing),
                10f,
                paint
            )
        }
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

    private fun checkCollision() {
//        if(RectF.intersects(player.getBound(), enemy.getBound())) {
//            handleCollision(player, enemy)
//        }
    }

    private fun handleCollision(player: Player, enemy: Enemy) {
            player.health--
            //loseHealth()
    }

    /*private fun loseHealth() {
        val canvas = holder?.lockCanvas() ?: return
        val paint = Paint()
        val heartLeft = 10f
        val heartSpacing = 2f
        for (i in 0 until 3) {
            canvas.drawBitmap(
                halfHeartBitmap,
                heartLeft + i * (heartBitmap.width + heartSpacing),
                10f,
                paint
            )
        }
    }*/

    private fun buildVisibleSet() : List<Entity> {
        return level.entities.filter { camera.inView(it) }
    }

    private fun update(dt: Float) {
        level.update(dt)
        checkCollision()
        camera.lookAt(level.player)
    }

    fun onPause() {
        Log.d(tag, "onPause()")
        isRunning = false
        inputs.onPause()
    }

    fun onResume() {
        Log.d(tag, "onResume()")
        inputs.onResume()
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