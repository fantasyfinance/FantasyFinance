package com.example.fantasyfinance;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.models.HandleXML;
import com.example.utils.Constants;
import com.parse.FindCallback;
import com.parse.GetCallback;
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
	ImageButton upButton;
	ImageButton downButton;
	final Context context = this;
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
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery("UserStockPreference");
			query.whereEqualTo("user", username);
			query.whereEqualTo("stock", stock);
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
			
			ParseQuery<ParseObject> predict_query = ParseQuery.getQuery("Predict");
			predict_query.whereEqualTo("user", username);
			predict_query.whereEqualTo("stock", stock);
			predict_query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> records, ParseException e) {
					if (records.size() > 0 && e == null) 
					{
						// so write the code to fetch prediction value
						String id = records.get(0).getObjectId();
						ParseQuery<ParseObject> prediction_value_query = ParseQuery.getQuery("Predict");
						prediction_value_query.getInBackground(id,new GetCallback<ParseObject>() 
								{
									public void done(ParseObject predictionObject,ParseException e) 
									{
										if (e == null) 
										{
											String prediction_Value = predictionObject.get("prediction").toString();
											if(prediction_Value.equals(Constants.not_Applicable))
											{
												upButton = (ImageButton) findViewById(R.id.upButton);
												downButton = (ImageButton) findViewById(R.id.downButton);
												upButton.setVisibility(View.VISIBLE);
												downButton.setVisibility(View.VISIBLE);
											}
										}
									}
								});
					} else if (e != null) {
						Log.d("DEBUG", "Error: " + e.getMessage());
					} else if (records.size() == 0 && e == null) {
						upButton = (ImageButton) findViewById(R.id.upButton);
						downButton = (ImageButton) findViewById(R.id.downButton);
						upButton.setVisibility(View.VISIBLE);
						downButton.setVisibility(View.VISIBLE);
					}
				}
			});
		}
		else {
			Intent intent = new Intent(this, Home.class);
			startActivity(intent);
		}
	}

	private HandleXML getValue(String company) {
		Date cDate = new Date();
		HandleXML obj;
		String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
		Calendar cal = Calendar.getInstance();
		int val = cal.get(Calendar.DAY_OF_WEEK);
		if (val == 1) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			cal.add(Calendar.DATE, -2);
			fDate = dateFormat.format(cal.getTime());
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

/*	private String getUrl(String fDate,String company) {
		String url = Constants.URL_HEADER
				+ Constants.query_beforeSymbol
				+ Uri.encode(company)
				+ Constants.query_afterSymbol_beforeStartDate
				+ Uri.encode(fDate)
				+ Constants.query_afterStartDate
				+ Uri.encode(fDate)
				+ Constants.query_afterEndDDate;
		return url;
	}*/
	
	private String getUrl(String fDate,String company) {
		String url = Constants.URL_HEADER
				+ Constants.query_beforeSymbol
				+ Uri.encode(company)
				+ Constants.query_afterSymbol_beforeStartDate;
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

	public void onUpClick(View v)
	{
		if (currentUser != null) {
			p1_button = (Button) findViewById(R.id.button1);
			if(p1_button.getText().equals(Constants.follow))
			{
				ParseObject prediction = new ParseObject("Predict");
				prediction.put("user", username);
				prediction.put("stock", stock);
				prediction.put("lock", "yes");
				prediction.put("prediction", "HIGH");
				prediction.saveInBackground();
				Toast.makeText(getApplicationContext(),stock+" is predicted HIGH", Toast.LENGTH_LONG).show();
			}	
			else if (p1_button.getText().equals(Constants.unFollow))
			{
				ParseQuery<ParseObject> update_query = ParseQuery.getQuery("Predict");
				update_query.whereEqualTo("user", username);
				update_query.whereEqualTo("stock", stock);
				update_query.whereEqualTo("lock", "no");
				update_query.findInBackground(new FindCallback<ParseObject>() 
				{
					public void done(List<ParseObject> predict_object, ParseException e) 
					{
						if (predict_object.size()==1 && e == null) 
						{
							String objectID = predict_object.get(0).getObjectId();
							ParseQuery<ParseObject> query = ParseQuery.getQuery("Predict");
							query.getInBackground(objectID,new GetCallback<ParseObject>() 
									{
										public void done(ParseObject predict_line,ParseException e) 
										{
											if (e == null) 
											{
												predict_line.put("user", username);
												predict_line.put("stock",stock);
												predict_line.put("lock","yes");
												predict_line.put("prediction", "HIGH");
												predict_line.saveInBackground();
												Toast.makeText(getApplicationContext(),stock+" is predicted HIGH", Toast.LENGTH_LONG).show();
												
											}
										}
									});
						} else {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setTitle("Stock Already Predicted");
							alertDialogBuilder.setMessage("Please Choose a Different Stock").setCancelable(false)
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
		else 
		{
				Intent intent = new Intent(this, Home.class);
				startActivity(intent);
		}
	}
	
	public void onDownClick(View v)
	{
		p1_button = (Button) findViewById(R.id.button1);
		if(p1_button.getText().equals(Constants.follow))
		{
			ParseObject prediction = new ParseObject("Predict");
			prediction.put("user", username);
			prediction.put("stock", stock);
			prediction.put("lock", "yes");
			prediction.put("prediction", "LOW");
			prediction.saveInBackground();
			Toast.makeText(getApplicationContext(),stock+" is predicted LOW", Toast.LENGTH_LONG).show();
		}
		else if (p1_button.getText().equals(Constants.unFollow))
		{
			ParseQuery<ParseObject> update_query = ParseQuery.getQuery("Predict");
			update_query.whereEqualTo("user", username);
			update_query.whereEqualTo("stock", stock);
			update_query.whereEqualTo("lock", "no");
			update_query.findInBackground(new FindCallback<ParseObject>() 
			{
				public void done(List<ParseObject> predict_object, ParseException e) 
				{
					if (predict_object.size()==1 && e == null) 
					{
						String objectID = predict_object.get(0).getObjectId();
						ParseQuery<ParseObject> query = ParseQuery.getQuery("Predict");
						query.getInBackground(objectID,new GetCallback<ParseObject>() 
								{
									public void done(ParseObject predict_line,ParseException e) 
									{
										if (e == null) 
										{
											predict_line.put("user", username);
											predict_line.put("stock",stock);
											predict_line.put("lock","yes");
											predict_line.put("prediction", "LOW");
											predict_line.saveInBackground();
											Toast.makeText(getApplicationContext(),stock+" is predicted LOW", Toast.LENGTH_LONG).show();
										}
									}
								});
					} else {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
						alertDialogBuilder.setTitle("Stock Already Predicted");
						alertDialogBuilder.setMessage("Please Choose a Different Stock").setCancelable(false)
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
