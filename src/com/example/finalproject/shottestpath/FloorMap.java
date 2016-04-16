package com.example.finalproject.shottestpath;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.finalproject.view.FloorMapView;
import com.example.finalproject.view.MapView;
import com.example.finalproject.view.PathView;
import com.example.finalproject.view.PositioningView;
import com.example.finalproject.view.RightTriangle;

import android.util.Log;
import android.view.View;

public class FloorMap {
	private List<Vertex> nodes;
	private List<Edge> edges;
	private ArrayList<ArrayList<Edge>> floorEdges = new ArrayList<ArrayList<Edge>>();
	private ArrayList<ArrayList<Vertex>> floorNodes = new ArrayList<ArrayList<Vertex>>();
	private Vertex currentNode;
	private Vertex endNode;
	private PathView pathview;
	private PositioningView position;
	private FloorMapView mapview;
	private int currentFloor;
	private int endFloor;
	private RightTriangle leftTriangle;
	private RightTriangle rightTriangle;
	private LinkedList<Vertex> path;
	private LinkedList<Vertex> otherPath;
	public FloorMap(PathView pathview,PositioningView position, FloorMapView mapview,RightTriangle leftTriangle, RightTriangle rightTriangle, JSONObject json_obj) throws JSONException{
		
		this.pathview = pathview;
		this.position = position;
		this.mapview = mapview;
		this.leftTriangle = leftTriangle;
		this.rightTriangle = rightTriangle;
		for(int i=1;i<=7;i++){
			floorNodes.add(new ArrayList<Vertex>());
			floorEdges.add(new ArrayList<Edge>());
		}
		
		String name;
		int floor,number,x,y;
		boolean isMarker,isRoom;
		JSONArray json_array = json_obj.getJSONArray("data");
		for(int i = 0;i<json_array.length();i++){
			JSONObject j_inside = json_array.getJSONObject(i);
			floor = j_inside.getInt("floor");
			number = j_inside.getInt("number");
			name = j_inside.getString("name");
			x = j_inside.getInt("x");
			y = j_inside.getInt("y");
			if(j_inside.getString("type").contains("marker")){
				isMarker = true;
			}else{
				isMarker = false;
			}
			if(j_inside.getString("type").contains("room")){
				isRoom = true;
			}else{
				isRoom = false;
			}
			
			try {
				name = new String(name.getBytes("UTF-8"), "UTF-8");
				Log.i("name", name);
				addVertex(floor,name,number, x, y,isMarker,isRoom);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
		for(int i = 0;i<json_array.length();i++){
			JSONObject j_inside = json_array.getJSONObject(i);
			int link_number,distance,angle;
			number = j_inside.getInt("number");
			floor = j_inside.getInt("floor");
			JSONArray link = j_inside.getJSONArray("link");
			for(int j=0;j<link.length();j++){
				JSONObject link_inside = link.getJSONObject(j);
				Log.i("Link JSON","Link JSON : "+link_inside.toString()+" "+number);
				link_number = link_inside.getInt("number");
				distance = link_inside.getInt("distance");
				angle = link_inside.getInt("angle");			
				addLane("EdgeGo"+link_number,floor,number,link_number,distance,angle);
				addLane("EdgeBack"+link_number,floor,link_number,number,distance,angle);
				
			}
		}
		
		
	    
	    
		
		
	}
		public LinkedList<Vertex> getShottestPath(int floorNum , int start,int des) {

		    // Lets check from location Loc_1 to Loc_10
		  		  	
		    Graph graph = new Graph(floorNodes.get(floorNum), floorEdges.get(floorNum));
		    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		    dijkstra.execute(floorNodes.get(floorNum).get(start));
		    
		    if(des == -1){
		    	return new LinkedList<Vertex>();
		    }
		    
		    
		    
		    if(start != des){
		    	LinkedList<Vertex> path = dijkstra.getPath(floorNodes.get(floorNum).get(des));
			    
//			    Log.i("PATH", "number of node"+path.size());
			    
//			    for (Vertex vertex : path) {
//			      System.out.println(vertex);
//			    }
			    
		    	if(path == null){
		    		path = new LinkedList<Vertex>();
		    	}
			    return path;
			    
		    }else{
		    	LinkedList<Vertex> path = new LinkedList<Vertex>();
		    	return path;
		    }
		    

		  }
	  
	  	  ////////////////GET PATH TO DRAW ////////////////////////////////
	  	  public LinkedList<Vertex> getPath(Vertex currentNode,Vertex endNode){
	  		int current_floor = currentNode.getFloor();
	  		int start = currentNode.getNumber();
	  		int end_floor;
	  		int des;
	  		if(endNode != null){
	  			end_floor = endNode.getFloor();
		  		des = endNode.getNumber();
	  		}else{
	  			des = -1;
	  			end_floor = current_floor;
	  		}
	  		
	  		
	  		if(current_floor != end_floor){
	  			path = getShottestPath(current_floor, start, 7);
	  		}else{
	  			path = getShottestPath(current_floor, start, des);
	  		}
			return path;
	  	  }

		  private void addLane(String laneId,int floorNum, int sourceLocNo, int destLocNo,
		      int duration , int angle) {
		    Edge lane = new Edge(laneId,floorNodes.get(floorNum).get(sourceLocNo), floorNodes.get(floorNum).get(destLocNo), duration,angle);
		    floorEdges.get(floorNum).add(lane);
		  }
		  
		  private void addVertex(int floorNum,String name,int i,int x,int y,boolean isMarker,boolean isRoom){
		      Vertex location = new Vertex("Node_" + i, name,i,x,y,floorNum,isMarker,isRoom);
		      floorNodes.get(floorNum).add(location);
		      
		      
		      
		  }
		  
		  public List<Vertex> getNodes(int floorNum){
			  return floorNodes.get(floorNum);
		  }
		  

		  public void setEndNode(Vertex endNode){
			  this.endNode = endNode;
			  if(currentNode != null){
				  drawPath(currentNode);
			  }else if(endNode != null){
				  mapview.show();
				  
			  }
		  }

		  public Vertex getEndNode(){
			  return endNode;
		  }
		  
		  public Vertex getCurrentPosition(){
			  return currentNode;
		  }
		  
		  public void currentPosition(Vertex node){
			  currentNode = node;
			  setCurrentFloor(currentNode.getFloor());
			  drawPosition(node.getX(),node.getY());
		  }
		  
		  public void setCurrentFloor(int floor){
			  this.currentFloor = floor;
			  mapview.setFloorNum(currentFloor);
		  }
		  
		  public void clear(){
			  currentNode = null;
			  endNode = null;
			  pathview.setPath(new LinkedList<Vertex>());
			  position.setPosition(-1, -1);
		  }
		  
		  public void drawPosition(int x,int y){
			  position.setPosition(x, y);
		  }
		  
		  public Vertex drawPath(Vertex Node){
			  LinkedList<Vertex> linePath = getPath(Node, endNode);
			  pathview.setPath(linePath);
			  Log.i("Linklist",linePath.toString());
			  if(endNode == null){
				  return null;
			  }
			  return linePath.get(1);
		  }
		  
		  
		  public Edge findTheWay(int floor,int start , int end){
			  
			  for(int i=0;i<floorEdges.get(floor).size();i++){
				  if(floorEdges.get(floor).get(i).getSource().getNumber() == start && floorEdges.get(floor).get(i).getDestination().getNumber() == end){
					  return floorEdges.get(floor).get(i);
				  }
			  }
			  	
			  return null;
			  
		  }
		  
		  public void showMap(){
				mapview.show();
				if(currentNode != null){
					position.show();
					if(endNode != null){
						if(currentNode.getFloor() != endNode.getFloor()){
							leftTriangle.show();
							rightTriangle.show();							
						}
					}
				}
				pathview.show();
				
		  }
		  public void hideMap(){
				mapview.hide();
				position.hide();
				pathview.hide();
				leftTriangle.hide();
				rightTriangle.hide();
		  }		  

		  public void rightMap(){
			  mapview.hide();
			  position.hide();
			  pathview.hide();
			  mapview.setFloorNum(endNode.getFloor());
			  drawPath(floorNodes.get(endNode.getFloor()).get(7));
			  mapview.show();
			  pathview.show();
		  }
		  
		  public void leftMap(){
			  mapview.hide();
			  position.hide();
			  pathview.hide();
			  mapview.setFloorNum(currentNode.getFloor());
			  drawPath(currentNode);
			  mapview.show();
			  pathview.show();
			  position.show();
			  
		  }

}
