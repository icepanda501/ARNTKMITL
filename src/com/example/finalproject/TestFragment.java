package com.example.finalproject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.example.finalproject.shottestpath.FloorMap;
import com.example.finalproject.shottestpath.Vertex;

import edu.dhbw.andar.exceptions.AndARException;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class TestFragment extends Fragment {
	
	private FloorMap floorMap;
	private Button clear_btn;
	private AutoCompleteTextView autocomplete;
	private List<Vertex> nodes;
	private ArrayList<String> items;
	private ArrayList<Integer> index;
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
        autocomplete = (AutoCompleteTextView)v.findViewById(R.id.autocomplete);
        clear_btn = (Button)v.findViewById(R.id.clearBtn);
//        SearchView search = (SearchView) v.findViewById(R.id.searchview);
        items = new ArrayList<String>();
        index = new ArrayList<Integer>();
        nodes = new ArrayList<Vertex>();
        
        ///////////////////////////// ADD NODE to AUTOCOMPLETE////////////////////////////////////
        int indexBuffer = 0;
        for(int i=1;i<=2;i++){
        	ArrayList<Vertex> nodeBuffers = (ArrayList<Vertex>) floorMap.getNodes(i);
    		for(Vertex node : nodeBuffers){
    			if(node.isRoom()){
    					nodes.add(node);
    					items.add(node.getName());
    					index.add(indexBuffer);
    					indexBuffer++;
    			}
    		}
        }
		Log.i("items","items : "+items.toString());
		Log.i("index of item","index of item : "+index.toString());
		Log.i("Nodes","Nodes : "+nodes.toString());
        //////////////////////////////////////////////////////////////////////////////////////////
        
        
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, items);
//        dropdown.setAdapter(adapter);
        autocomplete.setAdapter(adapter);
        autocomplete.setOnKeyListener(new OnKeyListener() {
        	@Override
            public boolean onKey(View v, int actionId,
                    KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
    				final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    			    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    				floorMap.setEndNode(nodes.get(index.get(items.indexOf(autocomplete.getText()))));
    		        Toast.makeText(TestFragment.this.getActivity(),  "Select : " + floorMap.getEndNode(),Toast.LENGTH_SHORT).show();
    		        return true;
                }
                return false;
            }
        });
        
        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
				floorMap.setEndNode(nodes.get(index.get(items.indexOf(listView.getItemAtPosition(position)))));
		        Toast.makeText(TestFragment.this.getActivity(),  "Select : " + floorMap.getEndNode(),Toast.LENGTH_SHORT).show();
				
			}
		});
        
        
        clear_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				floorMap.clear();
				autocomplete.setText("");
				
			}
		});
        return v;
    }
    
    public void setFloorMap(FloorMap floorMap){
    	this.floorMap = floorMap;
    }
}