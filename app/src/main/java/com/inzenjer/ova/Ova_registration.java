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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Ova_registration extends Activity{
	
	Button bt_oreg;
	EditText et_nm,et_ph,et_ad,et_em,et_ovps,et_ovcps;
	String s_nm,s_ph,s_ad,s_em,s_ps,s_cps,s_utyp,jresp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ova_registration);
		
		et_nm=(EditText)findViewById(R.id.et_name);
		et_ph=(EditText)findViewById(R.id.et_contact);
		et_ad=(EditText)findViewById(R.id.et_address);
		et_em=(EditText)findViewById(R.id.et_email);
		et_ovps=(EditText)findViewById(R.id.et_password);
		et_ovcps=(EditText)findViewById(R.id.et_confpassword);
		
		bt_oreg=(Button)findViewById(R.id.bt_regbut);
		bt_oreg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				s_nm=et_nm.getText().toString();
				s_ph=et_ph.getText().toString();
				s_ad=et_ad.getText().toString();
				s_em=et_em.getText().toString();
				s_ps=et_ovps.getText().toString();
				s_cps=et_ovcps.getText().toString();
				s_utyp="UR";
				
				
				if(!s_ps.contentEquals(s_cps))
				{
					Toast.makeText(getApplicationContext(), "Password Mismatch", Toast.LENGTH_LONG).show();
					return;
				}
				else
				{
					Usr_reg urg=new Usr_reg();
					urg.execute();
				}
			
			}
		});
		
	}
	public class Usr_reg extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String server = getResources().getString(R.string.serverid);
				DefaultHttpClient c= new DefaultHttpClient();
				ResponseHandler<String> resp= new BasicResponseHandler();
				// link to the database phpfile
				HttpPost pmethod = new HttpPost(server+"ova_register.php");
				List<NameValuePair> npairs= new ArrayList<NameValuePair>(5);
				
				//refer database table for correct name of column
				npairs.add(new BasicNameValuePair("name", s_nm));
				npairs.add(new BasicNameValuePair("address", s_ad));
				npairs.add(new BasicNameValuePair("phone", s_ph));
				npairs.add(new BasicNameValuePair("logtype", s_utyp));
				npairs.add(new BasicNameValuePair("email", s_em));
				npairs.add(new BasicNameValuePair("password", s_ps));
				
				pmethod.setEntity(new UrlEncodedFormEntity(npairs));
				jresp=c.execute(pmethod,resp);
								
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error:"+e);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			
			if(jresp.contains("success"))
			{
				Toast.makeText(getApplicationContext(), "Registration completed. Please log in...", Toast.LENGTH_LONG).show();
				Intent i = new Intent(getApplicationContext(), Home_2.class);
				startActivity(i);
				
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Registration error try again", Toast.LENGTH_LONG).show();
			}
			
			super.onPostExecute(result);
		
			
		}
	}
		
}
	
	
	

