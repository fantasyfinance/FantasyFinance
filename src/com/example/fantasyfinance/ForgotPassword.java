package com.example.fantasyfinance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.example.utils.Constants;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgotPassword extends Activity {

	final Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		
		
	}

	public void onForgotClick(View v) {
		EditText et = (EditText) findViewById(R.id.forgotemailID);
		String enteredEmail = et.getText().toString();
		Parse.initialize(this, Constants.APPLICATION_KEY,
				Constants.APPLICATION_KEY_TOKEN);
		sendEmail(enteredEmail);
	}
	private void sendEmail(String enteredEmail) {
		ParseUser.requestPasswordResetInBackground(enteredEmail,
				new RequestPasswordResetCallback() {
					public void done(ParseException e) {
						if (e == null) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setTitle("Email Sent!!");
							alertDialogBuilder.setMessage("Click Ok to Login!!!").setCancelable(false)
								.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										Intent logout = new Intent(getApplicationContext(), Home.class);
										startActivity(logout);
										overridePendingTransition(R.anim.left_out, R.anim.right_in);
									}
								  });
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						} else {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setTitle("Invalid email address!!!");
							alertDialogBuilder.setMessage("Please give a valid email address").setCancelable(false)
								.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										
									}
								  });
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgot_password, menu);
		return true;
	}

}
