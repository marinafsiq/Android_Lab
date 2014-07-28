//package com.example.lab04_204_10;
//
//import android.os.Bundle;
//import android.app.Activity;
//import android.view.Menu;
//
//public class MainActivity extends Activity {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//}

package com.example.lab04_204_10;

import mapper.MapLoader;
import mapper.MapView;
import mapper.NavigationalMap;
import mapper.PositionListener;

import android.graphics.PointF;
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
import android.view.View.OnAttachStateChangeListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MainActivity extends Activity{

TextView tv, tv1, tv2, orderView, t;
LinearLayout layout1;
TextView acceleOutput, maxAc, orientationView;
float smoothedAccel;
float [] in;
Button resetButton;
static MapView mv;
Maplisten ml = null;
static final float STEP_LENGHT = (float) 0.6; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv = (TextView) findViewById(R.id.label1);
		tv.setText("    Dead Reckoning");
		
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

		acceleOutput = new TextView(getApplicationContext());
		layout1.addView(acceleOutput);
		
		orderView = new TextView(getApplicationContext());
		layout1.addView(orderView);
		orderView.setText("Instruction...");
		
		
		SensorManager accelSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor accelSensor = accelSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		SensorEventListener acSen = new SensorSteps(acceleOutput, mv, orderView, map);
		accelSensorManager.registerListener(acSen, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);	
		
		addListenerOnButton(acSen);

		
//--------------------Magnetic Field sensor------------------------------
		
		Sensor mfSensor = accelSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		//SensorEventListener mfSen = new SensorSteps(acceleOutput, mv);
		//accelSensorManager.registerListener(mfSen, mfSensor, SensorManager.SENSOR_DELAY_FASTEST);
		accelSensorManager.registerListener(acSen, mfSensor, SensorManager.SENSOR_DELAY_FASTEST);
		
		Sensor acSensor = accelSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		//accelSensorManager.registerListener(mfSen, acSensor, SensorManager.SENSOR_DELAY_FASTEST);
		accelSensorManager.registerListener(acSen, acSensor, SensorManager.SENSOR_DELAY_FASTEST);
		
		
//--------------------Interaction Map and Real world------------------------------
		
		
		ml = new Maplisten();
		mv.addListener(ml);
		t = new TextView(getApplicationContext());
		layout1.addView(t);
	}//end OnCreat
	
	
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
				((SensorSteps)acSen).reset();
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
	
	
	/*public void originChanged(MapView source, PointF loc) {
		source.setOriginPoint(loc);
		if(mv.getUserPoint().x == 0 && mv.getUserPoint().x == 0){
			mv.setUserPoint(loc);
		}
		updateValues(source);
		//t.setText("Inside straightPath\n\ndest: x= "+mv.getDestinationPoint().x+" y="+mv.getDestinationPoint().y+"\norigin: x= "+mv.getOriginPoint().x+" y="+mv.getOriginPoint().y+"\nuserPoint: x= "+source.getUserPoint().x+" y="+source.getUserPoint().y);
	}

	@Override
	public void destinationChanged(MapView source, PointF dest) {
		source.setDestinationPoint(dest);
		//straightPath(source);
		updateValues(source);

	}*/
	
	
	
	
	/*public void updateValues(MapView source){
		mv = source;
		NavigationalMap walls = new NavigationalMap();
		
		if(walls.calculateIntersections(mv.getOriginPoint(), mv.getDestinationPoint()).isEmpty()){
			straightPath(mv);
		}*/
		
		//t.setText("Update Values:\ndest: x= "+mv.getDestinationPoint().x+" y="+mv.getDestinationPoint().y+"\norigin: x= "+mv.getOriginPoint().x+" y="+mv.getOriginPoint().y+"\nuserPoint: x= "+source.getUserPoint().x+" y="+source.getUserPoint().y);
	
	

	

}

