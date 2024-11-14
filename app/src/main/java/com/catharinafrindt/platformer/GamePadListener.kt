package com.catharinafrindt.platformer

import android.view.KeyEvent
import android.view.MotionEvent

interface GamepadListener {
    fun dispatchGenericMotionEvent(event: MotionEvent): Boolean
    fun dispatchKeyEvent(event: KeyEvent): Boolean
}