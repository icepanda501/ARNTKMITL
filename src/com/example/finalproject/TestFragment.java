package com.example.finalproject;


import java.util.ArrayList;
import java.util.List;

import com.example.finalproject.shottestpath.FloorMap;
import com.example.finalproject.shottestpath.Vertex;
import com.example.finalproject.view.FloorMapView;



import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TestFragment extends Fragment {
	
	private FloorMap floorMap;
	private ImageView clear_btn;
	private ImageView floorText;
	private AutoCompleteTextView autocomplete;
	private List<Vertex> nodes;
	private ArrayList<String> items;
	private ArrayList<Integer> index;
	private SidebarFragment sidebar;
	private static TestFragment f;
	
    public static TestFragment newInstance(String text) {

        f = new TestFragment();
        Bundle b = new Bundle();
        b.putString("text", text);
        f.setArguments(b);
        return f;
    }
    
    
    
   

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment, container, false); 
        autocomplete = (AutoCompleteTextView)v.findViewById(R.id.autocomplete);
        clear_btn = (ImageView)v.findViewById(R.id.clearBtn);
        floorText = (ImageView)v.findViewById(R.id.floorButton);
//        SearchView search = (SearchView) v.findViewById(R.id.searchview);
        items = new ArrayList<String>();
        index = new ArrayList<Integer>();
        nodes = new ArrayList<Vertex>();
        sidebar = SidebarFragment.getInstance();
//        floorMap.setFloorText(floorText);
        
        ///////////////////////////// ADD NODE to AUTOCOMPLETE////////////////////////////////////
        int indexBuffer = 0;
        for(int i=1;i<=6;i++){
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
        
        floorText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction().setCustomAnimations(R.anim.emter_from_left, R.anim.exit_to_right,0,0).show(sidebar).commit();
				
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