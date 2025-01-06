package com.catharinafrindt.platformer

import android.content.res.AssetManager
import android.media.MediaPlayer

class BackgroundMusic(private val assetManager: AssetManager) {
    private val mediaPlayer: MediaPlayer = MediaPlayer()

    init {
        val descriptor = assetManager.openFd("levelOne.wav")
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