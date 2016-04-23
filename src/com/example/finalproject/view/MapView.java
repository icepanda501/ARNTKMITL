package com.example.finalproject.view;

import com.example.finalproject.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MapView extends View {
	
	Paint paint = new Paint();
	int mXpos = 10;
	int mYpos = 10;
	
	public MapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressLint("DrawAllocation") @Override
	protected void onDraw(Canvas canvas) {
		paint.setColor(Color.BLACK);
		
		Bitmap map = BitmapFactory.decodeResource(getResources(), R.drawable.mapbutton);
		canvas.drawBitmap(map, mXpos, mYpos,paint);
	}

	
}
