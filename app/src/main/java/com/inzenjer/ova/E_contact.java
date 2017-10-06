package com.inzenjer.ova;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class E_contact extends Activity{
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e_contact);
	
	}
	
	
	public void tps(View view)
	{
		ecall("111");
	}
	public void ambs(View view)
	{
		ecall("123");
	}
	public void ffs(View view)
	{
		ecall("1234");
	}
	

	public void ecall(String num)
	{
		//Intent in=new Intent(Intent.ACTION_CALL,Uri.parse(num));
		Intent phoneIntent = new Intent(Intent.ACTION_CALL); 
		phoneIntent.setData(Uri.parse("tel:"+num));
	      try{
	         startActivity(phoneIntent);
	      }
	      
	      catch (android.content.ActivityNotFoundException ex){
	         Toast.makeText(getApplicationContext(),"Error in call facility",Toast.LENGTH_SHORT).show();
	      }
	}

}
