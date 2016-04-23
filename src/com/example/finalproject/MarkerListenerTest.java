package com.example.finalproject;

import java.util.ArrayList;
import java.util.LinkedList;

import com.example.finalproject.graphics.Model3D;
import com.example.finalproject.shottestpath.FloorMap;
import com.example.finalproject.shottestpath.Vertex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.exceptions.AndARException;
import edu.dhbw.andar.interfaces.MarkerVisibilityListener;

public class MarkerListenerTest implements MarkerVisibilityListener{
	
	private Model3D model3d;
	private FloorMap floormap;
	private Vertex node;
	private Context context;
	private LinkedList<Vertex> nextNode;
	private ARToolkit artoolkit;
	public MarkerListenerTest(Model3D model3d,FloorMap floorMap,Context context, ARToolkit artoolkit){
		this.model3d = model3d;
		this.floormap = floorMap;
		this.node = model3d.getNode();
		this.context = context;
		this.artoolkit = artoolkit;
	}

	@Override
	public void makerVisibilityChanged(boolean visible) {
		if(visible){
			if(model3d.isVisible()){
				
//					ArrayList<Model3D> models =  ListModel.getInstance().getModelsWithout(model3d);
					
					
					Log.i("PATTERN NAME : ",model3d.getPatternName()+" is Visible" + " NumberNode : "+node.getNumber()+" End Node = "+floormap.getEndNode());
					
					 
					((Activity) context).runOnUiThread(new Runnable(){
						   @SuppressLint("ShowToast") public void run(){
							   Toast.makeText(context, "Pattern : "+model3d.getPatternName(), Toast.LENGTH_SHORT).show();
							   floormap.currentPosition(node);
//							   Log.i("X,Y : "," NumberNode : "+node.getNumber()+" X  "+node.getX() +" Y "+node.getY());
						   }
					});
					nextNode = floormap.drawPath(node);
					
					if(nextNode != null){
						
						if(nextNode.isEmpty()){
							model3d.setXRotat(-90);
							model3d.setZRotat(0);
						}else{
							Log.i("LastNode","LastNode : "+nextNode.get(nextNode.size()-1));
							Vertex LastNode = nextNode.get(nextNode.size()-1);
							float angle = (float) Math.toDegrees(Math.atan2(LastNode.getX() - node.getX(), LastNode.getY() - node.getY()));
//							Log.i("Angle between points", "angle : "+angle);
							model3d.setYRotat((int) angle+node.getAngle());
						}
						model3d.setScale(80f);
						
						
//						Edge edge = floormap.findTheWay(node.getFloor(), node.getNumber(), nextNode.getNumber());
//						if(edge != null){
//							model3d.setYRotat(edge.getAngle());
//						}
						

						
					}else{
						model3d.setScale(0);
						((Activity) context).runOnUiThread(new Runnable(){
							   public void run(){
								   		floormap.showMap();
								   }
							});
						
					}
			}else{
				if(model3d.getRegis()){
					Log.i("Out pap","Out la na 1" + model3d.getPatternName());
//					artoolkit.unregisterARObject(model3d);
					model3d.setRegis(false);
				}
			}
			
		}
		Log.i("Don't see","Don't see that comming "+model3d.getPatternName());
//		else{
//			try {
//				if(!model3d.getRegis()){
//					model3d.setRegis(true);
//					Log.i("REGIS","Regis la naaaaa " + model3d.getPatternName());
//					artoolkit.registerARObject(model3d);
//
//					
//				}
//				
//			} catch (AndARException e) {
//				// TODO Auto-generated catch block
//				Log.e("HEY mun error","I sus kuy :"+model3d.getPatternName());
//				e.printStackTrace();
//			}
//		}
	}

}
