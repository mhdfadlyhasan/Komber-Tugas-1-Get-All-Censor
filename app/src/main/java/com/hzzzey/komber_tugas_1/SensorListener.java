 package com.hzzzey.komber_tugas_1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SensorListener extends AppCompatActivity implements SensorEventListener, View.OnClickListener {
    private Sensor mSensorLight;
    private Sensor mAcceloMeter;
    int i=0,j=0, iter = 1;

    Boolean record = false, kill = false;
    long timer;
    SensorManager mSensorManager;
    LineChart LineChartAccel;
    List<Entry> entriesX,entriesY,entriesZ;
    LineDataSet dataSetX,dataSetY,dataSetZ;
    Vibrator vibe;
    Button button, btnSave;
    public TextView mLightSensorValue, mAccelometerValue;
    MediaPlayer mPlay, Mstop;
    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            // TODO tambah fitur detect delay
            for (int paksi = 0; paksi <= 30; ) {
                if (j < 20) {
                    record = true;
                } else {
                    j = 0;
                    record = false;
                    Mstop.start();
                    SystemClock.sleep(2000);
                    timer = System.currentTimeMillis(); //reset timer
                    mPlay.start();
                    paksi++;
                }
            }
            record = false;
            s.interrupt();
            if (!btnSave.isEnabled()) runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnSave.setEnabled(true);
                }
            });
        }
    });
    Thread s = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true)
            {
                if(!t.isAlive()){
                    if(System.currentTimeMillis() - timer > 10000)
                    {
                        mPlay.start();
                        timer = System.currentTimeMillis();
                        t.start();
                        break;
                    }
                }
                else {
                    t.interrupt();
                    record = false;
                    break;
                }
            }

        }});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_listener);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
            }
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mAcceloMeter = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mLightSensorValue = findViewById(R.id.light_sensor_value);
        mAccelometerValue = findViewById(R.id.game_rotation_sensor_value);
        LineChartAccel = (LineChart) findViewById(R.id.accelMeterChart);

        button = findViewById(R.id.buttonstartstop);
        btnSave = findViewById(R.id.btn_save_record);
        button.setTag(0);
        button.setText("start tracking");
        button.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnSave.setEnabled(false);

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
        mPlay = MediaPlayer.create(SensorListener.this, R.raw.beep02);
        Mstop  = MediaPlayer.create(SensorListener.this, R.raw.beepdone);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101 :
                if (grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Anda harus mengijinkan aplikasi menulis sebelum melanjutkan.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        float currentValue = event.values[0];
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                if (record) {
                    //todo add value was here
                    addValue(event);
                    SystemClock.sleep(100);
                }
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
        if (mAcceloMeter != null) {
            mSensorManager.registerListener(this, mAcceloMeter,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    //  biar teks tombolnya bisa ganti, sama switch tracking
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonstartstop :
                timer = System.currentTimeMillis();
//                jalankan thread baru
                s.start();
                break;
            case R.id.btn_save_record:
                t.interrupt();
                if (isExternalStorageWritable() && btnSave.isEnabled() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    String csv = "", filename = "test" + String.valueOf(iter) + ".csv";
                    File csvFile = new File(getExternalFilesDir(null), filename);
                    for (int j = 0; j < entriesX.size() - 1; j++) {
                        String data;
                        data = entriesX.get(j).getY() + "," + entriesY.get(j).getY() + "," + entriesZ.get(j).getY() + "\n";
                        csv = csv.concat(data);
                    }

                    Log.d("Saverecord", csv);
                    try {
                        FileOutputStream fos = new FileOutputStream(csvFile);
                        fos.write(csv.getBytes());
                        fos.close();
                        Toast.makeText(this, "File saved as " + filename, Toast.LENGTH_SHORT).show();
                        iter++;
                        btnSave.setEnabled(false);
                    } catch (IOException err) {
                        err.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Isi supaya LineChart rilis mem yang dipake
        try {
            t.interrupt();
            kill = true;
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public void addValue(SensorEvent event){
        i++;
        j++;
        Log.d("sensorval" , String.valueOf(i));
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

    private boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}