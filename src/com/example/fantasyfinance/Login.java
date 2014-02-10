package com.example.fantasyfinance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseUser;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		TextView tvLabel = (TextView) findViewById(R.id.usertvName);
		tvLabel.setText(getIntent().getStringExtra("username"));
	}

	public void onLogoutAction(MenuItem mi) {
		ParseUser.logOut();
		ParseUser currentUser = ParseUser.getCurrentUser(); 
		invalidateOptionsMenu();
		Intent logout = new Intent(getApplicationContext(),Home.class);
		startActivity(logout);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
