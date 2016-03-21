package com.example.finalproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class LineFocusCamera extends View {
	
	Paint paint = new Paint();
	private int lineColor ;

	public LineFocusCamera(Context context) {
		super(context);
		
		lineColor = Color.WHITE;
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onDraw(Canvas canvas) {
		paint.setStrokeWidth(15f);
		paint.setColor(lineColor);
	    paint.setStyle(Paint.Style.STROKE);
	    paint.setStrokeJoin(Paint.Join.ROUND);
		canvas.drawLine(0, 0, 0, 100, paint);
		canvas.drawLine(0, 0, 100, 0, paint);
		
		canvas.drawLine(0, 300, 100, 300, paint);
		canvas.drawLine(0, 300, 0, 200, paint);
		
		canvas.drawLine(400, 300, 300, 300, paint);
		canvas.drawLine(400, 300, 400, 200, paint);
		
		canvas.drawLine(400, 0, 400, 100, paint);
		canvas.drawLine(400, 0, 300, 0, paint);
        
	}

}
