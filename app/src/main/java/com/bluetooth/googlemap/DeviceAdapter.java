package com.bluetooth.googlemap;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluetooth.googlemap.manager.ColorManager;
import com.bluetooth.googlemap.manager.Manager;
import com.bluetooth.googlemap.modals.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {
    public List<Device> mDeviceList = new ArrayList<>();
    private ColorManager colorManager = ColorManager.getInstance();

    public DeviceAdapter() {
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_item, parent, false);

        DeviceHolder deviceHolder = new DeviceHolder(view);

        return deviceHolder;
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int position) {
        Device device = mDeviceList.get(position);
        holder.textDeviceName.setText(device.getName());
        holder.textDeviceAddress.setText("MAC: " + device.getAddress());
        holder.textDeviceSignal.setText(device.getSignal() + "dBm");

        if (colorManager.isThemeDark) {
            holder.textDeviceName.setTextColor(colorManager.DARK_TEXT);
            holder.textDeviceAddress.setTextColor(colorManager.DARK_TEXT);
            holder.textDeviceSignal.setTextColor(colorManager.DARK_TEXT);
            holder.itemView.setBackgroundColor(colorManager.DARK_BACKGROUND);
        } else {
            holder.textDeviceName.setTextColor(colorManager.BRIGHT_TEXT);
            holder.textDeviceAddress.setTextColor(colorManager.BRIGHT_TEXT);
            holder.textDeviceSignal.setTextColor(colorManager.BRIGHT_TEXT);
            holder.itemView.setBackgroundColor(colorManager.BRIGHT_BACKGROUND);
        }
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    public void setDeviceList(List<Device> mDeviceList) {
        this.mDeviceList = mDeviceList;
        this.notifyDataSetChanged();
    }

    static class DeviceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textDeviceName;
        TextView textDeviceAddress;
        TextView textDeviceSignal;
        TextView textDevicePaired;

        public DeviceHolder(View itemView) {
            super(itemView);
            textDeviceName = itemView.findViewById(R.id.text_name);
            textDeviceAddress = itemView.findViewById(R.id.text_address);
            textDeviceSignal = itemView.findViewById(R.id.text_signal);
            textDevicePaired = itemView.findViewById(R.id.text_paired);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            String s = this.textDeviceAddress.getText().toString();
            Pattern pattern = Pattern.compile("(([0-9A-Fa-f]{2}[:-]){5}[0-9A-Fa-f]{2})");
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                Manager.getInstance().showDetails(matcher.group(1));
            }

        }

        public void setTextColor(int color) {
            this.textDeviceName.setTextColor(color);
            this.textDeviceAddress.setTextColor(color);
            this.textDeviceSignal.setTextColor(color);
            this.textDevicePaired.setTextColor(color);
        }
    }
}