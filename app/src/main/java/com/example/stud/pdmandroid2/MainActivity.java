package com.example.stud.pdmandroid2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor sensor;
    TextView numar1;
    TextView numar2;
    float gravity[] = new float[3];
    float linear_acceleration[] = new float[3];
    float valoareCritica = 5.0f;
    int contorizare = 0;
    float[] memorare = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numar1 = (TextView) findViewById(R.id.number1);
        numar2 = (TextView) findViewById(R.id.number2);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        final float alpha = 0.8f;


        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        //String text = linear_acceleration[0] + " " + linear_acceleration[1] + " " + linear_acceleration[2];

        if((linear_acceleration[0] > valoareCritica || linear_acceleration[0] < -valoareCritica) ||
                (linear_acceleration[1] > valoareCritica || linear_acceleration[1] < -valoareCritica) ||
                (linear_acceleration[2] > valoareCritica || linear_acceleration[2] < -valoareCritica)) {
            if(contorizare > 3) {
                numar1.setText(String.valueOf(new Random().nextInt(5) + 1));
                numar2.setText(String.valueOf(new Random().nextInt(5) + 1));
                contorizare = 0;
            } else {
                contorizare++;
            }
            memorare[0] = linear_acceleration[0];
            memorare[1] = linear_acceleration[1];
            memorare[2] = linear_acceleration[2];
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
