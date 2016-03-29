package com.example.finalproject.view;

import com.example.finalproject.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class FloorMapView extends View {
	
	Paint paint = new Paint();
	int mXpos = 10;
	int mYpos = 10;
	int visible = -1;
	public FloorMapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressLint("DrawAllocation") 
	
	protected void onDraw(Canvas canvas) {
		
		Bitmap map = BitmapFactory.decodeResource(getResources(), R.drawable.floor1);
		map = Bitmap.createScaledBitmap(map, 550, 550, true);
		canvas.drawBitmap(map, 0, 0,paint);

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
