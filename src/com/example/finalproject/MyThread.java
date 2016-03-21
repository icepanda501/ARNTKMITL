package com.example.finalproject;

import java.util.ArrayList;

import android.util.Log;

import com.example.finalproject.graphics.Model3D;
import com.example.finalproject.view.PositioningView;

import edu.dhbw.andar.interfaces.MarkerVisibilityListener;




public class MyThread extends Thread{
	
	
	private Model3D model3d;
	private ArrayList<Model3D> objModel3d;
	private PositioningView position;
	private boolean running = true;
	 
	
	public MyThread(ArrayList<Model3D> objModel3d, PositioningView position) {
		this.objModel3d = objModel3d;
		this.position = position;
		
		setDaemon(true);
		start();
	}
	
	



	@Override
	public synchronized void run() {
		super.run();
		setName("Navigat Thread");

		yield();

	
		while(running) {
//			model3d = objModel3d.get(0);
//			if(model3d.isVisible()){
//				
//				position.setPosition(290,200);
//				
//			}
//			Log.i("PATTERN NAME : ","ok");
			for(Model3D objModel : objModel3d){
				if(objModel.isVisible()){
					position.setPosition(290,200);
					Log.i("PATTERN NAME : ",objModel.getPatternName());
				}
			}
	
			yield();
		}
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
