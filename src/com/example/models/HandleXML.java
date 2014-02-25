package com.example.models;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class HandleXML {
	
	   //private String close = "Close";
	   private String close = "AskRealtime";
	   private String low = "DaysLow";
	   private String high = "DaysHigh";
	   private String open = "Open";
	   private String volume = "Volume";
	   private String adj_close = "LastTradePriceOnly";
	   private String urlString = null;
	   private XmlPullParserFactory xmlFactoryObject;
	   public volatile boolean parsingComplete = true;
	   public HandleXML(String url){
	      this.urlString = url;
	   }
	   public String getClose(){
	      return close;
	   }
	   public String getLow(){
	      return low;
	   }
	   public String getHigh(){
	      return high;
	   }
	   public String getOpen(){
	      return open;
	   }
	   public String getVolume(){
		   return volume;
	   }
	   public String getAdjClose(){
		   return adj_close;
	   }
	   

	   public void parseXMLAndStoreIt(XmlPullParser myParser) {
	      int event;
	      String text=null;
	      try {
	         event = myParser.getEventType();
	         while (event != XmlPullParser.END_DOCUMENT) 
	         {
	            String name=myParser.getName();
	            switch (event)
	            {
	               case XmlPullParser.START_TAG:break;
	               case XmlPullParser.TEXT:text = myParser.getText();
	               							break;

	               case XmlPullParser.END_TAG:if(name.equals("AskRealtime")){
	                     							close = text;
	                     							Log.d("DEBUG","CLOSE");
	                  							}
	                  						  else if(name.equals("DaysHigh")){ 	
	                  							  //high = myParser.getAttributeValue(null,"value");
	                  							  high = text;
	                  						  }
	                  						  else if(name.equals("Open")){
	                  							  //open = myParser.getAttributeValue(null,"value");
	                  							  open = text;
	                  						  }
	                  						  else if(name.equals("DaysLow")){
	                  							  //low = myParser.getAttributeValue(null,"value");
	                  							  low = text;
	                  						  }
	                  						  else if(name.equals("Volume")){
	                  							  volume = text;
	                  						  }
	                  						  else if(name.equals("LastTradePriceOnly")){
	                  							  adj_close = text;
	                  						  }
	                  						  else{
	                  							  
	                  						  }
	                  						break;
	            }		 
	            event = myParser.next(); 

	       }
	       parsingComplete = false;
	      } catch (Exception e) {
	         e.printStackTrace();
	      }

	   }
	   public void fetchXML(){
	      Thread thread = new Thread(new Runnable(){
	         @Override
	         public void run() {
	            try {
	               URL url = new URL(urlString);
	               HttpURLConnection conn = (HttpURLConnection) 
	               url.openConnection();
	                  conn.setReadTimeout(10000 /* milliseconds */);
	                  conn.setConnectTimeout(15000 /* milliseconds */);
	                  conn.setRequestMethod("GET");
	                  conn.setDoInput(true);
	                  conn.connect();
	            InputStream stream = conn.getInputStream();

	            xmlFactoryObject = XmlPullParserFactory.newInstance();
	            XmlPullParser myparser = xmlFactoryObject.newPullParser();

	            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES
	            , false);
	            myparser.setInput(stream, null);
	            parseXMLAndStoreIt(myparser);
	            stream.close();
	            } catch (Exception e) {
	               e.printStackTrace();
	            }
	        }
	    });

	    thread.start(); 


	   }

}
