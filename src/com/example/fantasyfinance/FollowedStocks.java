package com.example.fantasyfinance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.parse.ParseUser;

public class FollowedStocks extends Fragment {
	
	String username;
	ParseUser currentUser;
	final Context context = getActivity();
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.activity_followed_stocks, container, false);
        currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			Bundle b = getActivity().getIntent().getExtras();
			username = b.getString("username");
			TextView tvLabel = (TextView) rootView.findViewById(R.id.followed_username);
			tvLabel.setText(username);
		} else {
			Intent intent = new Intent(getActivity(), Home.class);
			startActivity(intent);
			
		}
        return rootView;
    }
}
