package com.example.fantasyfinance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapters.LazyAdapter;
import com.example.utils.Constants;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class FollowedStocks extends Fragment {
	
	String username;
	ParseUser currentUser;
	final Context context = getActivity();
	public ListView lv;
    LazyAdapter adapter;
    final Set<String> companies = new HashSet<String>();
    final ArrayList<String> symbols = new ArrayList<String>();
    final ArrayList<String> stocks  = new ArrayList<String>();
    int share = 50;
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
		final ArrayList<HashMap<String, String>> savedActivity = new ArrayList<HashMap<String, String>>();
		Bundle b = getActivity().getIntent().getExtras();
		username = b.getString("username");
		ParseQuery<ParseObject> query = ParseQuery
				.getQuery("UserStockPreference");
		query.whereEqualTo("user", username);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> records, ParseException e) 
			{
				if (records.size() > 0 && e == null) 
				{
					int size = records.size();
					for(int i = 0 ;i < size ; i++)
					{
						String id = records.get(i).getObjectId();
						ParseQuery<ParseObject> query = ParseQuery.getQuery("UserStockPreference");
						query.getInBackground(id,new GetCallback<ParseObject>() 
						{
									public void done(ParseObject followObject,ParseException e) 
									{
										if (e == null) {
											String company = followObject.get("stock").toString();
											companies.add(company);
											HashMap<String, String> map = new HashMap<String, String>();
											map.put(Constants.KEY_SYMBOL,company);
											int index = Arrays.asList(Constants.symbols).indexOf(company);
											String companyName =Constants.companies[index];
											map.put(Constants.KEY_NAME, companyName);
											map.put(Constants.KEY_VALUE, Integer.toString(share));
											share ++;
											savedActivity.add(map);
										}
									}
						});
					}
				} 
				else if (e != null) {
				} 
				else if (records.size() == 0 && e == null) {
					// so write the code for submit follow button
					TextView tv = (TextView)getActivity().findViewById(R.id.followed_username);
					tv.setText("No Stocks are Followed!!!");
				}
			}
		});
        
		adapter=new LazyAdapter(getActivity(), savedActivity);        
        lv.setAdapter(adapter);
        currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String companySymbol =(String) ((TextView) view.findViewById(R.id.stockSymbol)).getText();
					username = currentUser.getUsername();
					Intent intent = new Intent(getActivity(), StockInfo.class);
					intent.putExtra("username",username);
					intent.putExtra("selectedStock",companySymbol);
					startActivity(intent);
					
				}
			});
		}
	}
}
