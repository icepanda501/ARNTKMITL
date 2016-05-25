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
import com.example.finalproject.MainActivity;
import com.example.finalproject.Config;
import com.example.finalproject.MainActivity.ModelLoader;
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
import android.view.View.OnFocusChangeListener;
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
	private PositioningView goalPosition;
	private PathView pathview;
	
	private ArrayList<Model3D> objModel3d = new ArrayList<Model3D>();
	private ArrayList<MarkerListenerTest> markerListenners = new ArrayList<MarkerListenerTest>();
	private Model model;
	private ProgressDialog waitDialog;
	private Resources res;
	private Button btest;
	ARToolkit artoolkit;
	private int switchmap = -1;
	private FloorMap floormap;
	private FloorMapView mapview;
	private RightTriangle rightTriangle;
	private RightTriangle leftTriangle;
	private MapView mapbutton;
	private TestFragment fragobj;
	private SidebarFragment sidebar;
	public MainActivity() {
		super(false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setNonARRenderer(new LightingRenderer());//or might be omited
		res=getResources();
		artoolkit = getArtoolkit();		
//		getSurfaceView().setOnTouchListener(new TouchEventHandler());
		getSurfaceView().getHolder().addCallback(this);
		
		mapbutton = new MapView(this);
		mapview = new FloorMapView(this);
		mapview.setFloorNum(1);
		mapview.hide();
        FrameLayout.LayoutParams floorParams = new FrameLayout.LayoutParams(550,550);
//        floorParams.leftMargin = 700;
//        floorParams.topMargin = 250;
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
		
        
        
        FrameLayout.LayoutParams mapParams = new FrameLayout.LayoutParams(300, 150);
        mapParams.leftMargin = 1000;
        mapParams.topMargin = 550;
//        mapParams.leftMargin = 1700;
//        mapParams.topMargin = 85;
        float[] roundedCorner = new float[] { 5, 5, 5, 5, 5, 5, 5, 5 };
        CustomBorderDrawable gd = new CustomBorderDrawable(new RoundRectShape(roundedCorner, null, null));
        gd.setFillColour(0xFF00FFFF);
        LineFocusCamera line = new LineFocusCamera(this);				  
        FrameLayout.LayoutParams lineParams = new FrameLayout.LayoutParams(400, 300);
        lineParams.leftMargin = 450;
        lineParams.topMargin = 200;
//        lineParams.leftMargin = 800;
//        lineParams.topMargin = 350;
        mapview.setBackgroundColor(Color.GREEN);
		position = new PositioningView(this,Color.BLACK);
		position.setVisibility(switchmap);
		goalPosition = new PositioningView(this,Color.RED);
		goalPosition.setVisibility(switchmap);
        pathview = new PathView(this);
        pathview.setVisibility(switchmap);
        
        
        try {
        	ArrayList <JSONObject> list_json = new ArrayList<JSONObject>();

			String [] filenames = getAssets().list("json");
			for(String filename : filenames){
				JSONObject json_obj2 = new JSONObject(loadJSONFromAsset("json/"+filename));
				Log.i("JSON FILE","JSON FILE : "+filename);
				list_json.add(json_obj2);
			}
			
			floormap = new FloorMap(pathview,position,goalPosition,mapview,leftTriangle,rightTriangle,list_json);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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
        fragobj= TestFragment.newInstance("");
        sidebar = SidebarFragment.newInstance("");
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
        getFrame().addView(goalPosition,floorParams);
        
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setId(12345);

        LinearLayout jj = new LinearLayout(this);
        LinearLayout.LayoutParams jjParams= new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        jj.setLayoutParams(jjParams);
        jj.setOrientation(LinearLayout.HORIZONTAL);
        jj.setId(12345);
        
        
        getFragmentManager().beginTransaction().add(ll.getId(), fragobj).commit();
        getFragmentManager().beginTransaction().add(jj.getId(), sidebar).hide(sidebar).commit();

        
        
        
        sidebar.setFloorMap(floormap);
        fragobj.setFloorMap(floormap);

        getFrame().addView(ll);
        getFrame().addView(jj);
        
        getFrame().setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				getFragmentManager().beginTransaction().setCustomAnimations(com.example.finalproject.R.anim.emter_from_left, com.example.finalproject.R.anim.exit_to_right,0,0).hide(sidebar).commit();
				return false;
			}
		});
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
								if(filename.contains("pat")){
									objModel3d.add(new Model3D(model,filename));
									Log.i("FILENAME",""+"Marker : "+filename);									
								}
							}
							
							///////////////////ADD NODE to MARKER/////////////////////////////////
							
							ListModel.newInstance(objModel3d);
							
							objModel3d = ListModel.getInstance().getModels();
							
							int index = 0;
							for(int i=1;i<=6;i++){
								List<Vertex> nodes = floormap.getNodes(i);
								for(Vertex node : nodes){
									if(node.isMarker()){
										objModel3d.get(index).setNode(node);
				    					try {
				    						if(i <= 6){
												artoolkit.registerARObject(objModel3d.get(index));
												objModel3d.get(index).setRegis(true);
				    						}
//											artoolkit.registerARObject(objModel3d.get(index));
//											objModel3d.get(index).setRegis(true);
											
										} catch (AndARException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
				    					markerListenners.add(new MarkerListenerTest(objModel3d.get(index),floormap,MainActivity.this,artoolkit));
				    					artoolkit.addVisibilityListener(markerListenners.get(index));
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
    		

			startPreview();
    	}
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
