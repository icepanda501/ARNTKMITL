package com.example.finalproject;

import java.util.ArrayList;
import java.util.LinkedList;

import com.example.finalproject.graphics.Model3D;
import com.example.finalproject.shottestpath.Edge;
import com.example.finalproject.shottestpath.FloorMap;
import com.example.finalproject.shottestpath.Vertex;
import com.example.finalproject.view.PathView;
import com.example.finalproject.view.PositioningView;

import android.util.Log;
import android.widget.Toast;
import edu.dhbw.andar.interfaces.MarkerVisibilityListener;

public class MarkerListenerTest implements MarkerVisibilityListener{
	
	private Model3D model3d;
	private FloorMap floormap;
	private Vertex node;
	public MarkerListenerTest(Model3D model3d,FloorMap floorMap){
		this.model3d = model3d;
		this.floormap = floorMap;
		this.node = model3d.getNode();
	}

	@Override
	public void makerVisibilityChanged(boolean visible) {
		if(visible){
			if(model3d.isVisible()){
					Log.i("PATTERN NAME : ",model3d.getPatternName()+" is Visible" + " NumberNode : "+node.getNumber()+" End Node = "+floormap.getEndNode());
					floormap.currentPosition(node);
					Vertex nextNode = floormap.drawPath(node.getFloor(), node.getNumber());
					if(nextNode != null){
						Edge edge = floormap.findTheWay(node.getFloor(), node.getNumber(), nextNode.getNumber());
						if(edge != null){
							model3d.setYRotat(edge.getAngle());
						}
					}
			}
			
		}
		
	}

}
