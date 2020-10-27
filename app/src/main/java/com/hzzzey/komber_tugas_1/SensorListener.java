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
    private Sensor mSensorProximity;
    private Sensor mSensorLight;
    private Sensor mGameRotation;
    SensorManager mSensorManager;
    public TextView mLightSensorValue, mProximitySensorValue,mGameRotationSensorValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_listener);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mGameRotation = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);

        mLightSensorValue = findViewById(R.id.light_sensor_value);
        mProximitySensorValue = findViewById(R.id.proximity_sensor_value);
        mGameRotationSensorValue = findViewById(R.id.game_rotation_sensor_value);
        Log.d("sensorvalue", mGameRotation.toString());


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
            case Sensor.TYPE_PROXIMITY:
                //Proximity
                mProximitySensorValue.setText(currentValue+"");
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                //GamerotationSensor
                mGameRotationSensorValue.setText(currentValue+"");
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

        if (mSensorProximity != null) {
            mSensorManager.registerListener(this, mSensorProximity,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mSensorLight != null) {
            mSensorManager.registerListener(this, mSensorLight,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mGameRotation != null) {
            mSensorManager.registerListener(this, mGameRotation,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

}
