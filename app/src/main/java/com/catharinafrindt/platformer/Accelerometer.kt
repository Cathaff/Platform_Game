package com.catharinafrindt.platformer

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.view.Surface
import androidx.core.math.MathUtils.clamp
import kotlin.math.sqrt

private const val LENGTH = 3 //azimuth (z), pitch (z), roll (y)
private val _lastAccels = FloatArray(LENGTH)
private val _lastMagFields = FloatArray(LENGTH)

private const val DEGREES_PER_RADIAN = 57.2957795f

private val _rotationMatrix = FloatArray(4 * 4)
private val _orientation = FloatArray(LENGTH)
private var _rotation: Int = 0 //default orientation of the device

private const val SHAKE_THRESHOLD = 3.25f // m/S^2
private const val SHAKE_COOLDOWN: Long = 300 //ms

private var _lastShake: Long = 0 //avoid constant jumping for minor movements of the device

private const val MAX_ANGLE = Math.PI.toFloat() / 2.0f // 90 degrees in radians
private const val MIN_INPUT = -1.0f
private const val MAX_INPUT = 1.0f

class Accelerometer(val _activity: MainActivity) : InputManager() {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            _rotation = _activity.display?.rotation ?: 0
        } else {
            _rotation = _activity.windowManager.defaultDisplay.rotation
        }
    }

    private fun registerListeners() {
        val sm = _activity.getSystemService(Activity.SENSOR_SERVICE) as SensorManager
        sm.registerListener(
            _accelerometerListener,
            sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_GAME
        )
        sm.registerListener(
            _magneticListener,
            sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    private fun unregisterListeners() {
        val sm = _activity.getSystemService(Activity.SENSOR_SERVICE) as SensorManager
        sm.unregisterListener(_accelerometerListener)
        sm.unregisterListener(_magneticListener)
    }

    override fun onResume() {
        registerListeners()
    }

    override fun onPause() {
        unregisterListeners()
    }

    override fun update(dt: Float) {
        _horizontalFactor = clamp(getHorizontalAxis() / MAX_ANGLE, MIN_INPUT, MAX_INPUT)
        _verticalFactor = 0.0f
        _isJumping = isJumping()
    }

    private val _accelerometerListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            System.arraycopy(event.values, 0, _lastAccels, 0, LENGTH)
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private val _magneticListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            System.arraycopy(event.values, 0, _lastMagFields, 0, LENGTH)
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }


    private fun getHorizontalAxis(): Float {
        if (!SensorManager.getRotationMatrix(_rotationMatrix, null, _lastAccels, _lastMagFields)) {
            // Case for devices that DO NOT have magnetic sensors
            if (_rotation == Surface.ROTATION_0) {
                return -_lastAccels[0] * 5
            } else {
                return -_lastAccels[1] * -5
            }
        } else { //we have a geomagnetic sensor and are not in free fall! Jay! :D
            if (_rotation == Surface.ROTATION_0) {
                SensorManager.remapCoordinateSystem(
                    _rotationMatrix,
                    SensorManager.AXIS_Y,
                    SensorManager.AXIS_MINUS_X,
                    _rotationMatrix
                )
                SensorManager.getOrientation(_rotationMatrix, _orientation)
                return _orientation[1] * DEGREES_PER_RADIAN
            } else {
                SensorManager.getOrientation(_rotationMatrix, _orientation)
                return -_orientation[1] * DEGREES_PER_RADIAN
            }
        }
    }

    private fun isJumping(): Boolean {
        if (System.currentTimeMillis() - _lastShake < SHAKE_COOLDOWN) {
            return _isJumping //return the old value until cooldown time has passed.
        }
        val x = _lastAccels[0]
        val y = _lastAccels[1]
        val z = _lastAccels[2]
        val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH
        if (acceleration > SHAKE_THRESHOLD) {
            _lastShake = System.currentTimeMillis()
            return true
        }
        return false
    }


}