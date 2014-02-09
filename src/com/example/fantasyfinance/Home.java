package com.example.fantasyfinance;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class Home extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	public void onLoginClick(View v) {
		Toast.makeText(getApplicationContext(), "Clicked Login !!!", 
				   Toast.LENGTH_LONG).show();
		
	}
	
	public void onRegisterClick(View v) {
		Toast.makeText(getApplicationContext(), "Clicked Register !!!", 
				   Toast.LENGTH_LONG).show();
	}
	
	public void onForgotPassword(View v) {
		Toast.makeText(getApplicationContext(), "Clicked Forgot !!!", 
				   Toast.LENGTH_LONG).show();
		
	}
	
	public void onResetClick(View v) {
		Toast.makeText(getApplicationContext(), "Clicked Reset !!!", 
				   Toast.LENGTH_LONG).show();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
