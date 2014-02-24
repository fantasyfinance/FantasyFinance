package com.example.fantasyfinance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapters.ExpandableListAdapter;
import com.example.utils.Constants;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ProfileActivity extends Fragment {

	String username;
	ParseUser currentUser;
	final Context context = getActivity();
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_profile, container,
				false);

		currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			Bundle b = getActivity().getIntent().getExtras();
			username = b.getString("username");
			setTag(username);
			TextView tvLabel = (TextView) rootView.findViewById(R.id.usertvName);
			tvLabel.setText(username);
		} else {
			Intent intent = new Intent(getActivity(), Home.class);
			startActivity(intent);
			
			
		}
		
		// get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(getActivity().getApplicationContext(), listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
 
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });
 
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
             /*   Toast.makeText(getActivity().getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*/
            }
        });
 
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
               /* Toast.makeText(getActivity().getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();*/
 
            }
        });
 
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
		return rootView;
	}
	
	private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        listDataHeader.add("Feb 22 2014           5");
        listDataHeader.add("Feb 23 2014           10");
        listDataHeader.add("Feb 24 2014           15");
 
        // Adding child data
        List<String> date1 = new ArrayList<String>();
        date1.add("AAPL             +5");
        date1.add("YHOO             -5");
        date1.add("BOFA             +5");
        
        List<String> date2 = new ArrayList<String>();
        date2.add("FB               +5");
        date2.add("GOOG             -5");
        date2.add("YHOO             +5");
        date2.add("FEYE             +5");
 
        List<String> date3 = new ArrayList<String>();
        date3.add("YHOO             +5");
        date3.add("GOOG             +5");
        date3.add("FEYE             +5");
       
 
        listDataChild.put(listDataHeader.get(0), date1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), date2);
        listDataChild.put(listDataHeader.get(2), date3);
    }

	private void setTag(String username) {

		Parse.initialize(getActivity(), Constants.APPLICATION_KEY,
				Constants.APPLICATION_KEY_TOKEN);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("TaglineModel");
		query.whereEqualTo("user", username);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> tags, ParseException e) {
				if (tags.size() == 1 && e == null) {
					String objectID = tags.get(0).getObjectId();

					ParseQuery<ParseObject> query = ParseQuery
							.getQuery("TaglineModel");
					query.getInBackground(objectID,
							new GetCallback<ParseObject>() {
								public void done(ParseObject taglineObject,
										ParseException e) {
									if (e == null) {
										String tag = taglineObject.get("tag")
												.toString();
										TextView tagView = (TextView) getActivity()
												.findViewById(
														R.id.usertvTagline);
										tagView.setText(tag);
									}
								}
							});
				} else {
					Log.d("DEBUG", "Error: " + e.getMessage());
				}
			}
		});
	}
}
