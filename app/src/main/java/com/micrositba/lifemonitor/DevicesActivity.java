package com.micrositba.lifemonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DevicesActivity extends AppCompatActivity  implements ItemClickListener{
    RecyclerView mRecyclerView;
    BluetoothDeviceDataAdapter mAdapter;
    List<BluetoothDeviceData> deviceList = new ArrayList<>();
    String addressInUse = null;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive (Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
            switch (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)){
                case BluetoothAdapter.STATE_ON:
                case BluetoothAdapter.STATE_OFF:
                    //If bluetooth state changes, update devices list
                    updateBluetoothDevicesList();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        //Get address in use in order to highlight address
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null)
            this.addressInUse = extras.getString("addressInUse",null);
        registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBluetoothDevicesList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Create Option Menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.devices_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_bluetooth) {
            //Go to Bluetooth settings
            Intent intentOpenBluetoothSettings = new Intent();
            intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
            startActivity(intentOpenBluetoothSettings);
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateBluetoothDevicesList()
    {
        TextView message = findViewById(R.id.no_paired_devices_message);
        deviceList.clear();
        //Get RecyclerView
        mRecyclerView = findViewById(R.id.devices_list_recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Separation Lines
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new BluetoothDeviceDataAdapter(deviceList,addressInUse,this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);

        //Bluetooth Handling
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            message.setText(getResources().getText(R.string.message_bluetooth_not_available));
            message.setVisibility(View.VISIBLE);
            // Device does not support Bluetooth
        } else if (!mBluetoothAdapter.isEnabled()) {
            message.setText(getResources().getText(R.string.message_bluetooth_disabled));
            message.setVisibility(View.VISIBLE);
        } else {
            message.setVisibility(View.GONE);
            // Bluetooth is enabled
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            for (BluetoothDevice bt : pairedDevices) {
                //Add name and address of each device
                deviceList.add(new BluetoothDeviceData(bt.getName(),bt.getAddress()));
            }
            //Device list empty? Show no devices message
            if(deviceList.size() == 0)
            {
                message.setText(getResources().getString(R.string.message_no_paired_devices));
                message.setVisibility(View.VISIBLE);
            }
        }
        //Inform adapter
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent();
        //Return information about selected device
        intent.putExtra("data_name", ((TextView) view.findViewById(R.id.device_name)).getText());
        intent.putExtra("data_address", ((TextView) view.findViewById(R.id.device_address)).getText());
        setResult(RESULT_OK,intent);
        //Finish Activity
        finish();
    }
}