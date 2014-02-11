package com.example.fantasyfinance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
