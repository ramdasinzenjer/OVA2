package com.inzenjer.ova;

import java.util.ArrayList;
import java.util.HashMap;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Garage extends Activity{
	
	Button bt_addvh;
	
	EditText ecmp,emod,ereg,eyear;
	String scmp,smod,sreg,syear,respo;
	
	 ListView list11; TextView model_name; 
	 String shared_uid;
	  int length;
	  String response,value1,s_modelname,s_compname,s_reg_no;
	  
	  String scn,srg,smn;
	  ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	 
	  JSONArray android = null;
	  Vehicle_list vl=new Vehicle_list();
	  Add_vehicle avl=new Add_vehicle();
	  
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.garagevehicle_list);
		
		ecmp=(EditText)findViewById(R.id.et_comp);
		emod=(EditText)findViewById(R.id.et_model);
		ereg=(EditText)findViewById(R.id.et_regnum);
		eyear=(EditText)findViewById(R.id.et_year);
		
		
		model_name=(TextView)findViewById(R.id.singletext);
		oslist = new ArrayList<HashMap<String, String>>();
		
		
		SharedPreferences share=this.getSharedPreferences("keyuid", MODE_WORLD_READABLE);
		shared_uid=share.getString("srtuid","");
		
		
		
		vl.execute();
		
		bt_addvh=(Button)findViewById(R.id.bt_addvehicle);
		
		bt_addvh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				scmp=ecmp.getText().toString();
				smod=emod.getText().toString();
				sreg=ereg.getText().toString();
				syear=eyear.getText().toString();
				if(scmp.equals("")||smod.equals("")||sreg.equals("")||syear.equals("")){
					Toast.makeText(getApplicationContext(), "Empty field detected...", Toast.LENGTH_SHORT).show();
				}
				else {
				avl.execute();
				}
			}
		});
		
	}
	
	
	class Vehicle_list extends AsyncTask<Void, Void, Void>
	  {
		 
		
		  @Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
		try 
		{
			String serv = getResources().getString(R.string.serverid);
			DefaultHttpClient hc=new DefaultHttpClient();
			ResponseHandler<String> res=new BasicResponseHandler();
			HttpPost postMethod=new HttpPost(serv+"veh_listret.php");
			List<NameValuePair>nameValuePairs=new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("user_id",shared_uid));
			
			postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		 response= hc.execute(postMethod,res);
		 
		 System.out.println(response);
			
		}
		catch(Exception e)
		{
			System.out.println("Error"+e);
		}
		
		return null;
		}
		  @Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			parsingmethod();
		}
	  }

	
	
	 public void parsingmethod(){
		  try
		  {
			  JSONObject job=new JSONObject(response);
			  JSONObject job1=job.getJSONObject("Event");
			  JSONArray ja=job1.getJSONArray("Details");
			  length=ja.length();
			  for(int i=0;i<length;i++)
			  {
				  JSONObject c=ja.getJSONObject(i);
				  //storing json item in variable
				  s_modelname=c.getString("model_nam");
				  s_compname=c.getString("com_name");
				  s_reg_no=c.getString("reg_no");
				  
				  //adding value hashmap key to value
				  HashMap<String, String> map=new HashMap<String, String>();
				  map.put("model_nam", s_modelname);
				  map.put("com_name", s_compname);
				  map.put("reg_no", s_reg_no);
				 
				  oslist.add(map);
				  list11=(ListView)findViewById(R.id.vlist);
				  ListAdapter adapter=new SimpleAdapter(getApplicationContext(), oslist, 
						  R.layout.list_single,
						  new String[] {"model_nam"}, new int[] {R.id.singletext});
				  list11.setAdapter(adapter);
				  list11.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						TextView text=(TextView)view.findViewById(R.id.singletext);
						value1=text.getText().toString().trim();
						Toast.makeText(getApplicationContext(), ""+oslist.get(+position).get("model_nam"), Toast.LENGTH_SHORT).show();
						
						SharedPreferences share= getSharedPreferences("keymodel", MODE_WORLD_READABLE);
						SharedPreferences.Editor ed=share.edit();
	                    ed.putString("srtmodel", value1); 
	                    ed.commit();
	                    
	                    opendialog(oslist.get(+position).get("com_name"),
	                    		oslist.get(+position).get("model_nam"),
	                    		oslist.get(+position).get("reg_no"));
	                      				
					}
				});
					  
				}
				  
			 }
		  catch(JSONException e)
		  {
			  e.printStackTrace();
			  }
		  }
	 
	 
	 public class Add_vehicle extends AsyncTask<Void, Void, Void>
	 {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try 
			{
				String serv = getResources().getString(R.string.serverid);
				DefaultHttpClient hc=new DefaultHttpClient();
				ResponseHandler<String> res=new BasicResponseHandler();
				HttpPost postMethod=new HttpPost(serv+"veh_listadd.php");
				List<NameValuePair>nameValuePairs=new ArrayList<NameValuePair>(1);
				nameValuePairs.add(new BasicNameValuePair("user_id",shared_uid));
				nameValuePairs.add(new BasicNameValuePair("com_name", scmp));
				nameValuePairs.add(new BasicNameValuePair("model_nam", smod));
				nameValuePairs.add(new BasicNameValuePair("year", syear));
				nameValuePairs.add(new BasicNameValuePair("reg_no", sreg));
				
				postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			 respo= hc.execute(postMethod,res);
			 
			 System.out.println(response);
				
			}
			catch(Exception e)
			{
				System.out.println("Error"+e);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(respo.contains("success"))
			{
				Toast.makeText(getApplicationContext(), "Vehicle list updated", Toast.LENGTH_SHORT).show();

				Intent i=new Intent(getApplicationContext(),Garage.class);
				startActivity(i);
			}
			else {
				Toast.makeText(getApplicationContext(), ""+respo, Toast.LENGTH_SHORT).show();
				
			}
			
		}
		 
	 }
	 
	 public void opendialog(String sc, String mn, String rn)
	 {
		 
		 scn=sc; smn=mn; srg=rn;
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);	   
		    	 alertDialogBuilder.setTitle("Please choose an action!");
			      alertDialogBuilder.setMessage("Vehicle Model : "+mn+"\n Company : "+sc+"\n Vehicle No : "+rn);
		    alertDialogBuilder.setPositiveButton("Proceed Request", new DialogInterface.OnClickListener() {
		         @Override
		         public void onClick(DialogInterface arg0, int arg1) {
		             
		        	 Intent i =new Intent(getApplicationContext(),Nearby_list.class);
	                    startActivity(i);
		            
		         }
		      });
		      
		    alertDialogBuilder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
		         @Override
		         public void onClick(DialogInterface dialog, int which) {
		        	
		        //	 Toast.makeText(getApplicationContext(),"Back",Toast.LENGTH_SHORT).show();
		        
		        	 //finish();
		         }
		      });
		      		     
		      AlertDialog alertDialog = alertDialogBuilder.create();
		      alertDialog.show();
	 }
	 
	 
}
