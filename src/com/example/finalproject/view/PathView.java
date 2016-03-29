package com.example.finalproject.view;

import java.util.LinkedList;

import com.example.finalproject.shottestpath.Vertex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class PathView extends View{
	
	Paint paint = new Paint();
	int xScale = 25;
	int yScale = 25;
	int visible = -1;
	LinkedList<Vertex> path;
	
	public PathView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void setPath(LinkedList<Vertex> path){
		this.path = path;
	}
	@Override
	protected void onDraw(Canvas canvas) {
	    
		paint.setStrokeWidth(5);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		
		if(path != null){
		    for(int i=0;i<path.size()-1;i++){
		    	canvas.drawLine(path.get(i).getX()*xScale+13, path.get(i).getY()*yScale+13, path.get(i+1).getX()*xScale+13, path.get(i+1).getY()*yScale+13, paint);
		    }	
		}

		
		
	}
	
	public void show(){
		visible = 1;
		this.setVisibility(visible);
	}
	
	public void hide(){
		visible = -1;
		this.setVisibility(visible);
	}
	
	public boolean isVisible(){
		return visible == 1;
	}

}
