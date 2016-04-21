package com.example.finalproject;

import java.util.ArrayList;

import android.os.Bundle;

import com.example.finalproject.graphics.Model3D;

public class ListModel {
	private static ListModel listModel = null;
	private ArrayList<Model3D> objModel3d = new ArrayList<Model3D>();
    public static ListModel newInstance(ArrayList<Model3D> objModel3d) {

    	listModel = new ListModel();
        Bundle b = new Bundle();
        listModel.objModel3d = objModel3d;
        return listModel;
    }
    
    public static ListModel getInstance(){
		return listModel;
    	
    }
	
	public ArrayList<Model3D> getModels(){
		return objModel3d;
		
	}
	
	public ArrayList<Model3D> getModelsWithout(Model3D model){
		objModel3d.remove(objModel3d.indexOf(model));
		return objModel3d;
	}
	
	public ArrayList<Model3D> addModel(Model3D model){
		objModel3d.add(model);
		
		return objModel3d;
	}

}
