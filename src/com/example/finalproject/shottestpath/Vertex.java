package com.example.finalproject.shottestpath;

public class Vertex {
	
	  final private String id;
	  final private String name;
	  final private int floorNum;
	  final private int x;
	  final private int y;
	  final private int number;
	  private boolean isRoom = false;
	  private boolean isMarker = false;
	  public Vertex(String id, String name,int number,int x,int y,int floorNum,boolean isMarker,boolean isRoom) {
		    this.id = id;
		    this.name = name;
		    this.x = x;
		    this.y = y;
		    this.number = number;
		    this.floorNum = floorNum;
		    this.isMarker = isMarker;
		    this.isRoom = isRoom;
	  }
	  
	  public String getId() {
		    return id;
		  }
	  

		  public String getName() {
		    return name;
		  }
		  
		  public int getNumber(){
			  return number;
		  }
		  public int getFloor(){
			  
			  return floorNum;
		  }
		  public int getX(){
			  return x;
		  }
		  public int getY(){
			  return y;
		  }
		  
		  @Override
		  public int hashCode() {
		    final int prime = 31;
		    int result = 1;
		    result = prime * result + ((id == null) ? 0 : id.hashCode());
		    return result;
		  }
		  
		  @Override
		  public boolean equals(Object obj) {
		    if (this == obj)
		      return true;
		    if (obj == null)
		      return false;
		    if (getClass() != obj.getClass())
		      return false;
		    Vertex other = (Vertex) obj;
		    if (id == null) {
		      if (other.id != null)
		        return false;
		    } else if (!id.equals(other.id))
		      return false;
		    return true;
		  }

		  @Override
		  public String toString() {
		    return name;
		  }

		  
		  public boolean isMarker(){
			return isMarker;
			  
		  }
		  
		  public boolean isRoom(){
			return isRoom;
			  
		  }
		  
		 

}
