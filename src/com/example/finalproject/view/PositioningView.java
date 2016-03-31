package com.example.finalproject.view;

import com.example.finalproject.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class PositioningView extends View{
	
	Paint paint = new Paint();
	int xScale = 11;
	int yScale = 11;
	int x;
	int y;
	int visible = -1;
	
	public PositioningView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
//		paint.setStrokeWidth(2);
		paint.setColor(Color.BLACK);
//		paint.setStyle(Paint.Style.STROKE);
		
		
//		for(int i=0; i<=550;i=i+25){
//			canvas.drawLine(i, 0, i, 550, paint);
//		}
//		for(int j=0; j<=550;j=j+25){
//			canvas.drawLine(0, j, 550, j, paint);
//		}
		
//        canvas.drawLine(0, 0, 100, 0, paint);
		canvas.drawCircle(x*xScale+15, y*yScale+15, 15, paint);
		
		

	}
	
	
	public void setPosition(int x,int y){
		
		this.x = x;
		this.y = y;
		
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
