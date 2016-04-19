package com.example.finalproject;

import java.util.ArrayList;
import java.util.LinkedList;

import com.example.finalproject.graphics.Model3D;
import com.example.finalproject.shottestpath.Edge;
import com.example.finalproject.shottestpath.FloorMap;
import com.example.finalproject.shottestpath.Vertex;
import com.example.finalproject.view.PathView;
import com.example.finalproject.view.PositioningView;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import edu.dhbw.andar.interfaces.MarkerVisibilityListener;

public class MarkerListenerTest implements MarkerVisibilityListener{
	
	private Model3D model3d;
	private FloorMap floormap;
	private Vertex node;
	private Context context;
	private LinkedList<Vertex> nextNode;
	public MarkerListenerTest(Model3D model3d,FloorMap floorMap,Context context){
		this.model3d = model3d;
		this.floormap = floorMap;
		this.node = model3d.getNode();
		this.context = context;
	}

	@Override
	public void makerVisibilityChanged(boolean visible) {
		if(visible){
			if(model3d.isVisible()){
					Log.i("PATTERN NAME : ",model3d.getPatternName()+" is Visible" + " NumberNode : "+node.getNumber()+" End Node = "+floormap.getEndNode());
					
					
					((Activity) context).runOnUiThread(new Runnable(){
						   public void run(){
							   floormap.currentPosition(node);
						
						   }
					});
					nextNode = floormap.drawPath(node);
					if(nextNode != null){
						model3d.setScale(80f);
						float angle = (float) Math.toDegrees(Math.atan2(floormap.getEndNode().getX() - node.getX(), floormap.getEndNode().getY() - node.getY()));
						
//						Edge edge = floormap.findTheWay(node.getFloor(), node.getNumber(), nextNode.getNumber());
//						if(edge != null){
//							model3d.setYRotat(edge.getAngle());
//						}
						
						Log.i("Angle between points", "angle : "+angle);
						model3d.setYRotat((int) angle+node.getAngle());
						
					}else{
						model3d.setScale(0);
						((Activity) context).runOnUiThread(new Runnable(){
							   public void run(){
								   		floormap.showMap();
								   }
							});
						
					}
			}
			
		}
		
	}

}
