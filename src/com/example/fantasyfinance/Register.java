package com.example.fantasyfinance;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.example.utils.Constants;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Register extends Activity {

	ParseUser currentUser;
	final Context context = this;
	EditText usernameField;
	EditText passwordField;
	EditText emailField;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
		
	}
	
	public void onRegisterClick(View v) {
		//Toast.makeText(getApplicationContext(), "Clicked Register !!!",Toast.LENGTH_LONG).show();
		usernameField = (EditText)findViewById(R.id.registerUsername);
		final String username = usernameField.getText().toString();
		passwordField = (EditText)findViewById(R.id.registerpwd);
		final String pwd = usernameField.getText().toString();
		emailField = (EditText)findViewById(R.id.emailID);
		final String email = emailField.getText().toString();
		Parse.initialize(this, Constants.APPLICATION_KEY,
				Constants.APPLICATION_KEY_TOKEN);
		currentUser = ParseUser.getCurrentUser();
		if(currentUser == null)
		{
			boolean clientValidation = validateAllFields(username,pwd,email);
			
			//code which says to enter correct credentials
			if(!clientValidation)
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder.setTitle("Incorrect Crendentials");
				alertDialogBuilder.setMessage("Please Enter Correct Credentials").setCancelable(false)
					.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							
						}
					  });
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
			
			//code which checks whether username is already present or not
			else
			{
				ParseQuery<ParseObject> query = ParseQuery.getQuery("TaglineModel");
				query.whereEqualTo("user", username);
				query.findInBackground(new FindCallback<ParseObject>() 
				{
					public void done(List<ParseObject> users, ParseException e) 
					{
						if (users.size() > 0 && e == null) 
						{
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setTitle("Username already taken");
							alertDialogBuilder.setMessage("Click Ok to choose a different name!").setCancelable(false)
								.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										
									}
								  });
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						} 
						else if(e != null) {
							Log.d("DEBUG", "Error: " + e.getMessage());
						}
						else if(users.size() == 0 && e == null)
						{
							Log.d("DEBUG", "This is valid = "+username);
							//insert in TaglineModel and User table
							
							ParseUser user = new ParseUser();
							user.setUsername(username);
							user.setPassword(pwd);
							user.setEmail(email);
							
							user.signUpInBackground(new SignUpCallback() {
								  public void done(ParseException e) {
								    if (e == null) {
								    	ParseObject tagInfo = new ParseObject("TaglineModel");
										tagInfo.put("user", username);
										tagInfo.put("tag", "Welcome!!!");
										tagInfo.saveEventually();
								    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
										alertDialogBuilder.setTitle("Successfully Registered!!!");
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
					}
				});
				
			}
			
			
		}
		
		//code which makes the user logout from the application inorder to register
		else
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle("Logging out to create an account");
			alertDialogBuilder.setMessage("Click Ok to exit!").setCancelable(false)
				.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						ParseUser.logOut();
						currentUser = ParseUser.getCurrentUser();
						invalidateOptionsMenu();
						Intent logout = new Intent(getApplicationContext(), Home.class);
						startActivity(logout);
						overridePendingTransition(R.anim.left_out, R.anim.right_in);
						
					}
				  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
	}
	private boolean validateAllFields(String uname,String pwd,String email) {
		// TODO Auto-generated method stub
		if (uname.matches("")|| pwd.matches("")||email.matches(""))
		{
			return false;
		}
		else
		{
			return true;
		}
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
