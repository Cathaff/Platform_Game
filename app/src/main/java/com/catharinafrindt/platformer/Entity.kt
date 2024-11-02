package com.catharinafrindt.platformer

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.util.Log
import kotlin.math.abs

abstract class Entity {
    private val tag = "Entity"
    var x = 0f
    var y = 0f
    var width = 0f
    var height = 0f

    init {
        Log.d(tag, "Entity created")
    }

    open fun update(dt: Float) {}
    open fun render(canvas: Canvas, transform: Matrix, paint: Paint) {}
    open fun onCollision(that: Entity) {} // Notify the Entity about collisions

    var left: Float
        get() = x
        set(value) {
            x = value
        }
    var right: Float
        get() = x + width
        set(value) {
            x = value - width
        }
    var top: Float
        get() = y
        set(value) {
            y = value
        }
    var bottom: Float
        get() = y + height
        set(value) {
            y = value - height
        }
    var centerX: Float
        get() = x + (width * 0.5f)
        set(value) {
            x = value - (width * 0.5f)
        }
    var centerY: Float
        get() = y + (height * 0.5f)
        set(value) {
            y = value - (height * 0.5f)
        }
}

//a basic axis-aligned bounding box intersection test.
//see: https://gamedev.stackexchange.com/questions/586/what-is-the-fastest-way-to-work-out-2d-bounding-box-intersection
fun isColliding(a: Entity, b: Entity): Boolean {
    return !(a.right <= b.left || b.right <= a.left || a.bottom <= b.top || b.bottom <= a.top)
}

fun getOverlap(a: Entity, b: Entity): PointF? {
    val centerDeltaX = a.centerX - b.centerX
    val halfWidths = (a.width + b.width) * 0.5f
    val dx = abs(centerDeltaX)

    if (dx > halfWidths) return null // No overlap on X

    val centerDeltaY = a.centerY - b.centerY
    val halfHeights = (a.height + b.height) * 0.5f
    val dy = abs(centerDeltaY)

    if (dy > halfHeights) return null // No overlap on Y

    val overlapX = halfWidths - dx
    val overlapY = halfHeights - dy

    // Determine the direction of the overlap and return the smallest one.
    return PointF(
        if (overlapX < overlapY) {
            if (centerDeltaX < 0) -overlapX else overlapX
        } else {
            0f
        },
        if (overlapY < overlapX) {
            if (centerDeltaY < 0) -overlapY else overlapY
        } else {
            0f
        }
    )
}
