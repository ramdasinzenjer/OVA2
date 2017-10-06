package com.inzenjer.ova;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Testclass extends Activity implements OnItemSelectedListener {
 
    private Button btnAddNewCategory;
    private TextView txtCategory;
    private Spinner spinnerFood;
    // array list for spinner adapter
    private ArrayList<Category> categoriesList;
    ProgressDialog pDialog;
 
    // API urls
    private String URL_CATEGORIES = "http://192.168.1.10/Ova/vlistT.php";
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testlay);
 
       // btnAddNewCategory = (Button) findViewById(R.id.btnAddNewCategory);
        spinnerFood = (Spinner) findViewById(R.id.spinner_vlist);
        //txtCategory = (TextView) findViewById(R.id.txtCategory);
         
        categoriesList = new ArrayList<Category>();
 
        // spinner item select listener
        spinnerFood.setOnItemSelectedListener(this);
 
        // Add new category click event
       /* btnAddNewCategory.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                if (txtCategory.getText().toString().trim().length() > 0) {
                     
                    // new category name
                    String newCategory = txtCategory.getText().toString();
 
                    // Call Async task to create new category
                    new AddNewCategory().execute(newCategory);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter category name", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });*/
 
      GetCategories gc= new GetCategories();
        gc.execute();
 
    }
 
    /**
     * Adding spinner data
     * */
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
        spinnerFood.setAdapter(spinnerAdapter);
    }
 
    /**
     * Async task to get all food categories
     * */
    private class GetCategories extends AsyncTask<Void, Void, Void> {
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Testclass.this);
            pDialog.setMessage("Vehicle categories..");
            pDialog.setCancelable(false);
            pDialog.show();
 
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL_CATEGORIES, ServiceHandler.GET);
 
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

 
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {
        Toast.makeText(
                getApplicationContext(),
                        parent.getItemAtPosition(position).toString() + " Selected" ,
                Toast.LENGTH_LONG).show();
    }
 
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {      
    }
}