package com.hzzzey.komber_tugas_1;

import android.hardware.Sensor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SensorListAdapter  extends RecyclerView.Adapter<SensorListAdapter.SensorViewHolder> {
    private List<Sensor> mDataset;

    public SensorListAdapter(List<Sensor> deviceSensors) {
        mDataset = deviceSensors;

        Log.d("sensor","mati");
    }

    public static class SensorViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public SensorViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.asdf);
        }
    }


    @NonNull
    @Override
    public SensorListAdapter.SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tests, parent, false);
        SensorViewHolder vh = new SensorViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SensorListAdapter.SensorViewHolder holder, int position) {
        holder.textView.setText(mDataset.get(position).getName());
        Log.d("sensor", "ini" + mDataset.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
