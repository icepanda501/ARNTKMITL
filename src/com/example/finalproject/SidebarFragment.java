package com.example.finalproject;

import java.util.ArrayList;
import java.util.List;

import com.example.finalproject.shottestpath.FloorMap;
import com.example.finalproject.shottestpath.Vertex;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class SidebarFragment extends Fragment{
private static SidebarFragment s = null;
private ImageView hamberger;
private ListView listview;
private FloorMap floorMap;
private ArrayList<Vertex> nodes_sidebar = new ArrayList<Vertex>();
private ArrayList<Integer> index = new ArrayList<Integer>();
private ArrayList<Item> items = new ArrayList<Item>();
private Context activity = this.getActivity();
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
    	hamberger = (ImageView) v.findViewById(R.id.hamberger_in_sidebar);
    	listview = (ListView)v.findViewById(R.id.listView_sidebar);
    	
        int indexBuffer = 0;
        for(int i=1;i<=6;i++){
        	ArrayList<Vertex> nodeBuffers = (ArrayList<Vertex>) floorMap.getNodes(i);
        	items.add(new SectionItem("Floor "+i));
        	nodes_sidebar.add(new Vertex("dummy","dummy", 0,0,0,0,false,false));
        	index.add(indexBuffer);
        	indexBuffer++;
    		for(Vertex node_sidevar : nodeBuffers){
    			if(node_sidevar.isRoom()){
    					items.add(new EntryItem(node_sidevar.getName()));
    					nodes_sidebar.add(node_sidevar);
    					index.add(indexBuffer);
    					indexBuffer++;
    			}
    		}
        }
    	
//    	items.add(new SectionItem("Floor "+1));
//    	items.add(new EntryItem("test test"));
    	EntryAdapter adapter = new EntryAdapter(this.getActivity(), items);
    	listview.setAdapter(adapter);
    	
    	hamberger.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getFragmentManager().beginTransaction().setCustomAnimations(R.anim.emter_from_left, R.anim.exit_to_right,0,0).hide(s).commit();
			}
		});
    	
    	listview.setOnItemClickListener(new OnItemClickListener() {
			
    	    @Override
    		public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {
    		      
    		     EntryItem item = (EntryItem) items.get(position);
    		     Log.i("Sidebar","You clicked "+ item.title +" position "+position);
    		     floorMap.setEndNode(nodes_sidebar.get(position));
    		     getFragmentManager().beginTransaction().setCustomAnimations(R.anim.emter_from_left, R.anim.exit_to_right,0,0).hide(s).commit();
    		}
		});
    	
		return v;    	
    }

	   
    public void setFloorMap(FloorMap floorMap){
    	this.floorMap = floorMap;
    }
}
