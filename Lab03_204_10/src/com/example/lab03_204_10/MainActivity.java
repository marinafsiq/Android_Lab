package com.example.lab03_204_10;

import java.io.File;
import java.util.Arrays;

import mapper.MapLoader;
import mapper.MapView;
import mapper.NavigationalMap;

//import ca.uwaterloo.lab03_204_10.AccelerometerSensorEventListener;
//import ca.uwaterloo.lab03_204_10.MagneticFieldSensorEventListener;

//import ca.uwaterloo.lab02_204_10.AccelerometerSensorEventListener;
//import ca.uwaterloo.lab02_204_10.LineGraphView;
//import ca.uwaterloo.lab02_204_10.R;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

TextView tv, tv1, tv2;
LinearLayout layout1;
TextView acceleOutput, maxAc, orientationView;
LineGraphView graph;
float smoothedAccel;
float [] in;
Button resetButton;
MapView mv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv = (TextView) findViewById(R.id.label1);
		tv.setText("    Accelerometer Graph");
		
		layout1 = (LinearLayout) findViewById(R.id.view1);
		layout1.setOrientation(LinearLayout.VERTICAL);
		
		smoothedAccel = 0;		
//---------------------------Map-----------------------------
		mv = new MapView(getApplicationContext(), 600, 600, 34, 34);
		registerForContextMenu(mv);
		NavigationalMap map = MapLoader.loadMap(getExternalFilesDir(null), "Lab-room-peninsula.svg");
		mv.setMap(map);
		layout1.addView(mv);
//--------------------Accelerometer sensor------------------------------
		graph = new LineGraphView(getApplicationContext(),100, Arrays.asList("low_z"));
		layout1.addView(graph);
		graph.setVisibility(View.VISIBLE);
		acceleOutput = new TextView(getApplicationContext());
		layout1.addView(acceleOutput);
		
		
		SensorManager accelSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor accelSensor = accelSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		SensorEventListener acSen = new AccelerometerSensorEventListener(graph, acceleOutput);
		accelSensorManager.registerListener(acSen, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);	
		
		addListenerOnButton(acSen);

		
//--------------------Magnetic Field sensor------------------------------
		
		Sensor mfSensor = accelSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		SensorEventListener mfSen = new AccelerometerSensorEventListener(graph, acceleOutput);
		accelSensorManager.registerListener(mfSen, mfSensor, SensorManager.SENSOR_DELAY_FASTEST);
		
		Sensor acSensor = accelSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		accelSensorManager.registerListener(mfSen, acSensor, SensorManager.SENSOR_DELAY_FASTEST);
		
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void addListenerOnButton(final SensorEventListener acSen) {
		resetButton = (Button) findViewById(R.id.button1);
		resetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((AccelerometerSensorEventListener)acSen).reset();
			}
		});
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	super.onCreateContextMenu(menu, v, menuInfo);
	mv.onCreateContextMenu(menu, v, menuInfo); }
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	return super.onContextItemSelected(item) || mv.onContextItemSelected(item); }


}
