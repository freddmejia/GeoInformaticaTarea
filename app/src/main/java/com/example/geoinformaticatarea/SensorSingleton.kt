package com.example.geoinformaticatarea

import android.hardware.Sensor

class SensorSingleton private constructor() {

    private var sensor: Sensor? = null

    fun setSensor(sensor: Sensor) {
        this.sensor = sensor
    }

    fun getSensor(): Sensor? {
        return sensor
    }

    companion object {
        @Volatile private var instance: SensorSingleton? = null

        fun getInstance(): SensorSingleton {
            return instance ?: synchronized(this) {
                instance ?: SensorSingleton().also { instance = it }
            }
        }
    }
}