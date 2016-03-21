package com.example.finalproject;

import com.example.finalproject.shottestpath.FloorMap;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TestFragment extends Fragment {
	
	private FloorMap floorMap;

    public static TestFragment newInstance(String text) {

        TestFragment f = new TestFragment();
        Bundle b = new Bundle();
        b.putString("text", text);
        f.setArguments(b);
        return f;
    }
    
   

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment, container, false);
        ((TextView) v.findViewById(R.id.tvFragText)).setText(getArguments().getString("text"));   
//        SearchView search = (SearchView) v.findViewById(R.id.searchview);
        Spinner dropdown = (Spinner)v.findViewById(R.id.spinner);
        final String[] items = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				floorMap.setEndNode(Integer.parseInt(adapter.getItem(position)));
		        Toast.makeText(TestFragment.this.getActivity(),  "Select : " + floorMap.getEndNode(),Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> perent) {
				// TODO Auto-generated method stub
				
			}
        	
		});

        
        
        

        
        return v;
    }
    
    public void setFloorMap(FloorMap floorMap){
    	this.floorMap = floorMap;
    }
}