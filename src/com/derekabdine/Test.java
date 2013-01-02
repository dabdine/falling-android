/*
 * Copyright (c) 2012 Derek Abdine
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.derekabdine;

import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Test extends Activity {r
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	// Create our layout
	final LinearLayout fv = new LinearLayout(this);
	fv.setOrientation(LinearLayout.VERTICAL);

	// Create the textview (will be used for debugging x/y/z accel values)
	final TextView tv = new TextView(this);
	tv.setTextSize(30);
	fv.addView(tv);

	// Create the media players for the two sound files.
	final MediaPlayer mp = MediaPlayer.create(this, R.raw.ahh);
	final MediaPlayer mp2 = MediaPlayer.create(this, R.raw.phew);

	fv.addView(new Button(this) {
	    {
		setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
			System.exit(0);
		    }
		});

		setText("Exit");
	    }
	});

	// Set the content view as our TextView object.
	setContentView(fv);

	final SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);

	sm.registerListener(
		new SensorEventListener() {
		    public void onSensorChanged(SensorEvent event) {
			final float[] values = event.values;

			float x = Math.abs(values[SensorManager.DATA_X]), y = Math
				.abs(values[SensorManager.DATA_Y]), z = Math
				.abs(values[SensorManager.DATA_Z]);

			double mag = Math.sqrt(x * x + y * y + z * z);

			if (x < 3 && y < 3 && z < 3) {
			    synchronized (m_fallen) {
				if (!m_fallen) {
				    mp.seekTo(0);
				    mp.start();
				    m_fallen = true;
				}
			    }
			} else if (mag > 9) {
			    synchronized (m_fallen) {
				if (m_fallen) {
				    mp2.seekTo(0);
				    mp2.start();
				    m_fallen = false;
				}
			    }
			}
		    }

		    public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
		    }
		}, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		SensorManager.SENSOR_DELAY_FASTEST);
    }

    /** Signal to determine which sound to play */
    private Boolean m_fallen = false;
}
