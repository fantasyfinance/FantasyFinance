package com.example.fantasyfinance;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.utils.Constants;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseUser;

public class Home extends Activity {

	EditText username;
	EditText password;
	AlertDialog.Builder dlgAlert;
	final Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
	}

	public void onLoginClick(View v) {
		Parse.initialize(this, Constants.APPLICATION_KEY,
				Constants.APPLICATION_KEY_TOKEN);
		username = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
			@Override
			public void done(ParseUser user, com.parse.ParseException e) {
				// TODO Auto-generated method stub
				if (user != null) {

					//Toast.makeText(getApplicationContext(), "Hello",Toast.LENGTH_LONG).show();
					Intent i = new Intent(getApplicationContext(),Login.class);
					i.putExtra("username",username.getText().toString() );
					i.putExtra("password", password.getText().toString());
					startActivity(i);
					
				} else {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
						alertDialogBuilder.setTitle("Incorrect Username and Password");
						alertDialogBuilder.setMessage("Click Ok to exit!").setCancelable(false)
							.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, close
									// current activity
									
								}
							  });
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
                }
			}
		});

		
	}
	
	public void onRegisterClick(View v) {
		//Toast.makeText(getApplicationContext(), "Clicked Register !!!",Toast.LENGTH_LONG).show();
		Intent i = new Intent(getApplicationContext(),Register.class);
		startActivity(i);
	}

	public void onForgotPassword(View v) {
		//Toast.makeText(getApplicationContext(), "Clicked Forgot !!!", Toast.LENGTH_LONG).show();
		Intent i = new Intent(getApplicationContext(),ForgotPassword.class);
		startActivity(i);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
