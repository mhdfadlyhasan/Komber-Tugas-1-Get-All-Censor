package com.hzzzey.komber_tugas_1;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class SensorListener extends AppCompatActivity implements SensorEventListener {
    private Sensor mSensorLight;
    private Sensor mAcceloMeter;
    int i=0;
    Boolean started = false;
    SensorManager mSensorManager;
    LineChart LineChartAccel;
    List<Entry> entriesX,entriesY,entriesZ;
    LineDataSet dataSetX,dataSetY,dataSetZ;
    public TextView mLightSensorValue, mAccelometerValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_listener);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mAcceloMeter = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mLightSensorValue = findViewById(R.id.light_sensor_value);
        mAccelometerValue = findViewById(R.id.game_rotation_sensor_value);
        LineChartAccel = (LineChart) findViewById(R.id.accelMeterChart);
        entriesX = new ArrayList<Entry>();
        entriesY = new ArrayList<Entry>();
        entriesZ = new ArrayList<Entry>();

        entriesX.add(new Entry(0,0));
        entriesY.add(new Entry(0,0));
        entriesZ.add(new Entry(0,0));

        dataSetX = new LineDataSet(entriesX,"Value X");
        dataSetX.setColor(Color.GREEN);
        dataSetY = new LineDataSet(entriesY,"Value Y");
        dataSetY.setColor(Color.RED);
        dataSetZ = new LineDataSet(entriesZ,"Value Z");
        dataSetZ.setColor(Color.BLUE);


        LineData data = new LineData(dataSetX,dataSetY,dataSetZ);
        LineChartAccel.setData(data);
        LineChartAccel.invalidate();
//
//
//        dataSetX.addEntry(new Entry(1,1));
//        dataSetY.addEntry(new Entry(2,2));
//        dataSetZ.addEntry(new Entry(3,3));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        int sensorType = event.sensor.getType();
        float currentValue = event.values[0];
        switch (sensorType) {
            // Event came from the light sensor.
            case Sensor.TYPE_LIGHT:
                // Handle light sensor
                mLightSensorValue.setText(currentValue+"");
                break;
            case Sensor.TYPE_ACCELEROMETER:
                addValue(i,event);
                i++;
                mAccelometerValue.setText(currentValue+"");
                break;
            default:
                // do nothing
        }
    }
    public void addValue(int i, SensorEvent event){
        if (started){

            dataSetX.addEntry(new Entry(i, event.values[0]));
            dataSetY.addEntry(new Entry(i, event.values[1]));
            dataSetZ.addEntry(new Entry(i, -10+event.values[2]));

            dataSetX.notifyDataSetChanged();
            dataSetY.notifyDataSetChanged();
            dataSetZ.notifyDataSetChanged();

            LineChartAccel.getLineData().notifyDataChanged();
            LineChartAccel.notifyDataSetChanged();
            LineChartAccel.invalidate();
        }
    }
    public void StartStopTracking(View v){
        started = !started;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mSensorLight != null) {
            mSensorManager.registerListener(this, mSensorLight,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mAcceloMeter != null) {
            mSensorManager.registerListener(this, mAcceloMeter,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}
