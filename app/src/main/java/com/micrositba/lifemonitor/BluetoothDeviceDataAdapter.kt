package com.micrositba.lifemonitor

import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat

class BluetoothDeviceDataAdapter(
        //List of bluetooth devices
        private val availableDevicesList: List<BluetoothDeviceData>,
        //Address to highlight
        private val addressInUse: String?,
        //Context
        private val mContext: Context?)
    : RecyclerView.Adapter<BluetoothDeviceDataAdapter.DeviceViewHolder>() {

    //Item Click Listener associated with adapter
    private var clickListener: ItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_device, parent, false)
        return DeviceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        //Set device info
        holder.name.text = availableDevicesList[position].name
        holder.address.text = availableDevicesList[position].address
        holder.itemView.tag = position
        //If address matches address in use, change indicator
        if (mContext != null && availableDevicesList[position].address == addressInUse) {
            val view = holder.itemView.findViewById<View>(R.id.device_status_indicator)
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent))
        }
    }

    override fun getItemCount(): Int {
        return availableDevicesList.size
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        this.clickListener = itemClickListener
    }

    inner class DeviceViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val name: TextView = itemView.findViewById(R.id.device_name)
        val address: TextView = itemView.findViewById(R.id.device_address)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            clickListener?.onClick(v, adapterPosition)
        }
    }
}
