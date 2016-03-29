package com.example.finalproject.shottestpath;

public class Edge {
	
	private final String id; 
	  private final Vertex source;
	  private final Vertex destination;
	  private final int weight; 
	  private final int angle;
	  
	  public Edge(String id, Vertex source, Vertex destination, int weight,int angle) {
	    this.id = id;
	    this.source = source;
	    this.destination = destination;
	    this.weight = weight;
	    this.angle = angle;
	  }
	  
	  public String getId() {
	    return id;
	  }
	  public Vertex getDestination() {
	    return destination;
	  }

	  public Vertex getSource() {
	    return source;
	  }
	  public int getWeight() {
	    return weight;
	  }
	  public int getAngle(){
		  return angle;
	  }
	  
	  @Override
	  public String toString() {
	    return source + " " + destination;
	  }

}
