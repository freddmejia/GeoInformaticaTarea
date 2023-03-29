package com.example.geoinformaticatarea

import android.animation.ValueAnimator
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.geoinformaticatarea.databinding.ActivityDetailSensorBinding
import com.example.geoinformaticatarea.databinding.SensorDetailItemBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class DetailSensorActivity : AppCompatActivity(), SensorEventListener {
    val TAG = "DetailSensorActivity"
    private lateinit var sensorManager: SensorManager
    private lateinit var binding: ActivityDetailSensorBinding
    private var sensor: Sensor? = null
    var sensorData = ArrayList<Float>()
    var temperatureEntries = mutableListOf<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSensorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        showSensorData()
        temperatureEntries = mutableListOf()
    }

    fun showSensorData(){
        try {
            sensor = SensorSingleton.getInstance().getSensor()
            binding.sensorName.text = sensor?.name
            binding.versionName.text = sensor?.version.toString()
            //binding.factoryName.text = sensor?.vendor
            binding.providerName.text = sensor?.vendor
            binding.typeName.text = sensor?.stringType.toString()
            Log.e(TAG, "getExtraDeviceData: "+sensor?.name.toString()
            +" \n "+sensor?.type.toString())

            registerSensorListener()


        }
        catch (e: java.lang.Exception){
            Log.e(TAG, "getExtraDeviceData: "+e.message )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        binding.bulbImage.isVisible = false
        binding.sensorChart.isVisible = false
        binding.accel.isVisible = false
        when(sensor?.type){
            Sensor.TYPE_LIGHT -> {

                event?.values?.get(0)?.toInt()?.let { updateBulb(value = it) }
                binding.bulbImage.isVisible = true
            }
            Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE,
            Sensor.TYPE_MAGNETIC_FIELD, Sensor.TYPE_ORIENTATION -> {
                binding.accel.isVisible = true
                binding.val1.text = event?.values?.get(0)?.toString()
                binding.val2.text = event?.values?.get(1)?.toString()
                binding.val3.text = event?.values?.get(2)?.toString()
            }
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                binding.sensorChart.isVisible = true
                event?.values?.get(0)?.let { updateTemperature(temp = it) }
            }
            else -> {

            }
        }

        Log.e(TAG, "onSensorChanged: "+ event?.values?.joinToString(", ") )
    }

    fun updateBulb(value: Int) {
        val opacity = value.toFloat() / 40000f
        val anim = AlphaAnimation(binding.bulbImage.alpha, opacity)
        anim.fillAfter = true
        binding.bulbImage.startAnimation(anim)
    }

    fun updateTemperature (temp: Float) {
        temperatureEntries.clear()
        val initialTime = System.currentTimeMillis()
        temperatureEntries.add(Entry(initialTime.toFloat(), temp))
        val dataSet = LineDataSet(temperatureEntries, "Temperatura")
        val data = LineData(dataSet)
        binding.sensorChart.data = data
        binding.sensorChart.notifyDataSetChanged()
        binding.sensorChart.invalidate()
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onResume() {
        super.onResume()
        registerSensorListener()
    }

    override fun onPause() {
        super.onPause()
        unregisterSensorListener()
    }

    private fun registerSensorListener() {
        if (sensor != null)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

    }

    private fun unregisterSensorListener() {
        sensorManager.unregisterListener(this)
    }

}