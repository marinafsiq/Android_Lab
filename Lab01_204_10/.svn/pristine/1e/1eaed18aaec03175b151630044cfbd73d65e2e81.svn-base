package ca.uwaterloo.lab01_204_10;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class MagneticFieldSensorEventListener implements SensorEventListener{
	TextView MFOutput, maxOutput;
	public float x, y, z;
	
	public MagneticFieldSensorEventListener(TextView outputView, float xx, float yy, float zz,TextView max){
		MFOutput = outputView;
		x = xx;
		y = yy;
		z = zz;
		maxOutput = max;
	}
	

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
			MFOutput.setText("Magnetic Field Sensor:\nx = "+event.values[0]+"\ny = "+event.values[1]+"\nz = "+event.values[2]);
		}
		
		if(Math.abs(x) <= Math.abs(event.values[0]) ){
			x = event.values[0];
		}
		if(Math.abs(y) <= Math.abs(event.values[1]) ){
			y = event.values[1];
		}
		if(Math.abs(z) <= Math.abs(event.values[2]) ){
			z = event.values[2];
		}
		maxOutput.setText("Record of Magnetic Field Sensor:\nx = "+x+"\ny = "+y+"\nz = "+z);
		
	}

}
