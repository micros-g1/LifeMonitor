package com.micrositba.lifemonitor

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

import java.util.ArrayList

  // The request code

class DevicesActivity : AppCompatActivity(), ItemClickListener {
    private var deviceList: MutableList<BluetoothDeviceData> = ArrayList()
    private var addressInUse: String? = null
    companion object
    {
        const val PICK_DEVICE = 1
    }

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothAdapter.ACTION_STATE_CHANGED == action)
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)) {
                    BluetoothAdapter.STATE_ON, BluetoothAdapter.STATE_OFF ->
                        //If bluetooth state changes, update devices list
                        updateBluetoothDevicesList()
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devices)
        val actionBar = this.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        //Get address in use in order to highlight address
        val intent = intent
        val extras = intent.extras
        if (extras != null)
            this.addressInUse = extras.getString("addressInUse", null)
        registerReceiver(mReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    override fun onResume() {
        super.onResume()
        updateBluetoothDevicesList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //Create Option Menu
        val inflater = menuInflater
        inflater.inflate(R.menu.devices_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_bluetooth) {
            //Go to Bluetooth settings
            val intentOpenBluetoothSettings = Intent()
            intentOpenBluetoothSettings.action = android.provider.Settings.ACTION_BLUETOOTH_SETTINGS
            startActivity(intentOpenBluetoothSettings)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateBluetoothDevicesList() {
        deviceList.clear()
        //Message TextView

        val message = findViewById<TextView>(R.id.devices_message)
        //Get RecyclerView
        val mRecyclerView: RecyclerView = findViewById(R.id.devices_list_recyclerView)

        //Configure Stuff
        val mLayoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView.layoutManager = mLayoutManager
        //Separation Lines
        mRecyclerView.addItemDecoration(
                DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        val mAdapter = BluetoothDeviceDataAdapter(deviceList, addressInUse, this)
        mRecyclerView.adapter = mAdapter
        mAdapter.setClickListener(this)

        //Bluetooth Handling
        val mBluetoothAdapter : BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            message.text = resources.getText(R.string.message_bluetooth_not_available)
            message.visibility = View.VISIBLE
        } else if (!mBluetoothAdapter.isEnabled) {
            message.text = resources.getText(R.string.message_bluetooth_disabled)
            message.visibility = View.VISIBLE
        } else {
            message.visibility = View.GONE
            // Bluetooth is enabled
            val pairedDevices = mBluetoothAdapter.bondedDevices
            for (bt in pairedDevices) {
                //Add name and address of each device
                deviceList.add(BluetoothDeviceData(bt.name, bt.address))
            }
            //Device list empty? Show no devices message
            if (deviceList.size == 0) {
                message.text = resources.getString(R.string.message_no_paired_devices)
                message.visibility = View.VISIBLE
            }
        }
        //Inform adapter
        mAdapter.notifyDataSetChanged()
    }

    override fun onClick(view: View, position: Int) {
        val intent = Intent()
        //Return information about selected device
        intent.putExtra("data_name", (view.findViewById<View>(R.id.device_name) as TextView).text)
        intent.putExtra("data_address", (view.findViewById<View>(R.id.device_address) as TextView).text)
        setResult(Activity.RESULT_OK, intent)
        //Finish Activity
        finish()
    }
}