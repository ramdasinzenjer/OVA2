package com.inzenjer.ova;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.inzenjer.ova.extra.Category;
import com.inzenjer.ova.extra.ServiceHandler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class RSAssist extends Activity implements OnItemSelectedListener,
	OnClickListener  {
	Button button;
	ListView listView;
	ArrayAdapter<String> adapter;

	
	 private ArrayList<Category> vehiclelist;
	 private ArrayList<Category> vehiclemodel;
	    ProgressDialog pDialog;
	 
	    // API urls
	   // private String URL_CATEGORIES = "http://192.168.1.8/Ova/vlistT.php";
	    String vid;
	/*ArrayAdapter<String> sap1,sap2;
	Spinner sp_vh,sp_vhmod;
	String[] veh_list={"BMW","Aston Martin","Mercedes","Porsche","Suzuki","Nissan","Audi","Bentley"};*/
	String sv,svm;
	
	Spinner sp_vh,sp_vhmod;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.rsassist);
	sp_vh=(Spinner)findViewById(R.id.spinner_vlist);
    sp_vhmod=(Spinner)findViewById(R.id.spinner_vmodel);
    
    vehiclelist = new ArrayList<Category>();
    vehiclemodel = new ArrayList<Category>();
    
    // spinner item select listener
    sp_vh.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			  sv=parent.getItemAtPosition(position).toString();
		        Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString() + " Selected" ,Toast.LENGTH_LONG).show();
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	});
    sp_vhmod.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			  vid=parent.getItemAtPosition(position).toString();
		        Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString() + " Selected" ,Toast.LENGTH_LONG).show();
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	});
    
	findViewsById();
	
	String[] sports = getResources().getStringArray(R.array.vehicledefect_array);
	adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_multiple_choice, sports);
	listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	listView.setAdapter(adapter);
	
	button.setOnClickListener(this);
	
	new Vehlist().execute();
	
	 
	}
	
	/**
     * Adding spinner data
     * */
    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();
         
       // txtCategory.setText("");
 
        for (int i = 0; i < vehiclelist.size(); i++) {
            lables.add(vehiclelist.get(i).getName());
        }
 
        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
 
        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
        sp_vh.setAdapter(spinnerAdapter);
        
    }
    
    private void populateSpinner2() {
    	
    	
        List<String> lables = new ArrayList<String>();
         
       // txtCategory.setText("");
 
        for (int i = 0; i < vehiclemodel.size(); i++) {
            lables.add(vehiclemodel.get(i).getName());
        }
 
        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
 
        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
        sp_vhmod.setAdapter(spinnerAdapter);
    }
	
	private void findViewsById() {
	listView = (ListView) findViewById(R.id.list);
	button = (Button) findViewById(R.id.bt_rqstasst);
	}
	
	public void onClick(View v) {
	SparseBooleanArray checked = listView.getCheckedItemPositions();
	ArrayList<String> selectedItems = new ArrayList<String>();
	for (int i = 0; i < checked.size(); i++) {
	    // Item position in adapter
	    int position = checked.keyAt(i);
	    // Add sport if it is checked i.e.) == TRUE!
	    if (checked.valueAt(i))
	        selectedItems.add(adapter.getItem(position));
	}
	
	String[] outputStrArr = new String[selectedItems.size()];
	
	for (int i = 0; i < selectedItems.size(); i++) {
	    outputStrArr[i] = selectedItems.get(i);
	    Toast.makeText(getApplicationContext(), outputStrArr[i], Toast.LENGTH_SHORT).show();
	}
	
	Intent intent = new Intent(getApplicationContext(),
	        ResultActivity.class);
	
	// Create a bundle object
	Bundle b = new Bundle();
	b.putStringArray("selectedItems", outputStrArr);
	
	// Add the bundle to the intent.
	intent.putExtras(b);
	
	// start the ResultActivity
	startActivity(intent);
	
}
	
	class Vehlist extends AsyncTask<Void, Void, Void>
	  {
		  @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	           /* pDialog = new ProgressDialog(RSAssist.this);
	            pDialog.setMessage("Vehicle categories..");
	            pDialog.setCancelable(false);
	            pDialog.show();*/
	 
	        }
	 
	        @Override
	        protected Void doInBackground(Void... arg0) {
	        	String server = getResources().getString(R.string.serverid);
	            ServiceHandler jsonParser = new ServiceHandler();
	            String json = jsonParser.makeServiceCall(server+"vlistT.php", ServiceHandler.GET);
	 
	            Log.e("Response: ", "> " + json);
	 
	            if (json != null) {
	                try {
	                    JSONObject jsonObj = new JSONObject(json);
	                    if (jsonObj != null) {
	                        JSONArray categories = jsonObj
	                                .getJSONArray("categories");                        
	 
	                        for (int i = 0; i < categories.length(); i++) {
	                            JSONObject catObj = (JSONObject) categories.get(i);
	                            Category cat = new Category(catObj.getInt("id"),
	                                    catObj.getString("name"));
	                            vehiclelist.add(cat);
	                            
	                            
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
	                pDialog.dismiss();*/
	            populateSpinner();
	           
	            new Vehmodel().execute();
	        }
	 
	
	  }

	class Vehmodel extends AsyncTask<Void, Void, Void>
	  {
		  @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(RSAssist.this);
	            pDialog.setMessage("Fetching models..");
	            pDialog.setCancelable(false);
	            pDialog.show();
	 
	        }
	 
	        @Override
	        protected Void doInBackground(Void... arg0) {
	        	String server = getResources().getString(R.string.serverid);
	            ServiceHandler jsonParser = new ServiceHandler();
	            /*List<NameValuePair> npairs= new ArrayList<NameValuePair>(1);
				npairs.add(new BasicNameValuePair("id", vid));*/
	            String json = jsonParser.makeServiceCall(server+"vlistTmodel.php", ServiceHandler.GET);
	 
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
	                            vehiclemodel.add(cat);
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
	            populateSpinner2();
	            
	        }
	 
	
	  }
	   @Override
	    public void onItemSelected(AdapterView<?> parent, View view, int position,
	            long id) {
		  
	        /*if(sv.contains("BMW"))
            {
            	vid="1";
            	sp_vhmod.setAdapter(null);
            	new Vehmodel().execute();
            	
            	populateSpinner2();
            }
            else if(sv.contains("AUDI"))
            {
            	vid="2";
            	sp_vhmod.setAdapter(null);
            	new Vehmodel().execute();
            	
            	populateSpinner2();
            }
            else if(sv.contains("HONDA"))
            {
            	vid="3";
            	sp_vhmod.setAdapter(null);
            	new Vehmodel().execute();
            	
            	populateSpinner2();
            }*/
	 
	       
	   }
	 
	    @Override
	    public void onNothingSelected(AdapterView<?> arg0) {  
	    	
	    	
	    }
}