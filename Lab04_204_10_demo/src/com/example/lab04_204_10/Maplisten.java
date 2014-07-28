package com.example.lab04_204_10;
import android.graphics.PointF;
import android.widget.LinearLayout;
import android.widget.TextView;
import mapper.MapView;
import mapper.PositionListener;


public class Maplisten implements PositionListener {
	
	//LinearLayout l2;
	
	public Maplisten(){
		
	}

	@Override
	public void originChanged(MapView source, PointF loc) {
		//source.setOriginPoint(loc);
		//if(source.getUserPoint().x==0 && source.getUserPoint().y==0)
			source.setUserPoint(source.getOriginPoint());
	}

	@Override
	public void destinationChanged(MapView source, PointF dest) {
		//source.setDestinationPoint(dest);

	}
	
/*	public PointF mlGetOriginChanged(MapView source, PointF loc){
		return source.getOriginPoint();
	}*/

}
