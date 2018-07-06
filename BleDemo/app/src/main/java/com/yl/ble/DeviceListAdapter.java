package com.yl.ble;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 设备列表
 * <p>
 * Created by yangle on 2018/7/5.
 * Website：http://www.yangle.tech
 */

public class DeviceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListener mItemClickListener;
    private List<BluetoothDevice> mBluetoothDeviceList;
    private List<String> mRssiList;

    public DeviceListAdapter(List<BluetoothDevice> bluetoothDeviceList, List<String> rssiList) {
        this.mBluetoothDeviceList = bluetoothDeviceList;
        this.mRssiList = rssiList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_device_list, viewGroup, false);
        return new DeviceListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final DeviceListViewHolder deviceListViewHolder = (DeviceListViewHolder) viewHolder;
        BluetoothDevice bluetoothDevice = mBluetoothDeviceList.get(i);
        String rssi = mRssiList.get(i);

        deviceListViewHolder.tvName.setText(bluetoothDevice.getName());
        deviceListViewHolder.tvMac.setText(bluetoothDevice.getAddress());
        deviceListViewHolder.tvRssi.setText("Rssi = " + rssi);

        // 回调点击事件
        if (mItemClickListener != null) {
            deviceListViewHolder.rlInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(deviceListViewHolder.rlInfo, i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mBluetoothDeviceList.size();
    }

    private class DeviceListViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlInfo;
        TextView tvName;
        TextView tvMac;
        TextView tvRssi;

        DeviceListViewHolder(View itemView) {
            super(itemView);
            rlInfo = itemView.findViewById(R.id.rl_info);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMac = itemView.findViewById(R.id.tv_mac);
            tvRssi = itemView.findViewById(R.id.tv_rssi);
        }
    }

    /**
     * 设置Item点击监听
     *
     * @param onItemClickListener 点击回调接口
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    /**
     * 点击回调接口
     */
    public interface OnItemClickListener {
        /**
         * 点击回调方法
         *
         * @param view     当前view
         * @param position 点击位置
         */
        void onItemClick(View view, int position);
    }
}
