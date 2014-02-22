package com.example.fantasyfinance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.utils.Constants;
import com.parse.ParseUser;

public class SearchStocks extends Fragment {

	String username;
	ParseUser currentUser;
	// List view
	public ListView lv;

	// Listview Adapter
	ArrayAdapter<String> adapter;

	// Search EditText
	EditText inputSearch;

	final Context context = getActivity();
	List<String> array_sort = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_search_stocks,
				container, false);
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
		lv = (ListView) getActivity().findViewById(R.id.listView1);
		lv.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, Constants.companies));
		currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					// selected item
					String selectedFromList = (String) (lv.getItemAtPosition(position));
					//Toast.makeText(getActivity().getApplicationContext(),position, Toast.LENGTH_LONG).show();
					int index = Arrays.asList(Constants.companies).indexOf(selectedFromList);
					Log.d("DEBUG",index+"");
					Intent intent = new Intent(getActivity(), StockInfo.class);
					intent.putExtra("username",currentUser.getUsername().toString());
					intent.putExtra("index", index);
					String selectedSymbol=Constants.symbols[index];
					intent.putExtra("selectedStock",selectedSymbol);
					startActivity(intent);
				}
			});
		} else {
			Intent intent = new Intent(getActivity(), Home.class);
			startActivity(intent);
		}
		inputSearch = (EditText) getActivity().findViewById(R.id.searchStock);
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text

				array_sort.clear();
				for (int i = 0; i < Constants.companies.length; i++) {
					if (Constants.companies[i].toString().contains(
							inputSearch.getText().toString())) {
						array_sort.add(Constants.companies[i].toString());
					}
				}
				lv.setAdapter(new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_list_item_1, array_sort));
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});

	}
}
