package com.inzenjer.ova;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.inzenjer.ova.extra.Category;
import com.inzenjer.ova.extra.ServiceHandler;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class Veh_status extends Activity {

	private Spinner spinnercatt;
	private ArrayList<Category> categoriesList;
	
	String scat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.veh_status);
		
		spinnercatt = (Spinner) findViewById(R.id.spinner_onveh);
		categoriesList = new ArrayList<Category>();
		spinnercatt.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				  scat=parent.getItemAtPosition(position).toString();
			  //      Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString() + " Selected" ,Toast.LENGTH_LONG).show();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		new Getcat().execute();
	}
	public class Getcat extends AsyncTask<Void, Void, Void>
	{
		  @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	           /* pDialog = new ProgressDialog(Req_myserv.this);
	            pDialog.setMessage("Fetching categories");
	            pDialog.setCancelable(false);
	            pDialog.show();*/
	 
	        }
	 
	        @Override
	        protected Void doInBackground(Void... arg0) {
	        	
	        	String server = getResources().getString(R.string.serverid);
	            ServiceHandler jsonParser = new ServiceHandler();
	            String json = jsonParser.makeServiceCall(server+"get_cat222.php", ServiceHandler.GET);
	 
	            Log.e("Response: ", "> " + json);
	 
	            if (json != null) {
	                try {
	                    JSONObject jsonObj = new JSONObject(json);
	                    if (jsonObj != null) {
	                        JSONArray categories = jsonObj
	                                .getJSONArray("model");                        
	 
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
	           /* if (pDialog.isShowing())
	                pDialog.dismiss();
*/	            populateSpinner();
	        }
	}
	private void populateSpinner() {
        List<String> lables = new ArrayList<String>();
         
       // txtCategory.setText("");
 
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
        spinnercatt.setAdapter(spinnerAdapter);
    }
}
