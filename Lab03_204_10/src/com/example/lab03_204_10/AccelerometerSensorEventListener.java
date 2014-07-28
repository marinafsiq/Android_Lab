package com.example.lab03_204_10;

//import java.util.Arrays;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

public class AccelerometerSensorEventListener implements SensorEventListener {
	TextView Output;
	public float max = 0;
	public float min = 0;
	LineGraphView graph;
	float smoothedAccel = 0;
	float maxLast = 0;
	float minLast = 0;
	float[] filterVec = new float[7];
	float[] in = new float[7];
	float[] maxVec = new float[10];
	float[] minVec = new float[10];
	int state = 0;
	int reading;
	int stepCounter = 0;
	static float a = (float) (0.5);
	static float[] vecGrav, vecMag;
	float[] direction = new float[3];
	static float direcNS, direcEW;

	public AccelerometerSensorEventListener(LineGraphView outgraph, TextView outputView) {
		graph = outgraph;
		Output = outputView;
	}

	public void onAccuracyChanged(Sensor s, int i) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

	// ---------------------DIRECTION-------------------------
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			vecGrav = event.values;
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			vecMag = event.values;

		if (vecGrav != null && vecMag != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, vecGrav, vecMag);
			if (success) {
				SensorManager.getOrientation(R, direction);
				//oriView.setText("Azimuth = " + direction[0]);
			}
		}

	//-----------------------END_DIRECTION--------------------
		
		 if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
	 //------------------------FILTER----------------------
		 for(int i=1; i < in.length-1; i++){
		 in[i-1] = in[i];
		 }
		 in[in.length-1] = event.values[2];
		 in = lowpass(in);
		
	//------------------------DIGITALIZING_THE_SIGNAL----------------------
		
		 if(maxLast >= in[in.length-1] ){
			 maxLast = in[in.length-1];
		 };
		
		 if(in[in.length-1]<0){
			 if(maxLast > 1){
				 for(int i=1; i<maxVec.length-1; i++){
					 maxVec[i-1] = maxVec[i];
				 }
				 maxVec[maxVec.length-1] = maxLast;
				 maxLast=0;
			 }
		 }
		 float sum = 0;
		 for(int i=0; i<maxVec.length; i++){
			 sum = maxVec[i] + sum;
		 }
		 max = Math.max(1, sum/maxVec.length);
		
		
		 if(minLast <= in[in.length-1] ){
			 minLast = in[in.length-1];
		 };
		
		 if(in[in.length-1]>0){
			 if(minLast < -1){
				 for(int i=1; i<maxVec.length-1; i++){
					 minVec[i-1] = minVec[i];
				 }
			 minVec[maxVec.length-1] = minLast;
			 minLast=0;
			 }
		 }
		 float sum1 = 0;
		 for(int i=0; i<minVec.length; i++){
			 sum1 = minVec[i] + sum1;
		 }
		 min = Math.min(-1, sum1/minVec.length);
		
		 if(in[in.length-1]>=max*0.7) reading = 1;
		 if(in[in.length-1]<=min*0.7) reading = -1;
		 if(in[in.length-1]<max*0.7 && in[in.length-1]>min*0.7) reading = 0;
				
		 float [] arrayPoints = new float[1];
		 arrayPoints[0] = in[in.length-1];
		 graph.addPoint(arrayPoints);
		
	 //------------------------STATE_MACHINE----------------------
		
		 switch(state){
		 case 0:
			 if(reading == 1)
				 state = 1;
			 else
				 state = 0;
			 break;
		 case 1:
			 if(reading == 0)
				 state = 2;
			 else
				 state = 1;
			 break;
		 case 2:
			 if(reading == -1)
				 state = 3;
			 else
				 state = 2;
			 break;
		 case 3:
			 if(reading == 0)
				 state = 4;
			 else
				 state = 3;
			 break;
		 case 4:
			 stepCounter++;
			 state = 0;
			 direcNS += Math.cos(direction[0]);
			 direcEW += Math.sin(direction[0]);
			 break;
		 }
		
		 Output.setText("---Steps Taken---\nSteps: "+stepCounter+"\n\n---Displacement (Steps)---\nNorth = "+direcNS+"\nEast = "+direcEW);
		 }

	} // end onSensorChanged

	float[] lowpass(float[] in) {
		filterVec[0] = 0;
		for (int i = 1; i < in.length; i++) {
			filterVec[i] = a * in[i] + (1 - a) * filterVec[i - 1];
		}
		return filterVec;
	}

	public void reset() {
		for (int i = 0; i < in.length; i++) {
			in[i] = 0;
		}
		stepCounter = 0;
		direcEW = 0;
		direcNS = 0;
	}

}
