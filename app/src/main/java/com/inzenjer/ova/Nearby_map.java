package com.inzenjer.ova;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi") public class Nearby_map extends FragmentActivity {
	
	GoogleMap googleMap;
	MarkerOptions markerOptions;
	LatLng[] latLng= new LatLng[10];
	LatLng llg;
	ProgressDialog pDialog;
	String respo;
	
	String slat,slng,stitle;
	
	Spinner sp_dist;
	ArrayAdapter<String> arr_adp_dist;
	String sdist_sing;
	String[] sc={"Select District","Thiruvananthapuram","Kollam","Pathananmthitta","Alapuzha","Kottayam","Idukki"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_map);
		
		sp_dist=(Spinner)findViewById(R.id.spinner_dist01);
        arr_adp_dist=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sc);
	    arr_adp_dist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_dist.setAdapter(arr_adp_dist);
        sp_dist.setOnItemSelectedListener(new OnItemSelectedListener()
        {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				sdist_sing=arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
		
		
		SupportMapFragment supportMapFragment = (SupportMapFragment) 
				getSupportFragmentManager().findFragmentById(R.id.map);
		// Getting a reference to the map
		googleMap = supportMapFragment.getMap();
		CameraUpdate center=
	            CameraUpdateFactory.newLatLng(new LatLng(10.850500, 76.271100));
	        CameraUpdate zoom=CameraUpdateFactory.zoomTo(10);

	        googleMap.moveCamera(center);
	        googleMap.animateCamera(zoom);
		
		// Getting reference to btn_find of the layout activity_main
        Button btn_find = (Button) findViewById(R.id.btn_find);
        
        
        
        // Defining button click event listener for the find button
        OnClickListener findClickListener = new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				
				//if(location!=null && !location.equals("")){
					new GeocoderTask().execute(sdist_sing);
			//	}
			}
		};
		
		// Setting button click event listener for the find button
		btn_find.setOnClickListener(findClickListener);		
				
	}
	private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{

		@Override
		protected List<Address> doInBackground(String... locationName) {
			// Creating an instance of Geocoder class
			Geocoder geocoder = new Geocoder(getBaseContext());
			List<Address> addresses = null;
			
			try {
				// Getting a maximum of 3 Address that matches the input text
				addresses = geocoder.getFromLocationName(locationName[0], 3);
			} catch (IOException e) {
				e.printStackTrace();
			}			
			return addresses;
		}
				
		@Override
		protected void onPostExecute(List<Address> addresses) {			
	        
	        if(addresses==null || addresses.size()==0){
				Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
			}
	        
	        // Clears all the existing markers on the map
	        googleMap.clear();
			
	        // Adding Markers on Google Map for each matching address
			for(int i=0;i<addresses.size();i++){				
				
				Address address = (Address) addresses.get(i);
			String s="8.476216";
			double d=Double.valueOf(s).doubleValue();
				
		        // Creating an instance of GeoPoint, to display in Google Map
		       llg = new LatLng(address.getLatitude(), address.getLongitude());
				/*latLng[0]= new LatLng(d, 76.97124);
		        latLng[1] = new LatLng(8.514925	,76.988406);
		        latLng[2] = new LatLng(8.614925, 76.988406);*/
		        
		        String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());
        
		        /*for (int j = 0; j < latLng.length; j++) {
		        	drawMarker(latLng[j]);
		        }*/
		        new Mapfrom_db().execute();
		       
		        
			}			
		}		
	}
	private void drawMarker(LatLng point) {
	    // Creating an instance of MarkerOptions
	    MarkerOptions markerOptions = new MarkerOptions();

	    MarkerOptions markerOptionsc=new MarkerOptions();
	    // Setting latitude and longitude for the marker
	    
	    markerOptions.position(point);
	    
	    markerOptions.title(""+stitle);

	    markerOptionsc.position(llg);
	    markerOptionsc.title("center");

	    double radiusInMeters = 100.0;
	     //red outline
	    int strokeColor = 0xffff0000;
	    //opaque red fill
	    int shadeColor = 0x44ff0000; 


	    CircleOptions circleOptions = new CircleOptions().center(llg).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
	    googleMap.addCircle(circleOptions);
	    // Adding marker on the Google Map
	    googleMap.addMarker(markerOptions);
	    
	  //  googleMap.addMarker(markerOptionsc);
	    CameraUpdate center=
	            CameraUpdateFactory.newLatLng(llg);
	        CameraUpdate zoom=CameraUpdateFactory.zoomTo(10);

	        googleMap.moveCamera(center);
	        googleMap.animateCamera(zoom);
	        
	        
	        final Circle circle = googleMap.addCircle(new CircleOptions().center(llg)
	                .strokeColor(Color.CYAN).radius(1000));

	        ValueAnimator vAnimator = new ValueAnimator();
	        vAnimator.setRepeatCount(ValueAnimator.INFINITE);
	        vAnimator.setRepeatMode(ValueAnimator.RESTART);  /* PULSE */
	        vAnimator.setIntValues(0, 100);
	        vAnimator.setDuration(1000);
	        vAnimator.setEvaluator(new IntEvaluator());
	        vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
	        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	            @Override
	            public void onAnimationUpdate(ValueAnimator valueAnimator) {
	                float animatedFraction = valueAnimator.getAnimatedFraction();
	                // Log.e("", "" + animatedFraction);
	                circle.setRadius(animatedFraction * 100);
	            }
	        });
	        vAnimator.start();
	        
	}
	public class Mapfrom_db extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Nearby_map.this);
            pDialog.setMessage("Vehicle categories..");
            pDialog.setCancelable(false);
            pDialog.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			try {
				String server = getResources().getString(R.string.serverid);
				DefaultHttpClient c= new DefaultHttpClient();
				ResponseHandler<String> resp= new BasicResponseHandler();
				// link to the database phpfile
				HttpPost pmethod = new HttpPost(server+"sclistret.php");
				List<NameValuePair> npairs= new ArrayList<NameValuePair>(1);
				npairs.add(new BasicNameValuePair("dist", sdist_sing));
				pmethod.setEntity(new UrlEncodedFormEntity(npairs));
				respo=c.execute(pmethod,resp);			
		
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error:"+e);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pDialog.isShowing())
                pDialog.dismiss();
			if(respo.contains("success"))
			{
				//Toast.makeText(getApplicationContext(), ""+respo, Toast.LENGTH_LONG).show();
	
				parsingmethod();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "No Registered Service center found. ", Toast.LENGTH_LONG).show();
				googleMap.clear();
			}
		}
		
	}
	public void parsingmethod()
	{
		try
		{
			JSONObject jobject=new JSONObject(respo);
			JSONObject jobject1=jobject.getJSONObject("Event");
			JSONArray ja=jobject1.getJSONArray("Details");
			int length=ja.length();
			for(int i=0;i<length;i++)
			{
				JSONObject data1=ja.getJSONObject(i);
				slat=data1.getString("latt");
				slng=data1.getString("long");
				stitle=data1.getString("address1");
				double dlt,dlng;
				dlt=Double.valueOf(slat).doubleValue();
				dlng=Double.valueOf(slng).doubleValue();
				latLng[i]= new LatLng(dlt, dlng);
				drawMarker(latLng[i]);
				
				
				/*ema=data1.getString("Email");
				pho=data1.getString("Phonenumber");*/
				//Toast.makeText(getApplicationContext(), "service centers recieved", Toast.LENGTH_SHORT).show();	
				/*SharedPreferences share=getSharedPreferences("keyuid", MODE_WORLD_READABLE);
				SharedPreferences.Editor ed=share.edit();
				ed.putString("srtuid",s_uid);
				ed.commit();*/				
			}
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}
	
	
	
}
