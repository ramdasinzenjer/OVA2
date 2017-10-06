package com.inzenjer.ova;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Home extends Activity {

	
	Button nlist,nmap,econt;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        nlist=(Button)findViewById(R.id.nearby);
        nmap=(Button)findViewById(R.id.locate);
        econt=(Button)findViewById(R.id.econtacts);
    
        nmap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i= new Intent(getApplicationContext(),Nearby_map.class);
				startActivity(i);
				
			}
		});
		nlist.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						Intent i= new Intent(getApplicationContext(),Nearby_list.class);
						startActivity(i);
						
					}
				});
		econt.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						Intent i= new Intent(getApplicationContext(),E_contact.class);
						startActivity(i);
						
					}
				});
        
        
    }
    
    
}
