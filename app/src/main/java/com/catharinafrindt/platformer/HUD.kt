package com.catharinafrindt.platformer

import android.graphics.Canvas
import android.graphics.Paint

interface HUD {
   fun renderHUD(canvas: Canvas, paint: Paint, level: LevelManager)
}