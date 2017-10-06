package com.inzenjer.ova;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Req_myserv extends Activity{
	
	String shmodel,respo,scmpname,shid,shunam;
	String s_cat,s_serv;
	EditText nm,ct,eo,cn,cmnt; Button subm;
	
	String s_nm,s_ct,s_eo,s_cn,s_cmnt,s_amount,stim;
	
	
	  private Spinner spinnercatt,spinnerservv;
	    // array list for spinner adapter
	    private ArrayList<Category> categoriesList,servlist;
	    ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reqmyserv);
		nm=(EditText)findViewById(R.id.et_cardname);
		ct=(EditText)findViewById(R.id.et_cardtype);
		eo=(EditText)findViewById(R.id.et_expdate);
		cn=(EditText)findViewById(R.id.et_cardnum);
		cmnt=(EditText)findViewById(R.id.et_cmnt);
		subm=(Button)findViewById(R.id.bt_submit);
		subm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				s_nm=nm.getText().toString();
				s_ct=ct.getText().toString();
				s_eo=eo.getText().toString();
				s_cn=cn.getText().toString();
				s_cmnt=cmnt.getText().toString();
			
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Calendar calobj = Calendar.getInstance();
				//System.out.println(df.format(calobj.getTime()));
				stim=df.format(calobj.getTime());
				
				
				new Req_update().execute();
				
			}
		});
		
		
		
		
		 spinnercatt = (Spinner) findViewById(R.id.spinnercat);
		 spinnerservv = (Spinner) findViewById(R.id.spinnerserv);
		  categoriesList = new ArrayList<Category>();
		  servlist =new ArrayList<Category>();
		  
		  spinnercatt.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					  s_cat=parent.getItemAtPosition(position).toString();
				  //      Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString() + " Selected" ,Toast.LENGTH_LONG).show();
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
		  spinnerservv.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					  s_serv=parent.getItemAtPosition(position).toString();
				  //      Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString() + " Selected" ,Toast.LENGTH_LONG).show();
					  new Get_estim().execute();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
		
		SharedPreferences share= getSharedPreferences("keymodel", MODE_WORLD_READABLE);
		shmodel=share.getString("srtmodel", "");
		SharedPreferences share1=getSharedPreferences("keyuid", MODE_WORLD_READABLE);
		shid=share1.getString("srtuid", "");
		SharedPreferences share2=getSharedPreferences("keyuanmserv", MODE_WORLD_READABLE);
		shunam=share2.getString("srtuanmserv", "");
		
		new Myvehserv().execute();	
		new Getcat().execute();
		new Getcatserv().execute();
	}
	public class Myvehserv extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String server = getResources().getString(R.string.serverid);
				DefaultHttpClient c= new DefaultHttpClient();
				ResponseHandler<String> resp= new BasicResponseHandler();
				// link to the database phpfile
				HttpPost pmethod = new HttpPost(server+"rcvmodel_det.php");
				List<NameValuePair> npairs= new ArrayList<NameValuePair>(1);
				npairs.add(new BasicNameValuePair("model_nam", shmodel));
				/*npairs.add(new BasicNameValuePair("password", sps));*/
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
				//Toast.makeText(getApplicationContext(), "Hello User !... "+sun, Toast.LENGTH_LONG).show();
				/*Intent i=new Intent(getApplicationContext(),Home_2.class);
				startActivity(i);*/
				parsingmethod();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_LONG).show();
				
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
				scmpname=data1.getString("com_name");			
				Toast.makeText(getApplicationContext(), scmpname, Toast.LENGTH_SHORT).show();		
				//txtestimate.setText("Approximate fare "+sestimate+" /- Rs");

			}
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
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
	            String json = jsonParser.makeServiceCall(server+"get_cat.php", ServiceHandler.GET);
	 
	            Log.e("Response: ", "> " + json);
	 
	            if (json != null) {
	                try {
	                    JSONObject jsonObj = new JSONObject(json);
	                    if (jsonObj != null) {
	                        JSONArray categories = jsonObj
	                                .getJSONArray("cate_name");                        
	 
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
    public class Getcatserv extends AsyncTask<Void, Void, Void>
	{
		  @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(Req_myserv.this);
	            pDialog.setMessage("Fetching sub services..");
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
	                            servlist.add(cat);
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
	
	
    private void populateSpinner2() {
        List<String> lables = new ArrayList<String>();
         
       // txtCategory.setText("");
 
        for (int i = 0; i < servlist.size(); i++) {
            lables.add(servlist.get(i).getName());
        }
 
        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
 
        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
        spinnerservv.setAdapter(spinnerAdapter);
    }
    public class Req_update extends AsyncTask<Void, Void, Void>
    {

    	@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String server = getResources().getString(R.string.serverid);
				DefaultHttpClient c= new DefaultHttpClient();
				ResponseHandler<String> resp= new BasicResponseHandler();
				// link to the database phpfile
				HttpPost pmethod = new HttpPost(server+"req_update.php");
				List<NameValuePair> npairs= new ArrayList<NameValuePair>(10);
				npairs.add(new BasicNameValuePair("user_id", shid));
				npairs.add(new BasicNameValuePair("service_cen", shunam));
				npairs.add(new BasicNameValuePair("make", scmpname));		
				npairs.add(new BasicNameValuePair("model", shmodel));				
				npairs.add(new BasicNameValuePair("categ", s_cat));
				npairs.add(new BasicNameValuePair("service", s_serv));			
				npairs.add(new BasicNameValuePair("name", s_nm ));
				npairs.add(new BasicNameValuePair("cardtype", s_ct));
				npairs.add(new BasicNameValuePair("expireson", s_eo));
				npairs.add(new BasicNameValuePair("cardnumber", s_cn));
				npairs.add(new BasicNameValuePair("amount", s_amount));
				npairs.add(new BasicNameValuePair("time", stim));
				npairs.add(new BasicNameValuePair("comt", s_cmnt));				
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
				Toast.makeText(getApplicationContext(), "Rquest sent successfully. ", Toast.LENGTH_LONG).show();
				
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Req Error...", Toast.LENGTH_LONG).show();
				
			}
			
			super.onPostExecute(result);
			
			
		}
    	
    }
    public class Get_estim extends AsyncTask<Void, Void, Void>
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
				npairs.add(new BasicNameValuePair("service", s_serv));
				/*npairs.add(new BasicNameValuePair("password", sps));*/
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
				//Toast.makeText(getApplicationContext(), "Hello User !... "+sun, Toast.LENGTH_LONG).show();
				/*Intent i=new Intent(getApplicationContext(),Home_2.class);
				startActivity(i);*/
				
				
				
				parsingmethod3();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Error getting estimate", Toast.LENGTH_LONG).show();
				
			}
			
			super.onPostExecute(result);
			
			
		}
		
	}
	public void parsingmethod3()
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
				s_amount=data1.getString("amount");			
				//Toast.makeText(getApplicationContext(), sestimate, Toast.LENGTH_SHORT).show();		
				//txtestimate.setText("Approximate fare "+sestimate+" /- Rs");

			}
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}

	
	

}
