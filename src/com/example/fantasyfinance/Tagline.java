package com.example.fantasyfinance;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.utils.Constants;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Tagline extends Activity {

	ParseUser currentUser;
	EditText et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tagline);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#000000")));
		currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			TextView tvLabel = (TextView) findViewById(R.id.tagline);
			tvLabel.setText(getIntent().getStringExtra("uname") + "'s "
					+ Constants.yourTag);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tagline, menu);
		return true;
	}

	public void onTaglineClick(View v) {
		currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// Add your initialization code here
			Parse.initialize(this, Constants.APPLICATION_KEY,
					Constants.APPLICATION_KEY_TOKEN);

			ParseQuery<ParseObject> query = ParseQuery.getQuery("TaglineModel");
			query.whereEqualTo("user", currentUser.getUsername());
			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> tags, ParseException e) {
					if (tags.size()==1 && e == null) {
						String objectID = tags.get(0).getObjectId();
						ParseQuery<ParseObject> query = ParseQuery
								.getQuery("TaglineModel");
						query.getInBackground(objectID,
								new GetCallback<ParseObject>() {
									public void done(ParseObject taglineObject,
											ParseException e) {
										if (e == null) {
											EditText et = (EditText) findViewById(R.id.tagText);
											String tag = et.getText().toString();
											taglineObject.put("user",currentUser.getUsername());
											taglineObject.put("tag", tag);
											taglineObject.saveInBackground();
											Intent login = new Intent(getApplicationContext(), Login.class);
											login.putExtra("username",currentUser.getUsername());
											startActivity(login);
											overridePendingTransition(R.anim.left_out, R.anim.right_in);
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
}
