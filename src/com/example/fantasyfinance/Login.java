package com.example.fantasyfinance;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class Login extends Activity {

	ParseUser currentUser;
	final Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		TextView tvLabel = (TextView) findViewById(R.id.usertvName);
		tvLabel.setText(getIntent().getStringExtra("username"));
		
		setTag(getIntent().getStringExtra("username"));
		
	}

	private void setTag(String username) {
		
		Parse.initialize(this, Constants.APPLICATION_KEY,
				Constants.APPLICATION_KEY_TOKEN);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("TaglineModel");
		query.whereEqualTo("user", username);
		query.findInBackground(new FindCallback<ParseObject>() 
		{
			public void done(List<ParseObject> tags, ParseException e) 
			{
				if (tags.size() == 1 && e == null) {
					String objectID = tags.get(0).getObjectId();
					
					ParseQuery<ParseObject> query = ParseQuery.getQuery("TaglineModel");
					query.getInBackground(objectID,new GetCallback<ParseObject>() 
							{
								public void done(ParseObject taglineObject,ParseException e) 
								{
									if (e == null) {
										String tag = taglineObject.get("tag").toString();
										TextView tagView = (TextView) findViewById(R.id.usertvTagline);
										tagView.setText(tag);
									}
								}
							});
				} 
				else 
				{
					Log.d("DEBUG", "Error: " + e.getMessage());
				}
			}
		});
	}

	public void onLogoutAction(MenuItem mi) {
		ParseUser.logOut();
		currentUser = ParseUser.getCurrentUser();
		invalidateOptionsMenu();
		Intent logout = new Intent(getApplicationContext(), Home.class);
		startActivity(logout);
	}

	public void onEditTagline(MenuItem mi) {
		currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			Intent editTag = new Intent(getApplicationContext(), Tagline.class);
			editTag.putExtra("uname", currentUser.getUsername().toString());
			startActivity(editTag);
		} else {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
			alertDialogBuilder.setTitle("Please login to continue");
			alertDialogBuilder
					.setMessage("Click Ok to exit!")
					.setCancelable(false)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Intent logout = new Intent(getApplicationContext(), Home.class);
									startActivity(logout);
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
