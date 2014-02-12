package com.example.fantasyfinance;

import com.example.utils.Constants;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.widget.TextView;

public class Tagline extends Activity {

	ParseUser currentUser ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tagline);
		currentUser = ParseUser.getCurrentUser(); 
		if (currentUser!=null)
		{
			TextView tvLabel = (TextView) findViewById(R.id.uname);
			tvLabel.setText(getIntent().getStringExtra("uname") + "'s "+Constants.yourTag);
			
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tagline, menu);
		return true;
	}

}
