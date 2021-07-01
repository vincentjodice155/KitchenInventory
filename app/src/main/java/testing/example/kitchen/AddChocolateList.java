package testing.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddChocolateList extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText editTextName, editTextAlert, editTextQuantity,editTextPopular;

    private Button buttonDatabaseAdd;
    private Button backButton;
    private ProgressDialog progressDialog;

    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chocolate_list);

        editTextName = (EditText) findViewById(R.id.name_input);
        editTextAlert = (EditText) findViewById(R.id.alert_input);
        editTextQuantity = (EditText) findViewById(R.id.quantity_input);
        editTextPopular = (EditText) findViewById(R.id.popular_input);

        //creating Spinner Stuff
        spinner = findViewById(R.id.spinner);

        ArrayList<String> chocolateTypes = new ArrayList<>();
        chocolateTypes.add("Milk Chocolate");
        chocolateTypes.add("Dark Chocolate");
        chocolateTypes.add("White Chocolate");

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,chocolateTypes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        buttonDatabaseAdd = (Button) findViewById(R.id.buttonAdd);

        progressDialog = new ProgressDialog(this);

        buttonDatabaseAdd.setOnClickListener(this);

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomeScreen();
            }
        });

    }
    public void openHomeScreen(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void registerUser(){

        final String name = editTextName.getText().toString().trim();
        final String alert = editTextAlert.getText().toString().trim();
        final String quantity = editTextQuantity.getText().toString().trim();
        final String popular = editTextPopular.getText().toString().trim();

        progressDialog.setMessage("Registering user... ");
        progressDialog.show();

        //creates the request to be sent to the database

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER, new Response.Listener<String>() {
            @Override
            //Lets you know whether it was successful or not with a message
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name",name);
                params.put("alert",alert);
                params.put("quantity",quantity);
                params.put("popular",popular);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    //Method for Clicking button allowing entry of name into database
    @Override
    public void onClick(View v) {
        //when you click the button it activated the registerUser method
        if(v == buttonDatabaseAdd)
            registerUser();
    }

    //Methods for Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String chocolateType = parent.getItemAtPosition(position).toString();
        String newChocolateTyoe = chocolateType + " " + editTextName.getText().toString();
        Toast.makeText(getApplicationContext(),chocolateType,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}