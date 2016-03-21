package com.example.finalproject.shottestpath;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.finalproject.view.PathView;
import com.example.finalproject.view.PositioningView;

import android.util.Log;

public class FloorMap {
	private List<Vertex> nodes;
	private List<Edge> edges;
	private ArrayList<ArrayList<Edge>> floorEdges = new ArrayList<ArrayList<Edge>>();
	private ArrayList<ArrayList<Vertex>> floorNodes = new ArrayList<ArrayList<Vertex>>();
	private int startNode = 0;
	private int endNode = -1;
	private PathView pathview;
	private PositioningView position;
	public FloorMap(PathView pathview,PositioningView position){
		
		this.pathview = pathview;
		this.position = position;
		for(int i=1;i<=5;i++){
			floorNodes.add(new ArrayList<Vertex>());
			floorEdges.add(new ArrayList<Edge>());
		}
		
	    addVertex(1,0, 3, 6,true);
	    addVertex(1,1, 9, 6,true);
	    addVertex(1,2, 11, 6,false);
	    addVertex(1,3, 11, 7,true);
	    addVertex(1,4, 11, 9,false);
	    addVertex(1,5, 13, 9,true);
	    addVertex(1,6, 17, 9,false);
	    addVertex(1,7, 11, 13,true);
	    addVertex(1,8, 17, 13,true);
	    addVertex(1,9, 11, 18,true);
	    addVertex(1,10, 17, 18,true);
	    addLane("Edge_0",1, 0, 1, 1);
	    addLane("Edge_1",1, 1, 2, 1);
	    addLane("Edge_2",1, 2, 3, 1);
	    addLane("Edge_3",1, 3, 4, 1);
	    addLane("Edge_4",1, 4, 5, 1);
	    addLane("Edge_5",1, 5, 6, 1);
	    addLane("Edge_6",1, 6, 8, 1);
	    addLane("Edge_7",1, 4, 7, 1);
	    addLane("Edge_8",1, 7, 8, 1);
	    addLane("Edge_9",1, 7, 9, 1);
	    addLane("Edge_10",1, 8, 10, 1);
	    addLane("Edge_11",1, 9, 10, 1);		
	    //two way lane
	    addLane("Edge_0",1, 1, 0, 1);
	    addLane("Edge_1",1, 2, 1, 1);
	    addLane("Edge_2",1, 3, 2, 1);
	    addLane("Edge_3",1, 4, 3, 1);
	    addLane("Edge_4",1, 5, 4, 1);
	    addLane("Edge_5",1, 6, 5, 1);
	    addLane("Edge_6",1, 8, 6, 1);
	    addLane("Edge_7",1, 7, 4, 1);
	    addLane("Edge_8",1, 8, 7, 1);
	    addLane("Edge_9",1, 9, 7, 1);
	    addLane("Edge_10",1, 10, 8, 1);
	    addLane("Edge_11",1, 10, 9, 1);		
	    
	    
		
		
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

		  private void addLane(String laneId,int floorNum, int sourceLocNo, int destLocNo,
		      int duration) {
		    Edge lane = new Edge(laneId,floorNodes.get(floorNum).get(sourceLocNo), floorNodes.get(floorNum).get(destLocNo), duration);
		    floorEdges.get(floorNum).add(lane);
		  }
		  
		  private void addVertex(int floorNum,int i,int x,int y,boolean isMarker){
		      Vertex location = new Vertex("Node_" + i, "Node_" + i,i,x,y,floorNum);
		      location.setIsMarker(isMarker);
		      floorNodes.get(floorNum).add(location);
		      
		  }
		  
		  public List<Vertex> getNodes(int floorNum){
			  return floorNodes.get(floorNum);
		  }
		  
		  public void setStartNode(int startNode){
			  this.startNode = startNode;
		  }
		  public void setEndNode(int endNode){
			  this.endNode = endNode;
		  }
		  public int getStartNode(){
			  return startNode;
		  }
		  public int getEndNode(){
			  return endNode;
		  }
		  
		  public void drawPosition(int x,int y){
			  position.setPosition(x, y);
		  }
		  
		  public void drawPath(int floorNum , int start,int des){
			  pathview.setPath(getShottestPath(floorNum , start, des));
		  }
		  

}
