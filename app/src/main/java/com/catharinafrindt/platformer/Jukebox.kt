package com.catharinafrindt.platformer

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.SoundPool
import java.io.IOException

object SFX{
    var coins = 0
    var levelSound = 1
}
const val MAX_STREAMS = 3


class Jukebox(private val assetManager: AssetManager) {
    private val tag = "Jukebox"
    private val soundPool: SoundPool

    init {
        val attr = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setAudioAttributes(attr)
            .setMaxStreams(MAX_STREAMS)
            .build()
        SFX.coins = loadSound("coins.wav")
        SFX.levelSound = loadSound("level1.wav")
    }

    private fun loadSound(fileName: String): Int{
        try {
            val descriptor: AssetFileDescriptor = assetManager.openFd(fileName)
            return soundPool.load(descriptor, 1)
        }catch(e: IOException){
//            Log.d(tag, "Unable to load $fileName! Check the filename, and make sure it's in the assets-folder.")
        }
        return 0
    }

    fun play(soundID: Int, loop: Int) {
        val leftVolume = 1f
        val rightVolume = 1f
        val priority = 0
        val playbackRate = 1.0f
        if (soundID > 0) {
            soundPool.play(soundID, leftVolume, rightVolume, priority, loop, playbackRate)
        }
    }

    fun destroy() {
        soundPool.release()
        //the soundpool can no longer be used! you have to create a new soundpool.
    }
}