package com.micrositba.lifemonitor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    static String deviceAddressInUse;
    static final int PICK_DEVICE = 1;  // The request code


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Create Option Menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_devices:
                Intent startDevicesActivity = new Intent(this, DevicesActivity.class);
                startDevicesActivity.putExtra("addressInUse",deviceAddressInUse);
                startActivityForResult(startDevicesActivity,PICK_DEVICE);
                break;
            case R.id.action_info_item:
                DialogFragment newFragment = new InfoDialog();
                newFragment.show(getSupportFragmentManager(), null);
                break;
            case R.id.action_settings_item:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        // Check which request we're responding to
        if (requestCode == PICK_DEVICE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                deviceAddressInUse = data.getStringExtra("data_address");
                Toast.makeText(this,deviceAddressInUse,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
