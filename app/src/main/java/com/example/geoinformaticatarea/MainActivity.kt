package com.example.geoinformaticatarea

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geoinformaticatarea.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity(), UISensor {

    private lateinit var sensorManager: SensorManager
    private lateinit var sensorList: List<Sensor>
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SensorAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)
        adapter = SensorAdapter(this@MainActivity, list = sensorList, observer = this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    override fun onClick(sensor: Sensor) {
        SensorSingleton.getInstance().setSensor(sensor = sensor)
        startActivity(Intent(this@MainActivity, DetailSensorActivity::class.java))
    }

}