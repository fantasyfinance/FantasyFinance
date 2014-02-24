package com.example.fantasyfinance;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

import com.example.models.HandleXML;
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
			
			HandleXML obj = getValue(stock);
			TextView realTime = (TextView)findViewById(R.id.realTimeValue);
			realTime.setText("Real Time:"+obj.getClose());
			TextView beforeHours = (TextView)findViewById(R.id.beforeHoursValue);
			beforeHours.setText("Before Hours : "+obj.getClose());
			TextView dailylow = (TextView)findViewById(R.id.dailyLow);
			dailylow.setText("Daily Low : "+obj.getLow());
			TextView dailyHigh = (TextView)findViewById(R.id.dailyHigh);
			dailyHigh.setText("Daily High : "+obj.getHigh());
			TextView volume = (TextView)findViewById(R.id.volume);
			volume.setText("Volume : "+obj.getVolume());
			TextView adj_close = (TextView)findViewById(R.id.adj_close);
			adj_close.setText("Adj Close : "+obj.getAdjClose());
			
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

	private HandleXML getValue(String company) {
		Date cDate = new Date();
		HandleXML obj;
		String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
		Calendar cal = Calendar.getInstance();
		int val = cal.get(Calendar.DAY_OF_WEEK);
		if (val == 1) {
			DateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			cal.add(Calendar.DATE, -2);
			fDate = dateFormat.format(cal
					.getTime());
		} else if (val == 7) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			cal.add(Calendar.DATE, -1);
			fDate = dateFormat.format(cal.getTime());
		}

		String url = getUrl(fDate, company);
		obj = new HandleXML(url);
	    obj.fetchXML();
	    while (obj.parsingComplete);
	    Log.d("DEBUG",obj.getClose());
		return obj;
	}

	private String getUrl(String fDate,String company) {
		String url = Constants.URL_HEADER
				+ Constants.query_beforeSymbol
				+ Uri.encode(company)
				+ Constants.query_afterSymbol_beforeStartDate
				+ Uri.encode(fDate)
				+ Constants.query_afterStartDate
				+ Uri.encode(fDate)
				+ Constants.query_afterEndDDate;
		return url;
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
			overridePendingTransition(R.anim.left_out, R.anim.right_in);
			
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
			overridePendingTransition(R.anim.left_out, R.anim.right_in);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stock_info, menu);
		return true;
	}

}
