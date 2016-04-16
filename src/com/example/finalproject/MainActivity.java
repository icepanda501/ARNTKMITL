package com.example.finalproject;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.finalproject.graphics.LightingRenderer;
import com.example.finalproject.graphics.Model3D;
import com.example.finalproject.models.Model;

import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.exceptions.AndARException;
import edu.dhbw.andar.pub.CustomObject;
import edu.dhbw.andar.pub.CustomRenderer;
import com.example.finalproject.MainActivity;
import com.example.finalproject.Config;
import com.example.finalproject.MainActivity.ModelLoader;
import com.example.finalproject.MainActivity.TakeAsyncScreenshot;
import com.example.finalproject.MainActivity.TouchEventHandler;
import com.example.finalproject.parser.ObjParser;
import com.example.finalproject.parser.ParseException;
import com.example.finalproject.parser.Util;
import com.example.finalproject.shottestpath.DijkstraAlgorithm;
import com.example.finalproject.shottestpath.Edge;
import com.example.finalproject.shottestpath.FloorMap;
import com.example.finalproject.shottestpath.Graph;
import com.example.finalproject.shottestpath.Vertex;
import com.example.finalproject.util.AssetsFileUtil;
import com.example.finalproject.util.BaseFileUtil;
import com.example.finalproject.util.SDCardFileUtil;
import com.example.finalproject.view.CustomView;
import com.example.finalproject.view.FloorMapView;
import com.example.finalproject.view.LineFocusCamera;
import com.example.finalproject.view.MapView;
import com.example.finalproject.view.PathView;
import com.example.finalproject.view.PositioningView;
import com.example.finalproject.view.RightTriangle;

import edu.dhbw.andopenglcam.R;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.text.Layout;
import android.transition.ChangeBounds;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;

import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AndARActivity implements SurfaceHolder.Callback{
	/**
	 * View a file in the assets folder
	 */
	public static final int TYPE_INTERNAL = 0;
	/**
	 * View a file on the sd card.
	 */
	public static final int TYPE_EXTERNAL = 1;
	
	public static final boolean DEBUG = false;
	
	/* Menu Options: */
	private final int MENU_SCALE = 0;
	private final int MENU_ROTATE = 1;
	private final int MENU_TRANSLATE = 2;
	private final int MENU_SCREENSHOT = 3;
	
	private int mode = MENU_SCALE;
	private PositioningView position;
	private PathView pathview;
	
	private ArrayList<Model3D> objModel3d = new ArrayList<Model3D>();
	private ArrayList<MarkerListenerTest> markerListenners = new ArrayList<MarkerListenerTest>();
	private Model model;
	private ProgressDialog waitDialog;
	private Resources res;
	private Button btest;
	ARToolkit artoolkitForFloor1;
	private int switchmap = -1;
	private FloorMap floormap;
	private FloorMapView mapview;
	private RightTriangle rightTriangle;
	private RightTriangle leftTriangle;
	public MainActivity() {
		super(false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setNonARRenderer(new LightingRenderer());//or might be omited
		res=getResources();
		artoolkitForFloor1 = getArtoolkit();		
		getSurfaceView().setOnTouchListener(new TouchEventHandler());
		getSurfaceView().getHolder().addCallback(this);
		
		MapView mapbutton = new MapView(this);
		mapview = new FloorMapView(this);
		mapview.setFloorNum(1);
		mapview.hide();
        FrameLayout.LayoutParams floorParams = new FrameLayout.LayoutParams(550,550);
        floorParams.leftMargin = 400;
        floorParams.topMargin = 150;
        
        
		rightTriangle = new RightTriangle(this);
		FrameLayout.LayoutParams rightTriangleParams = new FrameLayout.LayoutParams(100,100);
		rightTriangleParams.leftMargin = 1000;
		rightTriangleParams.topMargin = 350;
		leftTriangle = new RightTriangle(this);
		leftTriangle.setRotation(180);
		FrameLayout.LayoutParams leftTriangleParams = new FrameLayout.LayoutParams(100,100);
		leftTriangleParams.leftMargin = 250;
		leftTriangleParams.topMargin = 350;
		rightTriangle.setVisibility(switchmap);
		leftTriangle.setVisibility(switchmap);
		
		
        FrameLayout.LayoutParams searchParams = new FrameLayout.LayoutParams(300,100);
        searchParams.leftMargin = 400;
        
        
        FrameLayout.LayoutParams mapParams = new FrameLayout.LayoutParams(300, 150);
        SearchView input = new SearchView(this);
        mapParams.leftMargin = 1000;
        mapParams.topMargin = 550;
        FrameLayout.LayoutParams inputParams = new FrameLayout.LayoutParams(600,100);
        input.setLayoutParams(inputParams);
        inputParams.leftMargin = 700;
        inputParams.topMargin = 0;
        input.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        float[] roundedCorner = new float[] { 5, 5, 5, 5, 5, 5, 5, 5 };
        CustomBorderDrawable gd = new CustomBorderDrawable(new RoundRectShape(roundedCorner, null, null));
        gd.setFillColour(0xFF00FFFF);
        LineFocusCamera line = new LineFocusCamera(this);				  
        FrameLayout.LayoutParams lineParams = new FrameLayout.LayoutParams(400, 300);
        lineParams.leftMargin = 450;
        lineParams.topMargin = 200;
        
        input.setBackgroundColor(0xFF00FFFF);
        mapview.setBackgroundColor(Color.GREEN);
		position = new PositioningView(this);
		position.setVisibility(switchmap);
        pathview = new PathView(this);
        pathview.setVisibility(switchmap);
        
        
        try {
			JSONObject json_obj = new JSONObject(loadJSONFromAsset("json/data.json"));
			
			
			floormap = new FloorMap(pathview,position,mapview,leftTriangle,rightTriangle,json_obj);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        leftTriangle.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				floormap.leftMap();
				
			}
        	
        });
        
        rightTriangle.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				floormap.rightMap();
				
			}
        	
        });
        
        mapbutton.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				
				if(mapview.isVisible()){
					
					floormap.hideMap();
				}else{
					floormap.showMap();
				}
			}
        });
        
        
        getFrame().addView(mapbutton,mapParams);
        getFrame().addView(line,lineParams);
//        getFrame().addView(input);
        getFrame().addView(mapview,floorParams);
        getFrame().addView(position,floorParams);
        getFrame().addView(pathview,floorParams);
        getFrame().addView(rightTriangle,rightTriangleParams);
        getFrame().addView(leftTriangle,leftTriangleParams);
        
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        ll.setId(12345);

        TestFragment fragobj= TestFragment.newInstance("");
        getFragmentManager().beginTransaction().add(ll.getId(), fragobj).commit();
        fragobj.setFloorMap(floormap);
        
        getFrame().addView(ll);
//        getFrame().addView(search,searchParams);

        
//        changeRotation();
//        getFrame().addView(input,params);
        
	}
	
	public String loadJSONFromAsset(String path) {
        String json = null;
        try {
            InputStream is = getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


	/**
	 * Inform the user about exceptions that occurred in background threads.
	 */
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		System.out.println("");
	}
	

    /* create the menu
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//    	menu.add(0, MENU_TRANSLATE, 0, res.getText(R.string.translate))
//		.setIcon(R.drawable.translate);
//    	menu.add(0, MENU_ROTATE, 0, res.getText(R.string.rotate))
//    	.setIcon(R.drawable.rotate);
//    	menu.add(0, MENU_SCALE, 0, res.getText(R.string.scale))
//    	.setIcon(R.drawable.scale);     
//    	menu.add(0, MENU_SCREENSHOT, 0, res.getText(R.string.take_screenshot))
//		.setIcon(R.drawable.screenshoticon);     
    	

    	    
        return true;
    }
    
    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case MENU_SCALE:
	            mode = MENU_SCALE;
	            return true;
	        case MENU_ROTATE:
	        	mode = MENU_ROTATE;
	            return true;
	        case MENU_TRANSLATE:
	        	mode = MENU_TRANSLATE;
	            return true;
	        case MENU_SCREENSHOT:
	        	new TakeAsyncScreenshot().execute();
	        	return true;
        }
        return false;
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	super.surfaceCreated(holder);
    	//load the model
    	//this is done here, to assure the surface was already created, so that the preview can be started
    	//after loading the model
    	if(model == null) {
			waitDialog = ProgressDialog.show(this, "", 
	                getResources().getText(R.string.app_name), true);
			waitDialog.show();
			new ModelLoader().execute();
		}
    }
    
	
    /**
     * Handles touch events.
     * @author Tobias Domhan
     *
     */
    class TouchEventHandler implements OnTouchListener {
    	
    	private float lastX=0;
    	private float lastY=0;

		/* handles the touch events.
		 * the object will either be scaled, translated or rotated, dependen on the
		 * current user selected mode.
		 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
		 */
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(model!=null) {
				switch(event.getAction()) {
					//Action started
					default:
					case MotionEvent.ACTION_DOWN:
						lastX = event.getX();
						lastY = event.getY();
						break;
					//Action ongoing
					case MotionEvent.ACTION_MOVE:
						float dX = lastX - event.getX();
						float dY = lastY - event.getY();
						lastX = event.getX();
						lastY = event.getY();
						if(model != null) {
							switch(mode) {
								case MENU_SCALE:
									model.setScale(dY/100.0f);
						            break;
						        case MENU_ROTATE:
						        	model.setXrot(-1*dX);//dY-> Rotation um die X-Achse
									model.setYrot(-1*dY);//dX-> Rotation um die Y-Achse
						            break;
						        case MENU_TRANSLATE:
						        	model.setXpos(dY/10f);
									model.setYpos(dX/10f);
						        	break;
							}		
						}
						break;
					//Action ended
					case MotionEvent.ACTION_CANCEL:	
					case MotionEvent.ACTION_UP:
						lastX = event.getX();
						lastY = event.getY();
						break;
				}
			}
			return true;
		}
    	
    }
    
	public class ModelLoader extends AsyncTask<Void, Void, Void> {
		      
		
    	@Override
    	protected Void doInBackground(Void... params) {
    		
			Intent intent = getIntent();
			Bundle data = intent.getExtras();

			int type = TYPE_INTERNAL;
			String modelFileName = "seriousarrow.obj";
//			int type = data.getInt("type");
//			String modelFileName = data.getString("name");
//			Log.i("info",modelFileName);
			BaseFileUtil fileUtil= null;
			File modelFile=null;
			switch(type) {
			case TYPE_EXTERNAL:
				fileUtil = new SDCardFileUtil();
				modelFile =  new File(URI.create(modelFileName));
				modelFileName = modelFile.getName();
				fileUtil.setBaseFolder(modelFile.getParentFile().getAbsolutePath());
				break;
			case TYPE_INTERNAL:
				fileUtil = new AssetsFileUtil(getResources().getAssets());
				fileUtil.setBaseFolder("models/");
				break;
			}
			
			//read the model file:						
			if(modelFileName.endsWith(".obj")) {
				ObjParser parser = new ObjParser(fileUtil);
				try {
					if(Config.DEBUG)
						Debug.startMethodTracing("AndObjViewer");
					if(type == TYPE_EXTERNAL) {
						//an external file might be trimmed
						BufferedReader modelFileReader = new BufferedReader(new FileReader(modelFile));
						String shebang = modelFileReader.readLine();				
						if(!shebang.equals("#trimmed")) {
							//trim the file:			
							File trimmedFile = new File(modelFile.getAbsolutePath()+".tmp");
							BufferedWriter trimmedFileWriter = new BufferedWriter(new FileWriter(trimmedFile));
							Util.trim(modelFileReader, trimmedFileWriter);
							if(modelFile.delete()) {
								trimmedFile.renameTo(modelFile);
							}					
						}
					}
					if(fileUtil != null) {
						BufferedReader fileReader = fileUtil.getReaderFromName(modelFileName);
						if(fileReader != null) {
							model = parser.parse("Model", fileReader);
							
							String [] filenames = getAssets().list("");
							for(String filename : filenames){
								objModel3d.add(new Model3D(model,filename));
								Log.i("FILENAME",""+filename);
							}
							
							///////////////////ADD NODE to MARKER/////////////////////////////////
							int index = 0;
							for(int i=1;i<=6;i++){
								List<Vertex> nodes = floormap.getNodes(i);
								for(Vertex node : nodes){
									if(node.isMarker()){
										objModel3d.get(index).setNode(node);
				    					try {
											artoolkitForFloor1.registerARObject(objModel3d.get(index));
										} catch (AndARException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
				    					markerListenners.add(new MarkerListenerTest(objModel3d.get(index),floormap));
				    					artoolkitForFloor1.addVisibilityListener(markerListenners.get(index));
										index++;
									}
								}
								
							}
							/////////////////////////////////////////////////////////////////////////
							
							
							
						}
					}
					if(Config.DEBUG)
						Debug.stopMethodTracing();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
    		return null;
    	}
    	@Override
    	protected void onPostExecute(Void result) {
    		super.onPostExecute(result);
    		waitDialog.dismiss();
    		
    		int is = 0;
			if(objModel3d!=null)
				for(Model3D objModel : objModel3d){
//    					artoolkitForFloor1.registerARObject(objModel);
//    					markerListenners.add(new MarkerListenerTest(objModel,position,pathview,floormap,startNode,endNode));
//    					artoolkitForFloor1.addVisibilityListener(markerListenners.get(is));
//    					Log.i("PATNAME",""+objModel.getPatternName()+" coordinate :"+objModel.getNode().getX()+","+objModel.getNode().getY());
					is++;
				}
			startPreview();
    	}
    }
	
	class TakeAsyncScreenshot extends AsyncTask<Void, Void, Void> {
		
		private String errorMsg = null;

		@Override
		protected Void doInBackground(Void... params) {
			Bitmap bm = takeScreenshot();
			FileOutputStream fos;
			try {
				fos = new FileOutputStream("/sdcard/AndARScreenshot"+new Date().getTime()+".png");
				bm.compress(CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();					
			} catch (FileNotFoundException e) {
				errorMsg = e.getMessage();
				e.printStackTrace();
			} catch (IOException e) {
				errorMsg = e.getMessage();
				e.printStackTrace();
			}	
			return null;
		}
		
		protected void onPostExecute(Void result) {
			if(errorMsg == null)
				Toast.makeText(MainActivity.this, getResources().getText(R.string.screenshotsaved), Toast.LENGTH_SHORT ).show();
			else
				Toast.makeText(MainActivity.this, getResources().getText(R.string.screenshotfailed)+errorMsg, Toast.LENGTH_SHORT ).show();
		};
		
	}
	public class CustomBorderDrawable extends ShapeDrawable {
	    private Paint fillpaint, strokepaint;
	    private static final int WIDTH = 3; 

	    public CustomBorderDrawable(Shape s) {
	        super(s);
	        fillpaint = this.getPaint();
	        strokepaint = new Paint(fillpaint);
	        strokepaint.setStyle(Paint.Style.STROKE);
	        strokepaint.setStrokeWidth(WIDTH);
	        strokepaint.setARGB(255, 0, 0, 0);
	    }

	    @Override
	    protected void onDraw(Shape shape, Canvas canvas, Paint fillpaint) {
	        shape.draw(canvas, fillpaint);
	        shape.draw(canvas, strokepaint);
	    }

	    public void setFillColour(int c){
	        fillpaint.setColor(c);
	    }
	}
	

	
}
