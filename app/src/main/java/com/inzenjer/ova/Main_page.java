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
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main_page extends Activity{
	
	EditText et_un,et_ps;
	String sun,sps,respo; String s_uid;
	Button bt_lg,bt_rg,bt_gt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_page);
		
		et_un=(EditText)findViewById(R.id.et_username);
		et_ps=(EditText)findViewById(R.id.et_password);
		bt_lg=(Button)findViewById(R.id.bt_login);
		bt_rg=(Button)findViewById(R.id.bt_register);
		bt_gt=(Button)findViewById(R.id.bt_gst);
		
		/*boolean mobileDataEnabled = false; // Assume disabled
	    ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    try {
	        Class cmClass = Class.forName(cm.getClass().getName());
	        Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
	        method.setAccessible(true); // Make the method callable
	        // get the setting for "mobile data"
	        mobileDataEnabled = (Boolean)method.invoke(cm);
	    } catch (Exception e) {
	       Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
	    }*/
	  
		
		bt_lg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sun=et_un.getText().toString();
				sps=et_ps.getText().toString();
				
				ova_login ol=new ova_login();
				ol.execute();
				
			}
		});
		bt_rg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),Ova_registration.class);
				startActivity(i);
				
			}
		});
		bt_gt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),Guest_home.class);
				startActivity(i);
				
			}
		});
		
	}
	public class ova_login extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String server = getResources().getString(R.string.serverid);
				DefaultHttpClient c= new DefaultHttpClient();
				ResponseHandler<String> resp= new BasicResponseHandler();
				// link to the database phpfile
				HttpPost pmethod = new HttpPost(server+"ova_login.php");
				List<NameValuePair> npairs= new ArrayList<NameValuePair>(1);
				npairs.add(new BasicNameValuePair("user_name", sun));
				npairs.add(new BasicNameValuePair("password", sps));
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
				Intent i=new Intent(getApplicationContext(),Home_2.class);
				startActivity(i);
				
				
				
				parsingmethod();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Incorrect email or password ", Toast.LENGTH_LONG).show();
				
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
				s_uid=data1.getString("user_id");
				/*pas=data1.getString("Password");
				ema=data1.getString("Email");
				pho=data1.getString("Phonenumber");*/
				Toast.makeText(getApplicationContext(), s_uid, Toast.LENGTH_SHORT).show();	
				SharedPreferences share=getSharedPreferences("keyuid", MODE_WORLD_READABLE);
				SharedPreferences.Editor ed=share.edit();
				ed.putString("srtuid",s_uid);
				ed.commit();
				/*System.out.println(nam+ema+pas+pho);
				na.setText(nam);
				ps.setText(pas);
				em.setText(ema);
				ph.setText(pho);*/
		
			}
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}

}
