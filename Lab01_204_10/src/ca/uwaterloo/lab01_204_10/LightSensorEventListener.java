package ca.uwaterloo.lab01_204_10;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class LightSensorEventListener implements SensorEventListener{
	TextView loutput, maxOutput;
	public float x;
	
	
    public LightSensorEventListener(TextView outputView, float xx,TextView max){
    	loutput = outputView;
    	x = xx;
    	maxOutput = max;
    }
    
    public void onAccuracyChanged(Sensor s, int i) {}
    public void onSensorChanged(SensorEvent se) {
    	if (se.sensor.getType() == Sensor.TYPE_LIGHT) {
    		loutput.setText("Light Sensor:\nx = "+se.values[0]);
    		
    		if(Math.abs(x) <= Math.abs(se.values[0]) ){
				x = se.values[0];
			}
			maxOutput.setText("Record of Sensor Light:\nx = "+x);

    	}
    }
   

}

