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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Nearby_list extends Activity{

	// for getting spinnner
	Spinner sp_dist;
	ArrayAdapter<String> arr_adp_dist;
	String sdist_sing;  String sh,sid,sk;
	String[] sc={"Select District","Thiruvananthapuram","Kollam","Pathananmthitta","Alapuzha","Kottayam","Idukki"};
	
	//for getting listview
	ListView listview; TextView name1; 
	  int length;
	  String response,name2,value1,dept2;
	  ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_llistview);
		
		SharedPreferences share=getSharedPreferences("keyuid", MODE_WORLD_READABLE);
		sid= share.getString("srtuid","");
		Toast.makeText(getApplicationContext(), "hi "+sid, Toast.LENGTH_SHORT).show();
	
		sp_dist=(Spinner)findViewById(R.id.spinner_dist);
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
				//Toast.makeText(getApplicationContext(), ""+sdist_sing,Toast.LENGTH_SHORT).show();
				SharedPreferences share1=getSharedPreferences("keyplace", MODE_WORLD_READABLE);
				SharedPreferences.Editor ed1=share1.edit();
				ed1.putString("srtplace",sdist_sing);
				ed1.commit();
				Nearby_Serv ns=new Nearby_Serv();
				ns.execute();
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        listview=(ListView)findViewById(R.id.nearlist);
        name1=(TextView)findViewById(R.id.singletext);
		
	}
	
	public class Nearby_Serv extends  AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String server = getResources().getString(R.string.serverid);
				DefaultHttpClient c= new DefaultHttpClient();
				ResponseHandler<String> resp= new BasicResponseHandler();
				// link to the database phpfile
				HttpPost pmethod = new HttpPost(server+"sclistret.php");
				List<NameValuePair> npairs= new ArrayList<NameValuePair>(2);	
				//refer database table for correct name of column
				npairs.add(new BasicNameValuePair("dist", sdist_sing));
				pmethod.setEntity(new UrlEncodedFormEntity(npairs));
				sh=c.execute(pmethod,resp);
				
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
			if (sh.contains("success"))
			{
				parsingmethod();
			}
			else {
				Toast.makeText(getApplicationContext(), "No service center available..", Toast.LENGTH_SHORT).show();
				listview.setAdapter(null);
				oslist.clear();
			}		
		}	
	}
	 public void parsingmethod(){
		  try
		  {
			  listview.setAdapter(null);
				oslist.clear();
			  JSONObject job=new JSONObject(sh);
			  JSONObject job1=job.getJSONObject("Event");
			  JSONArray ja=job1.getJSONArray("Details");
			  length=ja.length();
			  for(int i=0;i<length;i++)
			  {
				  JSONObject c=ja.getJSONObject(i);
				  //storing json item in variable
				 // name2=c.getString(TAG_ID);
				  dept2=c.getString("user_name");				  
				  //adding value hashmap key to value
				  HashMap<String, String> map=new HashMap<String, String>();
				 // map.put(TAG_ID, name2);
				  map.put("user_name", dept2);				 
				  oslist.add(map);
				  listview=(ListView)findViewById(R.id.nearlist);
				  ListAdapter adapter=new SimpleAdapter(getApplicationContext(), oslist, 
						  R.layout.list_single,
						  new String[] {"user_name"}, new int[] {R.id.singletext});
				  listview.setAdapter(adapter);
				  listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						//TextView text=(TextView)view.findViewById(R.id.event1);
						//value1=text.getText().toString().trim();
				Toast.makeText(getApplicationContext(), ""+oslist.get(+position).get("user_name"), Toast.LENGTH_SHORT).show();
						
						SharedPreferences share= getSharedPreferences("keyuanmserv", MODE_WORLD_READABLE);
						SharedPreferences.Editor ed=share.edit();
	                    ed.putString("srtuanmserv", oslist.get(+position).get("user_name")); 
	                    ed.commit();
	                /*    if(sk.contains("guest"))
	                    {
	                    	Intent i =new Intent(getApplicationContext(),Service_center.class);
	                    startActivity(i);
	                    }
	                    else {
	                    */
	                    	Intent i =new Intent(getApplicationContext(),Req_myserv.class);
		                    startActivity(i);
						//}					
					}
				});				  
				}			  
			 }
		  catch(JSONException e)
		  {
			  e.printStackTrace();
		  }
	}
}
