package com.example.geoinformaticatarea

import android.content.Context
import android.hardware.Sensor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geoinformaticatarea.databinding.SensorDetailItemBinding

class SensorAdapter (val context: Context, var list: List<Sensor>, val observer: UISensor):
    RecyclerView.Adapter<SensorAdapter.holderBrandAdapter>() {
    class holderBrandAdapter(binding: SensorDetailItemBinding): RecyclerView.ViewHolder(binding.root){
        private val binding = binding
        fun binData(sensor: Sensor, observer: UISensor){
            binding.sensorName.text = sensor.name
            binding.right.setOnClickListener {
                observer.onClick(sensor = sensor)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holderBrandAdapter {
        val binding = SensorDetailItemBinding.bind(LayoutInflater.from(context).inflate(R.layout.sensor_detail_item,parent, false))
        return holderBrandAdapter(
            binding = binding
        )
    }

    override fun onBindViewHolder(holder: holderBrandAdapter, position: Int) {
        holder.binData(sensor = list[position], observer = observer)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}