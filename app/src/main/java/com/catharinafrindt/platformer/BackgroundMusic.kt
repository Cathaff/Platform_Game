package com.catharinafrindt.platformer

import android.content.res.AssetManager
import android.media.MediaPlayer

class BackgroundMusic(private val assetManager: AssetManager, private var gameLevelOne: Boolean) {
    private val mediaPlayer: MediaPlayer = MediaPlayer()

    init {
        var descriptor = assetManager.openFd("levelOne.wav")

        if (!gameLevelOne) {
            descriptor = assetManager.openFd("levelTwo.wav")
        }

        mediaPlayer.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
        mediaPlayer.isLooping = true
        mediaPlayer.prepare()
    }

    fun playMusic() {
        mediaPlayer.start()
    }

    fun pauseMusic() {
        mediaPlayer.pause()
    }

    fun destroy() {
        mediaPlayer.release()
    }

}