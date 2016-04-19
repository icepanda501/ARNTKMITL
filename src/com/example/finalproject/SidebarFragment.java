package com.example.finalproject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SidebarFragment extends Fragment {
private static SidebarFragment s = null;
	
    public static SidebarFragment newInstance(String text) {

    	s = new SidebarFragment();
        Bundle b = new Bundle();
        b.putString("text", text);
        s.setArguments(b);
        return s;
    }
    
    public static SidebarFragment getInstance(){
		return s;
    	
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	
    	View v =  inflater.inflate(R.layout.sidebar, container, false);
    	
    	
    	
		return v;    	
    }
	
	
}
