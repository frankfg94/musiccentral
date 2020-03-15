package com.gillioen.navbarmusiccentral;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ShakeEventManager implements SensorEventListener {

    @Nullable
    private SensorManager sManager;
    private Sensor s;

    private static final int MOV_COUNTS = 2;
    private static int mov_threshold = 8;
    private static final float ALPHA = 0.8F;
    private static final int SHAKE_WINDOW_TIME_INTERVAL = 500; // milliseconds

    // Gravity force on x,y,z axis
    @NonNull
    private float[] gravity = new float[3];

    private int counter;
    private long firstMovTime;
    private ShakeListener listener;
    private Context ctx;

    public ShakeEventManager() {
    }

    public void setListener(ShakeListener listener) {
        this.listener = listener;
    }

    public void init(@NonNull Context ctx) {
        sManager = (SensorManager)  ctx.getSystemService(Context.SENSOR_SERVICE);
        s = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.ctx = ctx;
        register();
    }

    public void register() {
        sManager.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(@NonNull SensorEvent sensorEvent) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.ctx);
        if(prefs.getBoolean("ShufflePreferences", false)) {
            mov_threshold = Integer.parseInt(prefs.getString("shake", "10"));
            float maxAcc = calcMaxAcceleration(sensorEvent);
            if (maxAcc >= mov_threshold) {
                if (counter == 0) {
                    counter++;
                    firstMovTime = System.currentTimeMillis();
                } else {
                    long now = System.currentTimeMillis();
                    long tot = (now - firstMovTime);
                    if (tot != 0 && tot < SHAKE_WINDOW_TIME_INTERVAL)
                        counter++;
                    else {
                        resetAllData();
                        return;
                    }
                    Log.d("SwA", "Mov counter [" + counter + "]");

                    if (counter >= MOV_COUNTS)
                    {
                        if (listener != null)
                            listener.onShake();
                        resetAllData();
                    }

                }
            }
        }

    }

    public void deregister()  {
        sManager.unregisterListener(this);
    }


    private float calcMaxAcceleration(@NonNull SensorEvent event) {
        gravity[0] = calcGravityForce(event.values[0], 0);
        gravity[1] = calcGravityForce(event.values[1], 1);
        gravity[2] = calcGravityForce(event.values[2], 2);

        float accX = event.values[0] - gravity[0];
        float accY = event.values[1] - gravity[1];
        float accZ = event.values[2] - gravity[2];

        float max1 = Math.max(accX, accY);
        return Math.max(max1, accZ);
    }

    // Low pass filter
    private float calcGravityForce(float currentVal, int index) {
        return  ALPHA * gravity[index] + (1 - ALPHA) * currentVal;
    }


    private void resetAllData() {
        counter = 0;
        firstMovTime = System.currentTimeMillis();
    }


    public interface ShakeListener {
        void onShake();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
