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
	int floorNum;
	public FloorMapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressLint("DrawAllocation") 
	
	protected void onDraw(Canvas canvas) {
		
		Bitmap map = BitmapFactory.decodeResource(getResources(), floorNum);
		map = Bitmap.createScaledBitmap(map, 550, 550, true);
		canvas.drawBitmap(map, 0, 0,paint);

	}
	
	public void setFloorNum(int n){
		switch (n) {
		case 1:
			floorNum = R.drawable.floor1;
			break;
		case 2:
			floorNum = R.drawable.floor2;
			break;
		case 3:
			floorNum = R.drawable.floor3;
			break;
		case 4:
			floorNum = R.drawable.floor4;
			break;
		case 5:
			floorNum = R.drawable.floor5;
			break;
		case 6:
			floorNum = R.drawable.floor6;
			break;

		default:
			break;
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
