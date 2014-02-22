package com.example.fantasyfinance;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.utils.Constants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class StockInfo extends Activity {

	ParseUser currentUser;
	EditText et;
	String username;
	String stock;
	Button p1_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_info);
		currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			TextView tvLabel = (TextView) findViewById(R.id.selectedStock);
			stock = getIntent().getStringExtra("selectedStock");
			tvLabel.setText(stock);
			username = getIntent().getStringExtra("username");
			ParseQuery<ParseObject> query = ParseQuery
					.getQuery("UserStockPreference");
			query.whereEqualTo("user", username);
			query.whereEqualTo("stock", stock);
			Log.d("DEBUG", stock);
			Log.d("DEBUG", username);
			
			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> records, ParseException e) {
					if (records.size() > 0 && e == null) {
						Log.d("DEBUG", "PRESENT");
						p1_button = (Button) findViewById(R.id.button1);
						p1_button.setText(Constants.unFollow);
						// so write the code for submit unfollow button
					} else if (e != null) {
						Log.d("DEBUG", "Error: " + e.getMessage());
					} else if (records.size() == 0 && e == null) {
						Log.d("DEBUG", "ABSENT");
						p1_button = (Button) findViewById(R.id.button1);
						p1_button.setText(Constants.follow);
						// so write the code for submit follow button
					}
				}
			});
		}
	}

	public void onLogoutAction(MenuItem mi) {
		ParseUser.logOut();
		currentUser = ParseUser.getCurrentUser();
		invalidateOptionsMenu();
		Intent logout = new Intent(getApplicationContext(), Home.class);
		startActivity(logout);
		StockInfo.this.finish();
	}

	public void onStatusClick(View v) {
		
		if (p1_button.getText().equals("Follow")) {
			Log.d("DEBUG","Follow");
			username = getIntent().getStringExtra("username");
			stock = getIntent().getStringExtra("selectedStock");
			
			ParseObject userstock = new ParseObject("UserStockPreference");
			userstock.put("user", username);
			userstock.put("stock", stock);
			userstock.saveInBackground();
			
			ParseObject prediction = new ParseObject("Predict");
			prediction.put("user", username);
			prediction.put("stock", stock);
			prediction.put("lock", "no");
			prediction.put("prediction", "n/a");
			prediction.saveInBackground();
			
			
			Intent intent = new Intent(this, Login.class);
			intent.putExtra("username",username);
			intent.putExtra("stock", stock);
			startActivity(intent);
			
		} else {
			Log.d("DEBUG","UnFollow");
			username = getIntent().getStringExtra("username");
			stock = getIntent().getStringExtra("selectedStock");
			ParseQuery<ParseObject> query = ParseQuery
					.getQuery("UserStockPreference");
			query.whereEqualTo("user", username);
			query.whereEqualTo("stock", stock);
			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> records, ParseException e) {
					if (records.size() == 1 && e == null) {
						records.get(0).deleteInBackground();
						ParseQuery<ParseObject> query = ParseQuery
								.getQuery("Predict");
						query.whereEqualTo("user", username);
						query.whereEqualTo("stock", stock);
						query.findInBackground(new FindCallback<ParseObject>() {
							public void done(List<ParseObject> records, ParseException e) {
								if (records.size() == 1 && e == null) {
									records.get(0).deleteInBackground();
								} else if (records.size() == 0 && e == null) {
									
								}
							}
						});
					} else if (records.size() == 0 && e == null) {
						
					}
				}
			});
			
			Intent intent = new Intent(this, Login.class);
			intent.putExtra("username",username);
			intent.putExtra("stock", stock);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stock_info, menu);
		return true;
	}

}
