package com.example.lab04_204_10;

//public class SensorSteps {
//
//}

import java.util.ArrayList;
import java.util.List;

import mapper.InterceptPoint;
import mapper.MapView;
import mapper.NavigationalMap;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SensorSteps implements SensorEventListener {
	TextView Output, orderView;
	public float max = 0;
	public float min = 0;
	//LineGraphView graph;
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
	MapView mv;
	PointF currentLoc;
	static final float STEP_LENGHT = (float) 0.5; 
	NavigationalMap wall;
	List<PointF> pathPoints = new ArrayList<PointF>();
	static final float AVOID_WALL = (float) 0.5;
	PointF origin = new PointF();
	PointF destination = new PointF();
	int i=0, p=1;
	int firstTime=0;
	PointF previousUserPoint = new PointF();
	PointF ppreviousUserPoint = new PointF();
	List<PointF> previousP = new ArrayList<PointF>();

	
	public SensorSteps(TextView outputView, MapView mapview, TextView order, NavigationalMap map) {
		wall=map;
		Output = outputView;
		orderView = order;
		mv = mapview;
		previousP.add(new PointF(0,0));
		previousP.add(new PointF(0,0));
		previousP.add(new PointF(0,0));
	}

	public void onAccuracyChanged(Sensor s, int i) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
	if(stepCounter<2 && mv.getOriginPoint().equals(0,0)==false && mv.getDestinationPoint().equals(0,0)==false && firstTime==0){
		firstTime=1;
		origin = mv.getOriginPoint();
		destination = mv.getDestinationPoint();
		if(wall.calculateIntersections(mv.getOriginPoint(), mv.getDestinationPoint()).isEmpty()){
			straightPath(mv.getOriginPoint(), mv.getDestinationPoint());
		}else{
			path(mv.getOriginPoint(), mv.getDestinationPoint());
			eachStep();
		}
	}	

	
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
			 direcNS += Math.cos(direction[0]+0.44);//somar 45 graus
			 direcEW += Math.sin(direction[0]+0.44);
			 
//Walk just in straight lines toward North/South, East/West. This way the user's position can be updated more accurately
			 float angle = 0;
			 if(direction[0]+0.44 < 9 *Math.PI/4 && direction[0]+0.44 > 7*Math.PI/4)//north
				 angle = 0;
			 if(direction[0]+0.44 < Math.PI/4 && direction[0]+0.44 > -Math.PI/4)//north
				 angle = 0;
			 if(direction[0]+0.44 > Math.PI/4 && direction[0]+0.44 < 3*Math.PI/4)//east
				 angle = (float) (2*Math.PI/4);
			if(Math.abs(direction[0]+0.44) > 3*Math.PI/4 && Math.abs(direction[0]+0.44) < 5*Math.PI/4)//south
				 angle = (float) (Math.PI);
			 if(direction[0]+0.44 < -Math.PI/4 && direction[0]+0.44 > -3*Math.PI/4)//west
				 angle = (float) (6*Math.PI/4);
			 if(direction[0]+0.44 > 5*Math.PI/4 && direction[0]+0.44 < 7*Math.PI/4)//west
				 angle = (float) (3*Math.PI/2);

			 currentLoc = new PointF(0,0);
			 currentLoc.x = mv.getUserPoint().x+(float) (STEP_LENGHT*Math.sin(angle));
			 currentLoc.y = mv.getUserPoint().y-(float) (STEP_LENGHT*Math.cos(angle));	
			 if(i%3==0){
				 ppreviousUserPoint = previousUserPoint;
				 previousUserPoint = mv.getUserPoint();
				 i++;
			 }
			 mv.setUserPoint(currentLoc.x,currentLoc.y);
			 insideWall(previousUserPoint, ppreviousUserPoint);
			 eachStep();  
			 break;
		 }
		 }

	} // end onSensorChanged
	
	public void eachStep(){
		 if(firstTime==1){
			 if(p<pathPoints.size()){
				 if(arriveDestination(mv.getUserPoint(), pathPoints.get(p), p))
					 p++;
			}
		 }	 
	}
	
	public boolean arriveDestination(PointF currentPosition, PointF destination, int p){
		float x, y;
		x = currentPosition.x-destination.x;
		y = currentPosition.y-destination.y;
		if(x<=0.5 && y<=0.5){
			if(destination == mv.getDestinationPoint())
				orderView.setText("You arrived at your destination!");
			else{
				if(p>0)
					orderPath(currentPosition, destination);
			}
			return true;
		}else
			return false;
	}
	
	
	public void orderPath(PointF origin, PointF dest){
		double x, y, stepsX, stepsY, degrees, hypotenuse;
		x = dest.x - origin.x;
		y = dest.y - origin.y;
		stepsX = Math.abs(x/STEP_LENGHT);
		stepsY = Math.abs(y/STEP_LENGHT);
		String WestEast;
		String NorthSouth;
		if(x>=0) WestEast = "East"; else WestEast = "West";
		if(y>=0) NorthSouth = "South"; else NorthSouth = "North";
		orderView.setText("Walk "+stepsX+" steps toward "+WestEast+" and "+stepsY+" steps toward "+NorthSouth);
	}
	public void straightPath(PointF origin, PointF dest){
		pathPoints.add(origin);
		pathPoints.add(dest);
		mv.setUserPath(pathPoints);
		orderPath(origin, dest);
	}
	
//	public void path(PointF orig, PointF dest){
//		pathPoints.add(orig);
//		int choosenPoint1=5, choosenPoint2=5;
//		for(int i=0; i<4; i++){
//			if(wall.calculateIntersections(orig, point.get(i)).isEmpty()==true && choosenPoint1==5){
//				pathPoints.add(point.get(i));
//				choosenPoint1 = i;
//			}
//		}
//		if(wall.calculateIntersections(point.get(choosenPoint1), dest).isEmpty()==true){
//			pathPoints.add(dest);
//		}else{
//			for(int i=0; i<4; i++){
//				if(wall.calculateIntersections(dest, point.get(i)).isEmpty()==true && choosenPoint2==5){
//					pathPoints.add(point.get(i));
//					choosenPoint2 = i;
//				}
//			}
//			pathPoints.add(dest);
//		}
//		mv.setUserPath(pathPoints);
//	}
	
	public void path(PointF orig, PointF dest){
		pathPoints.add(orig);		
		PointF np1 = new PointF(orig.x, 9.5f);
		
		if(wall.calculateIntersections(orig, np1).isEmpty()==true)
			pathPoints.add(np1);
		if(wall.calculateIntersections(np1, dest).isEmpty()==true){
			pathPoints.add(dest);
		}else{
			PointF np2 = new PointF(dest.x, 9.5f);
			if(wall.calculateIntersections(dest, np2).isEmpty()==true)
				pathPoints.add(np2);
			}
			pathPoints.add(dest);
		mv.setUserPath(pathPoints);
	}
	
	void insideWall(PointF previousPoint, PointF ppreviousPoint){
		if( wall.calculateIntersections(previousPoint, mv.getUserPoint()).isEmpty()==false || wall.calculateIntersections(ppreviousPoint, mv.getUserPoint()).isEmpty()==false){
			//Toast.makeText(null , "Wall", Toast.LENGTH_LONG).show();
			mv.setUserPoint(previousPoint);
		}
		float x = mv.getUserPoint().x;
		float y = mv.getUserPoint().y;
//		if(firstTime==1){
//		if(x<2.5 || y<2.5 || y>10 || x>15)
//			//Toast.makeText(null , "Wall", Toast.LENGTH_LONG).show();
//		}
//		if((4.5<x && x<6.5)&&(8.2>y && y>2.5))
//			//Toast.makeText(null , "Wall", Toast.LENGTH_LONG).show();
//		if((8.2<x && x<10.5)&&(8.2>y && y>2.5))
//			//Toast.makeText(null , "Wall", Toast.LENGTH_LONG).show();
//		if((12.5<x && x<14.5)&&(8.2>y && y>2.5))
//		//	Toast.makeText(null , "Wall", Toast.LENGTH_LONG).show();
	}

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
		i=0; p=1;
		firstTime=0;
		mv.setOriginPoint(new PointF(0,0));
		mv.setDestinationPoint(new PointF(0,0));
		pathPoints.clear();
	}

}