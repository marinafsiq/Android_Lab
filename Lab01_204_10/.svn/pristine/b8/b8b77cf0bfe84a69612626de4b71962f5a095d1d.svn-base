package ca.uwaterloo.lab01_204_10;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class AccelerometerSensorEventListener implements SensorEventListener{
	TextView acOutput, maxOutput;
	public float x, y, z;
	LineGraphView graph;
	
	public AccelerometerSensorEventListener(LineGraphView outgraph, TextView outputView, float xx, float yy, float zz,TextView max){
		graph = outgraph;
		acOutput = outputView;
		x = xx;
		y = yy;
		z = zz;
		maxOutput = max;
	} 
	
	public void onAccuracyChanged(Sensor s, int i){}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)	{
			acOutput.setText("Accelerometer Sensor:\nx = "+event.values[0]+"\ny = "+event.values[1]+"\nz = "+event.values[2]);
			graph.addPoint(event.values);
			
			if(Math.abs(x) <= Math.abs(event.values[0]) ){
				x = event.values[0];
			}
			if(Math.abs(y) <= Math.abs(event.values[1]) ){
				y = event.values[1];
			}
			if(Math.abs(z) <= Math.abs(event.values[2]) ){
				z = event.values[2];
			}
			maxOutput.setText("Record of Accelerometer Sensor:\nx = "+x+"\ny = "+y+"\nz = "+z);
			
		}	
	}
	

}
