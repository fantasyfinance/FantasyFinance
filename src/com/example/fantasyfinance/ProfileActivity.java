package com.example.fantasyfinance;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
		return rootView;
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
