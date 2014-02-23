package com.example.fantasyfinance;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.adapters.LazyAdapter;
import com.example.utils.Constants;
import com.parse.ParseUser;

public class FollowedStocks extends Fragment {
	
	String username;
	ParseUser currentUser;
	final Context context = getActivity();
	public ListView lv;
    LazyAdapter adapter;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.activity_followed_stocks, container, false);
        currentUser = ParseUser.getCurrentUser();

		if (currentUser != null) {
			Bundle b = getActivity().getIntent().getExtras();
			username = b.getString("username");
		} else {
			Intent intent = new Intent(getActivity(), Home.class);
			startActivity(intent);
		}
        return rootView;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		lv = (ListView) getActivity().findViewById(R.id.list);
		ArrayList<HashMap<String, String>> savedActivity = new ArrayList<HashMap<String, String>>();
		
		final String companies[] = {"American Airlines Group, Inc. ","Atlantic American Corporation ","Applied Optoelectronics, Inc. ","AAON, Inc. ","Apple Inc. "};
		final String symbols[] = {"AALCP","AAME","AAOI","AAON","AAPL"};
		final String stocks[] = {"38","39","40","41","42"};
		// looping through all song nodes <song>
		for (int i = 0; i < 5; i++) {
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put(Constants.KEY_NAME, companies[i]);
			map.put(Constants.KEY_SYMBOL,symbols[i] );
			map.put(Constants.KEY_VALUE,stocks[i]);

			// adding HashList to ArrayList
			savedActivity.add(map);
		}
		
		adapter=new LazyAdapter(getActivity(), savedActivity);        
        lv.setAdapter(adapter);
		
	}
}
