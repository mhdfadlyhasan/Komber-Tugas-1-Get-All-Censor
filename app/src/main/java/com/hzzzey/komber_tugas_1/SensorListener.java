package com.hzzzey.komber_tugas_1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SensorListener extends AppCompatActivity implements SensorEventListener {
    private Sensor mSensorLight;
    private Sensor mAccelometer;
    SensorManager mSensorManager;
    public TextView mLightSensorValue, mAccelometerValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_listener);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mAccelometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mLightSensorValue = findViewById(R.id.light_sensor_value);
        mAccelometerValue = findViewById(R.id.game_rotation_sensor_value);


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
                mAccelometerValue.setText(currentValue+"");
                break;
            default:
                // do nothing
        }

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
        if (mAccelometer != null) {
            mSensorManager.registerListener(this, mAccelometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

}
