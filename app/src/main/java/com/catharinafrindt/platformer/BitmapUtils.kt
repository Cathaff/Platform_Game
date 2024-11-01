package com.catharinafrindt.platformer

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point

object BitmapUtils {
    private const val TAG = "BitmapUtils"

    fun scaleBitmap(bmp: Bitmap, targetWidth: Int, targetHeight: Int, useBilinearFiltering: Boolean = true): Bitmap {
        if (targetWidth == bmp.width && targetHeight == bmp.height) return bmp

        val newDimensions = scaleToTargetDimensions(
            targetWidth.toFloat(),
            targetHeight.toFloat(),
            bmp.width.toFloat(),
            bmp.height.toFloat()
        )
        return Bitmap.createScaledBitmap(bmp, newDimensions.x, newDimensions.y, useBilinearFiltering)
    }

    @Throws(OutOfMemoryError::class)
    fun loadScaledBitmap(
        context: Context, bitmapName: String,
        targetWidth: Int, targetHeight: Int
    ): Bitmap {
        val res = context.resources
        val resID = res.getIdentifier(bitmapName, "drawable", context.packageName)
        return loadScaledBitmap(res, resID, targetWidth, targetHeight)
    }

    @Throws(OutOfMemoryError::class)
    fun loadScaledBitmap(
        res: Resources, resID: Int,
        targetWidth: Int, targetHeight: Int
    ): Bitmap {
        val options = readBitmapMetaData(res, resID)
        val newDimensions = scaleToTargetDimensions(
            targetWidth.toFloat(),
            targetHeight.toFloat(),
            options.outWidth.toFloat(),
            options.outHeight.toFloat()
        )
        var bitmap = loadSubSampledBitmap(res, resID, newDimensions.x, newDimensions.y, options)

        // Pixel-perfect scaling if dimensions don't match
        if (bitmap.height != newDimensions.y || bitmap.width != newDimensions.x) {
            bitmap = Bitmap.createScaledBitmap(bitmap, newDimensions.x, newDimensions.y, true)
        }
        return bitmap
    }

    private fun loadSubSampledBitmap(
        res: Resources,
        resId: Int,
        targetWidth: Int,
        targetHeight: Int,
        opts: BitmapFactory.Options
    ): Bitmap {
        opts.inSampleSize = calculateInSampleSize(opts, targetWidth, targetHeight)
        opts.inJustDecodeBounds = false
        opts.inScaled = true
        opts.inDensity = opts.outHeight
        opts.inTargetDensity = targetHeight * opts.inSampleSize

        return BitmapFactory.decodeResource(res, resId, opts)
            ?: throw IllegalArgumentException("Failed to decode resource with id $resId")
    }

    private fun calculateInSampleSize(
        opts: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = opts.outHeight
        val width = opts.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun readBitmapMetaData(res: Resources, resID: Int): BitmapFactory.Options {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(res, resID, options)
        options.inJustDecodeBounds = false
        return options
    }

    private fun scaleToTargetDimensions(
        targetWidth: Float, targetHeight: Float,
        srcWidth: Float, srcHeight: Float
    ): Point {
        require(targetWidth > 0 || targetHeight > 0) { "At least one of targetWidth or targetHeight must be non-zero" }

        val aspectRatio = srcWidth / srcHeight
        val newWidth = if (targetWidth > 0) targetWidth else targetHeight * aspectRatio
        val newHeight = if (targetHeight > 0) targetHeight else targetWidth / aspectRatio

        return Point(newWidth.toInt(), newHeight.toInt())
    }

    fun rotate(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix().apply {
            postRotate(angle)
        }
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    fun flip(source: Bitmap, horizontally: Boolean): Bitmap {
        val matrix = Matrix()
        val centerX = source.width * 0.5f
        val centerY = source.height * 0.5f
        matrix.postScale(if (horizontally) 1.0f else -1.0f, if (horizontally) -1.0f else 1.0f, centerX, centerY)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    fun scaleToHeight(source: Bitmap, height: Int): Bitmap {
        val ratio = height / source.height.toFloat()
        val newWidth = (source.width * ratio).toInt()
        return Bitmap.createScaledBitmap(source, newWidth, height, true)
    }
}