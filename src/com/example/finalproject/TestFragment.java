package com.example.finalproject;


import java.util.ArrayList;
import java.util.List;

import com.example.finalproject.shottestpath.FloorMap;
import com.example.finalproject.shottestpath.Vertex;
import com.example.finalproject.view.FloorMapView;



import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
	private AlertDialog.Builder builder;
	private Dialog dialog;
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
        SidebarFragment.getInstance().setAutocomplete(autocomplete);
        
		builder = new AlertDialog.Builder(this.getActivity());
		builder.setTitle("Where do you want to go ?");

		// Set up the input
		final AutoCompleteTextView input = new AutoCompleteTextView(this.getActivity());
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setAdapter(adapter);
		builder.setView(input);
//		dialog = builder.show();
		input.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		input.setThreshold(2);
		input.setId(123456789);
		input.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if(arg0.length() >= 2){
					input.setDropDownAnchor(input.getId());
					InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(input.getWindowToken(), 0);				
				}		
			}
			
		});
		input.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
    				final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    			    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
//    			    Toast.makeText(TestFragment.this.getActivity(),  "index of item : "+items.indexOf(autocomplete.getText()+""),Toast.LENGTH_SHORT).show();
    			    if(items.indexOf(input.getText()+"") == -1){
    			    	Toast.makeText(TestFragment.this.getActivity(),  "Destination not found",Toast.LENGTH_SHORT).show();
    			    	input.setText("");
    			    }else{
        				floorMap.setEndNode(nodes.get(index.get(items.indexOf(input.getText()+""))));
        		        Toast.makeText(TestFragment.this.getActivity(),  "Select : " + floorMap.getEndNode(),Toast.LENGTH_SHORT).show();
        		        input.dismissDropDown();
    			    }

    		        return true;
                }
				return false;
			}
		});
		
		input.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View v, int position,
					long arg3) {
				final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
				floorMap.setEndNode(nodes.get(index.get(items.indexOf(listView.getItemAtPosition(position)))));
		        Toast.makeText(TestFragment.this.getActivity(),  "Select : " + floorMap.getEndNode(),Toast.LENGTH_SHORT).show();
		        autocomplete.setText(listView.getItemAtPosition(position).toString());
		        autocomplete.dismissDropDown();
		        dialog.cancel();
			}
		});
		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {

		    }
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});

		
        autocomplete.setOnKeyListener(new OnKeyListener() {
        	@Override
            public boolean onKey(View v, int actionId,
                    KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
    				final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    			    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
//    			    Toast.makeText(TestFragment.this.getActivity(),  "index of item : "+items.indexOf(autocomplete.getText()+""),Toast.LENGTH_SHORT).show();
    			    if(items.indexOf(autocomplete.getText()+"") == -1){
    			    	Toast.makeText(TestFragment.this.getActivity(),  "Destination not found",Toast.LENGTH_SHORT).show();
    			    	autocomplete.setText("");
    			    }else{
        				floorMap.setEndNode(nodes.get(index.get(items.indexOf(autocomplete.getText()+""))));
        		        Toast.makeText(TestFragment.this.getActivity(),  "Select : " + floorMap.getEndNode(),Toast.LENGTH_SHORT).show();
        		        autocomplete.dismissDropDown();
    			    }

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