package com.example.finalproject;

public class EntryItem implements Item{
	
	public final String title;
	  
	 public EntryItem(String title){
	  this.title = title;

	 }

	@Override
	public boolean isSection() {
		// TODO Auto-generated method stub
		return false;
	}
	  
}