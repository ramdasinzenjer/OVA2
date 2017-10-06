package com.inzenjer.ova;

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
import org.json.JSONException;
import org.json.JSONObject;

import com.inzenjer.ova.extra.Category;
import com.inzenjer.ova.extra.ServiceHandler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Get_estimate extends Activity{

	
	 private Button btnservlist;
	    private TextView txtestimate;
	    String sestimate,respo,sserv ;
	    private Spinner spinnerService;
	    // array list for spinner adapter
	    private ArrayList<Category> categoriesList;
	    ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getestimate);
		
		txtestimate = (TextView)findViewById(R.id.tv_estimate);
		 btnservlist = (Button) findViewById(R.id.bt_getestimate);
		  spinnerService = (Spinner) findViewById(R.id.spinner_servlist);
		  categoriesList = new ArrayList<Category>();
		  
	        // spinner item select listener
		  spinnerService.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				 Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString() + " Selected" ,Toast.LENGTH_LONG).show();
				 sserv=parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		  
		  btnservlist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//getting estimate
				new Get_esti().execute();
			}
		});
		  new Getserv_list().execute();
	}
	
	public class Getserv_list extends AsyncTask<Void, Void, Void>
	{
		  @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(Get_estimate.this);
	            pDialog.setMessage("Fetching food categories..");
	            pDialog.setCancelable(false);
	            pDialog.show();
	 
	        }
	 
	        @Override
	        protected Void doInBackground(Void... arg0) {
	        	
	        	String server = getResources().getString(R.string.serverid);
	            ServiceHandler jsonParser = new ServiceHandler();
	            String json = jsonParser.makeServiceCall(server+"get_services.php", ServiceHandler.GET);
	 
	            Log.e("Response: ", "> " + json);
	 
	            if (json != null) {
	                try {
	                    JSONObject jsonObj = new JSONObject(json);
	                    if (jsonObj != null) {
	                        JSONArray categories = jsonObj
	                                .getJSONArray("service");                        
	 
	                        for (int i = 0; i < categories.length(); i++) {
	                            JSONObject catObj = (JSONObject) categories.get(i);
	                            Category cat = new Category(catObj.getInt("id"),
	                                    catObj.getString("name"));
	                            categoriesList.add(cat);
	                        }
	                    }
	 
	                } catch (JSONException e) {
	                    e.printStackTrace();
	                }
	 
	            } else {
	                Log.e("JSON Data", "Didn't receive any data from server!");
	            }
	 
	            return null;
	        }
	 
	        @Override
	        protected void onPostExecute(Void result) {
	            super.onPostExecute(result);
	            if (pDialog.isShowing())
	                pDialog.dismiss();
	            populateSpinner();
	        }
	}
	
	
    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();
 
        for (int i = 0; i < categoriesList.size(); i++) {
            lables.add(categoriesList.get(i).getName());
        }
 
        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
 
        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
        spinnerService.setAdapter(spinnerAdapter);
    }
    
	public class Get_esti extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String server = getResources().getString(R.string.serverid);
				DefaultHttpClient c= new DefaultHttpClient();
				ResponseHandler<String> resp= new BasicResponseHandler();
				// link to the database phpfile
				HttpPost pmethod = new HttpPost(server+"get_estimate.php");
				List<NameValuePair> npairs= new ArrayList<NameValuePair>(1);
				npairs.add(new BasicNameValuePair("service", sserv));
				
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
			if(respo.contains("success"))
			{	
				parsingmethod();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Error in estimate retrieal", Toast.LENGTH_LONG).show();
				
			}
			
			super.onPostExecute(result);
			
			
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
				sestimate=data1.getString("amount");			
				Toast.makeText(getApplicationContext(), sestimate, Toast.LENGTH_SHORT).show();		
				txtestimate.setText("Approximate estimate "+sestimate+" /- Rs");

			}
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}

	
}
