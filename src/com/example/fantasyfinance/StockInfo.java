package com.example.fantasyfinance;

import com.parse.ParseUser;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class StockInfo extends Activity {

	ParseUser currentUser;
	EditText et;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_info);
		currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			TextView tvLabel = (TextView) findViewById(R.id.selectedStock);
			tvLabel.setText(getIntent().getStringExtra("selectedStock"));
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stock_info, menu);
		return true;
	}

}
