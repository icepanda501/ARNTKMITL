package com.example.finalproject;

import java.util.ArrayList;
import java.util.LinkedList;

import com.example.finalproject.graphics.Model3D;
import com.example.finalproject.shottestpath.FloorMap;
import com.example.finalproject.shottestpath.Vertex;
import com.example.finalproject.view.PathView;
import com.example.finalproject.view.PositioningView;

import android.util.Log;
import android.widget.Toast;
import edu.dhbw.andar.interfaces.MarkerVisibilityListener;

public class MarkerListenerTest implements MarkerVisibilityListener{
	
	private Model3D model3d;
	private ArrayList<Model3D> objModel3d;
	private PositioningView position;
	private FloorMap floormap;
	private PathView pathview;
	private Vertex node;
	private LinkedList<Vertex> linklist;
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
					position.setPosition(node.getX(),node.getY());
					floormap.drawPosition(node.getX(), node.getY());
					floormap.drawPath(node.getFloor(), node.getNumber(), floormap.getEndNode());
					Log.i("Linklist",linklist.toString());			
					pathview.setPath(linklist);
				
			}
			
		}
		
	}

}
