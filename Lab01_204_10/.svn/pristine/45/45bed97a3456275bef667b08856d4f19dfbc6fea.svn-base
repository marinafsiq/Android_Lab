package ca.uwaterloo.lab01_204_10;

import java.util.Arrays;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
TextView tv, tv1, tv2;
LinearLayout layout1;
TextView lightOutput, acceleOutput, mfOutput, rvOutput, maxAc, maxL, maxMF, maxRV;
LineGraphView graph;
float xa, ya, za, l, xr, yr, zr, xm, ym, zm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv = (TextView) findViewById(R.id.label1);
		tv.setText("    Accelerometer Graph");
		
		layout1 = (LinearLayout) findViewById(R.id.view1);
		layout1.setOrientation(LinearLayout.VERTICAL);
		
		xa = ya = za = l = xr = yr = zr = xm = ym = zm =0;		
		
//--------------------Accelerometer sensor------------------------------
		graph = new LineGraphView(getApplicationContext(),100, Arrays.asList("x", "y", "z"));
		layout1.addView(graph);
		graph.setVisibility(View.VISIBLE);
		acceleOutput = new TextView(getApplicationContext());
		maxAc = new TextView(getApplicationContext());
		layout1.addView(maxAc);
		layout1.addView(acceleOutput);
		
		SensorManager accelSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor accelSensor = accelSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		SensorEventListener acSen = new AccelerometerSensorEventListener(graph, acceleOutput, xa, ya, za, maxAc);
		accelSensorManager.registerListener(acSen, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
		
		xa = ((AccelerometerSensorEventListener) acSen).x;
		ya = ((AccelerometerSensorEventListener) acSen).y;
		za = ((AccelerometerSensorEventListener) acSen).z;
		
//--------------------Light sensor------------------------------
		lightOutput = new TextView(getApplicationContext());
		maxL = new TextView(getApplicationContext());
		layout1.addView(maxL);
		layout1.addView(lightOutput);

		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); //requesting sensor manager
		Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //requesting the light sensor
		SensorEventListener lSen = new LightSensorEventListener(lightOutput, l, maxL); //instance of your new SensorEventListener 
		sensorManager.registerListener(lSen, lightSensor, SensorManager.SENSOR_DELAY_NORMAL); //register it
		
		l = ((AccelerometerSensorEventListener) acSen).x;
	
//--------------------Magnetic Field sensor------------------------------
		mfOutput = new TextView(getApplicationContext());
		maxMF = new TextView(getApplicationContext());
		layout1.addView(maxMF);
		layout1.addView(mfOutput);
		
		SensorManager mfSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor mfSensor = mfSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		SensorEventListener mfSen = new MagneticFieldSensorEventListener(mfOutput, xm, ym, zm, maxMF);
		mfSensorManager.registerListener(mfSen, mfSensor, SensorManager.SENSOR_DELAY_NORMAL);
		
		xm = ((AccelerometerSensorEventListener) acSen).x;
		ym = ((AccelerometerSensorEventListener) acSen).y;
		zm = ((AccelerometerSensorEventListener) acSen).z;
		
//--------------------Rotation Vector sensor------------------------------
		rvOutput = new TextView(getApplicationContext());
		maxRV = new TextView(getApplicationContext());
		layout1.addView(maxRV);
		layout1.addView(rvOutput);
		
		SensorManager rvSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor rvSensor = rvSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		SensorEventListener rvSen = new RotationVectorSensorEventListener(rvOutput, xr, yr, zr, maxRV);
		rvSensorManager.registerListener(rvSen, rvSensor, SensorManager.SENSOR_DELAY_NORMAL);
		
		xr = ((AccelerometerSensorEventListener) acSen).x;
		yr = ((AccelerometerSensorEventListener) acSen).y;
		zr = ((AccelerometerSensorEventListener) acSen).z;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
