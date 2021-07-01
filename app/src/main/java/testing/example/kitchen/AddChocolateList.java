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


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddChocolateList extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    //creates the values that user will edit
    private EditText editTextName, editTextAlert, editTextQuantity,editTextPopular;


    private ProgressDialog progressDialog;
    private Button buttonDatabaseAdd;
    public static String spinnerValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chocolate_list);


        Button backButton;
        Spinner spinner;

        editTextName = findViewById(R.id.name_input);
        editTextAlert = findViewById(R.id.alert_input);
        editTextQuantity = findViewById(R.id.quantity_input);
        editTextPopular = findViewById(R.id.popular_input);

        //creating Spinner Stuff
        spinner = findViewById(R.id.spinner);

        ArrayList<String> chocolateTypes = new ArrayList<>();
        chocolateTypes.add("Mk");
        chocolateTypes.add("Dk");
        chocolateTypes.add("Wh");

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,chocolateTypes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);


        buttonDatabaseAdd = findViewById(R.id.buttonAdd);

        progressDialog = new ProgressDialog(this);

        buttonDatabaseAdd.setOnClickListener(this);

        backButton = findViewById(R.id.backButton);

        //back button goes from AddChocolateList -> MainActivity
        backButton.setOnClickListener(v -> openHomeScreen());

    }
    //method that goes from AddChocolateList -> MainActivity
    public void openHomeScreen(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    //method that adds Chocolates to the Database
    private void registerUser(){
        //name uses dropdown value as well as user entered value
        final String name = spinnerValue + " " + (editTextName.getText().toString().trim());
        final String alert = editTextAlert.getText().toString().trim();
        final String quantity = editTextQuantity.getText().toString().trim();
        final String popular = editTextPopular.getText().toString().trim();

        progressDialog.setMessage("Registering user... ");
        progressDialog.show();

        //creates the request to be sent to the database

        //Lets you know whether it was successful or not with a message
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER, response -> {
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(response);

                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
        }){
            //an override method that was needed to get it to work
            // it just works don't question it >:D
            @Override
            protected Map<String, String> getParams() {
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
        spinnerValue = parent.getItemAtPosition(position).toString();
        //Toast.makeText(getApplicationContext(),spinnerValue,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}