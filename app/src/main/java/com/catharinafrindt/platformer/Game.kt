package com.catharinafrindt.platformer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.os.SystemClock.uptimeMillis
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random

var RNG = Random(uptimeMillis())
lateinit var engine : Game
val NANOS_TO_SECOND = 1.0f / 1000000000.0f

class Game(context: Context, attrs: AttributeSet? = null) : SurfaceView(context, attrs), Runnable,
    SurfaceHolder.Callback {
    private val tag = "Game"
    var heartBitmap: Bitmap
    var coinBitmap: Bitmap
    private var fingerDown = false
    private var isGameOver = false
    private var statusRender: StatusRender
    var jukeBox = Jukebox(context.assets)
    var background = BackgroundMusic(context.assets)

    init {
        engine = this
        holder?.addCallback(this)
        holder?.setFixedSize(screenWidth(), screenHeight())
        heartBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.lifehearth_full)
        coinBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.coinyellow)
        statusRender = StatusRender(heartBitmap, coinBitmap)
    }

    private lateinit var gameThread : Thread
    @Volatile var isRunning : Boolean = false
    var inputs = InputManager() // a valid null-controller
    private val camera = Viewport(screenWidth(), screenHeight(), 0.0f, 8.0f)
    val bitmapPool = BitmapPool(this)
    private val level: LevelManager = LevelManager(TestLevel(), context)
    fun worldToScreenX(worldDistance: Float) = camera.worldToScreenX(worldDistance)
    fun worldToScreenY(worldDistance: Float) = camera.worldToScreenY(worldDistance)
    fun screenHeight() = context.resources.displayMetrics.heightPixels
    fun screenWidth() = context.resources.displayMetrics.widthPixels
    fun levelHeight() = level.levelHeight
    fun setControls(control: InputManager) {
        inputs.onPause()
        inputs.onStop()
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
            update(dt)
            render()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> fingerDown = true
            MotionEvent.ACTION_UP -> {
                fingerDown = false
                if(isGameOver) {
                    restart()
                }
            }
        }
        return true
    }

    private fun restart() {
        level.playerHealth = PLAYER_STARTING_HEALTH
        level.collectedCoins = 0
        level.totalCoins = level.fixedTotalCoins
        level.entities.removeAll { it is Coin }
        level.fixedCoinsToAddWhenRestart.forEach { pos ->
            val coin = Coin("coinyellow", pos.x, pos.y)
            level.addEntity(coin)
        }
        level.player.respawn()
        background.playMusic()
        isGameOver = false
    }


    private fun render() {
        val canvas = holder?.lockCanvas() ?: return
        canvas.drawColor(Color.CYAN)
        val paint = Paint()
        statusRender.renderHUD(canvas, paint, level, isGameOver)
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
        if(isGameOver) {
            jukeBox.destroy()
            background.destroy()
            return
        }
        inputs.update(dt)
        level.update(dt)
        camera.lookAt(level.player)

        checkGameOver()
    }

    private fun checkGameOver() {
        if(level.playerHealth <= 0)
        {
            isGameOver = true
        }
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
        background.playMusic()
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