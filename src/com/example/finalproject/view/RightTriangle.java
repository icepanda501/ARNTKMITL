package com.example.finalproject.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.view.View;

public class RightTriangle extends View {
	int visible = -1;
	public RightTriangle(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressLint("DrawAllocation") @Override
	protected void onDraw(Canvas canvas) {
	    super.onDraw(canvas);
	    Paint paint = new Paint();

	    paint.setColor(android.graphics.Color.TRANSPARENT);
	    canvas.drawPaint(paint);

	    paint.setStrokeWidth(4);
	    paint.setColor(android.graphics.Color.RED);
	    paint.setStyle(Paint.Style.FILL_AND_STROKE);
	    paint.setAntiAlias(true);
	    Path path = new Path();
	    path.setFillType(FillType.EVEN_ODD);
	    path.lineTo(0, 100);
	    path.lineTo(87, 50);
	    path.lineTo(0, 0);
	    path.close();

	    canvas.drawPath(path, paint);
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
