package com.micrositba.lifemonitor

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var deviceAddressInUse: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //Create Option Menu
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_devices -> {
                val startDevicesActivity = Intent(this, DevicesActivity::class.java)
                startDevicesActivity.putExtra("addressInUse", deviceAddressInUse)
                startActivityForResult(startDevicesActivity, DevicesActivity.PICK_DEVICE)
            }
            R.id.action_info_item -> {
                val newFragment = InfoDialog()
                newFragment.show(supportFragmentManager, null)
            }
            R.id.action_settings_item -> {
                val startSettingsActivity = Intent(this, SettingsActivity::class.java)
                startActivity(startSettingsActivity)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check which request we're responding to
        if (requestCode == DevicesActivity.PICK_DEVICE) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                deviceAddressInUse = data!!.getStringExtra("data_address")
                Toast.makeText(this, deviceAddressInUse,
                        Toast.LENGTH_SHORT).show()
            }
        }
    }
}
