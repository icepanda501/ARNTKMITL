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
	
	private Paint paint = new Paint();
	int xScale = 11;
	int yScale = 11;
	public int x;
	public int y;
	int visible = -1;
	int error_point = 0;
	int color;
	public PositioningView(Context context,int color) {
		super(context);
		this.color = color;
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		paint.setColor(color);
		canvas.drawCircle(x*xScale+error_point, y*yScale+error_point, 15, paint);
	}
	
	public void setColor(int color){
		paint.setColor(color);
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
