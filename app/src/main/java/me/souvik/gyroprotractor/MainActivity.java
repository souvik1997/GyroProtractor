package me.souvik.gyroprotractor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView label;
    private SensorManager sensor;

    private float[] set = null;
    private boolean copyValues = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        label = (TextView) findViewById(R.id.textView);
        sensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyValues = true;
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (copyValues)
        {
            if (set == null)
            {
                set = new float[3];
            }
            System.arraycopy(event.values, 0, set, 0, 3);
            copyValues = false;
        }
        if (set != null)
        {
            double dot = set[0]*event.values[0] + set[1]*event.values[1] + set[2]*event.values[2];
            double mag = Math.sqrt(set[0] * set[0] + set[1] * set[1] + set[2] * set[2]) *
                    Math.sqrt(event.values[0] * event.values[0] + event.values[1] * event.values[1] + event.values[2] * event.values[2]);
            label.setText(String.format("%.3f", Math.acos(dot/mag)*360/(2*Math.PI)));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
