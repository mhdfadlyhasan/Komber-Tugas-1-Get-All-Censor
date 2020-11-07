package com.hzzzey.komber_tugas_1;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class SensorListener extends AppCompatActivity implements SensorEventListener, View.OnClickListener {
    private Sensor mSensorLight;
    private Sensor mAcceloMeter;
    int i=0;
    Boolean started = false;
    SensorManager mSensorManager;
    LineChart LineChartAccel;
    List<Entry> entriesX,entriesY,entriesZ;
    LineDataSet dataSetX,dataSetY,dataSetZ;
    Vibrator vibe;
    Button button;
    public TextView mLightSensorValue, mAccelometerValue;

    // disini aman ga ya, kalo dimasukin ke StartStopTracking ntar fast as fuck
    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            SystemClock.sleep(3000);
            started = !started;
            vibe.vibrate(1000);
            SystemClock.sleep(15000);
            started = !started;
            vibe.vibrate(1000);
        }
    });

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

        button = findViewById(R.id.buttonstartstop);
        button.setTag(0);
        button.setText("start tracking");
        button.setOnClickListener(this);

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        entriesX = new ArrayList<Entry>();
        entriesY = new ArrayList<Entry>();
        entriesZ = new ArrayList<Entry>();

        dataSetX = new LineDataSet(entriesX,"Value X");
        dataSetX.setColor(Color.GREEN);
        dataSetY = new LineDataSet(entriesY,"Value Y");
        dataSetY.setColor(Color.RED);
        dataSetZ = new LineDataSet(entriesZ,"Value Z");
        dataSetZ.setColor(Color.BLUE);


        LineData data = new LineData(dataSetX,dataSetY,dataSetZ);
        LineChartAccel.setData(data);
        LineChartAccel.invalidate();
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
                addValue(event);
                mAccelometerValue.setText(currentValue+"");
                break;
            default:
                // do nothing
        }
    }
    public void addValue(SensorEvent event){
        if (started){

            i++;
            dataSetX.addEntry(new Entry(i, event.values[0]));
            dataSetY.addEntry(new Entry(i, event.values[1]));
            dataSetZ.addEntry(new Entry(i, event.values[2]));

            dataSetX.notifyDataSetChanged();
            dataSetY.notifyDataSetChanged();
            dataSetZ.notifyDataSetChanged();

            LineChartAccel.getLineData().notifyDataChanged();
            LineChartAccel.notifyDataSetChanged();
            LineChartAccel.invalidate();
        }
    }

//    entah kenapa ga pakai ini, work2 aja
//    public void StartStopTracking(View v){
//
////        t.start();
//
//    }

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

//  biar teks tombolnya bisa ganti, sama switch tracking
    @Override
    public void onClick(View view) {
        if(started == false){
            button.setText("stop tracking");
            started = true;
        }
        else{
            button.setText("start tracking");
            started = false;
        }
    }
}