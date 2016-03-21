package com.example.finalproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;





public class CustomView extends View {
	
	private Paint circlePaint;
	private String circleText;
	private int circleCol, labelCol;
	
	public CustomView(Context context) {
		super(context);
		
		circlePaint = new Paint();
		
		circleText = "";
		circleCol = 0xFF00FFFF;
		labelCol = Color.BLACK;
		// TODO Auto-generated constructor stub
	}

	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		circlePaint = new Paint();
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {

		//get half of the width and height as we are working with a circle
		int viewWidthHalf = this.getMeasuredWidth()/2;
		int viewHeightHalf = this.getMeasuredHeight()/2;
		//get the radius as half of the width or height, whichever is smaller
		//subtract ten so that it has some space around it
		int radius = 0;
		if(viewWidthHalf>viewHeightHalf)
			radius=viewHeightHalf-10;
		else
			radius=viewWidthHalf-10;
		
		//set drawing properties
		circlePaint.setStyle(Style.FILL);
		circlePaint.setAntiAlias(true);
		//set the paint color using the circle color specified
		circlePaint.setColor(circleCol);
		//draw the circle using the properties defined
		canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);

		//set the text color using the color specified
		circlePaint.setColor(labelCol);
		//set text properties
		circlePaint.setTextAlign(Paint.Align.CENTER);
		circlePaint.setTextSize(20);
		//draw the text using the string attribute and chosen properties
		canvas.drawText(circleText, viewWidthHalf, viewHeightHalf, circlePaint);
	}
	
	public int getCircleColor(){
		return circleCol;
	}

	/**
	 * Set the circle color
	 * @param newColor new color as an int
	 */
	public void setCircleColor(int newColor){
		//update the instance variable
		circleCol=newColor;
		//redraw the view
		invalidate();
		requestLayout();
	}

	/**
	 * Get the current text label color
	 * @return color as an int
	 */
	public int getLabelColor(){
		return labelCol;
	}

	/**
	 * Set the label color
	 * @param newColor new color as an int
	 */
	public void setLabelColor(int newColor){
		//update the instance variable
		labelCol=newColor;
		//redraw the view
		invalidate();
		requestLayout();
	}

	/**
	 * Get the current label text
	 * @return text as a string
	 */
	public String getLabelText(){
		return circleText;
	}

	/**
	 * Set the label text
	 * @param newLabel text as a string
	 */
	public void setLabelText(String newLabel){
		//update the instance variable
		circleText=newLabel;
		//redraw the view
		invalidate();
		requestLayout();
	}
}
