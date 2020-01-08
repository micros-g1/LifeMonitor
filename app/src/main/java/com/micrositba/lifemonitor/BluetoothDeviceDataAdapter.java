package com.micrositba.lifemonitor;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class BluetoothDeviceDataAdapter extends RecyclerView.Adapter<BluetoothDeviceDataAdapter.DeviceViewHolder>{

    //List of bluetooth devices
    private List<BluetoothDeviceData> availableDevicesList;
    //Item Click Listener associated with adapter
    private ItemClickListener clickListener;
    //Address to highlight
    private String addressInUse;
    //Context
    private Context mContext;

    BluetoothDeviceDataAdapter(List<BluetoothDeviceData> availableDevicesList, String addressInUse, Context mContext) {
        this.availableDevicesList = availableDevicesList;
        this.addressInUse = addressInUse;
        this.mContext = mContext;
    }

    @Override
    @NonNull
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        //Set device info
        holder.name.setText(availableDevicesList.get(position).getName());
        holder.address.setText(availableDevicesList.get(position).getAddress());
        holder.itemView.setTag(position);
        //If address matches address in use, change indicator
        if(mContext != null && availableDevicesList.get(position).getAddress().equals(addressInUse)) {
            View view = holder.itemView.findViewById(R.id.device_status_indicator);
            view.setBackgroundColor(mContext.getResources()
                    .getColor(R.color.colorAccent));
        }
    }

    @Override
    public int getItemCount() {
        return availableDevicesList.size();
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private TextView address;
        DeviceViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.device_name);
            address = itemView.findViewById(R.id.device_address);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }
}
