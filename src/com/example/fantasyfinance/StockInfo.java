package com.example.fantasyfinance;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
			ParseQuery<ParseObject> query = ParseQuery.getQuery("UserStockPreference");
			query.whereEqualTo("user", username);
			query.whereEqualTo("stock","AAPL");
			Log.d("DEBUG",stock);
			Log.d("DEBUG",username);
			
			query.findInBackground(new FindCallback<ParseObject>() 
			{
				public void done(List<ParseObject> records, ParseException e) 
				{
					if (records.size() > 0 && e == null) 
					{
						Log.d("DEBUG","PRESENT");
						Button p1_button = (Button)findViewById(R.id.button1);
						p1_button.setText("UnFollow");
						//so write the code for submit unfollow button
					} 
					else if(e != null) {
						Log.d("DEBUG", "Error: " + e.getMessage());
					}
					else if(records.size() == 0 && e == null)
					{
						Log.d("DEBUG","ABSENT");
						Button p1_button = (Button)findViewById(R.id.button1);
						p1_button.setText("Follow");
						//so write the code for submit follow button
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
	
	
	public void onStatusClick(View v)
	{
		Log.d("DEBUG","Clicked");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stock_info, menu);
		return true;
	}

}
