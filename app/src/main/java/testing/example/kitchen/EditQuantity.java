package testing.example.kitchen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class EditQuantity extends AppCompatActivity {


    private EditText eQuantity,eName;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quantity);

        Button updateButton,addButton,subtractButton,backButton;

        eName = findViewById(R.id.nameOfQuantity);
        eQuantity = findViewById(R.id.quantity_ET);

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");


        eName.setText(MainActivity.chocolateList.get(position).getName());
        eQuantity.setText(String.valueOf(MainActivity.chocolateList.get(position).getQuantity()));

        updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(v -> update());

        backButton = findViewById(R.id.goBackButton);
        backButton.setOnClickListener(v -> openHomeScreen());

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> addQuantity());

        subtractButton = findViewById(R.id.subButton);
        subtractButton.setOnClickListener(v -> subtractQuantity());

    }
    public void openHomeScreen(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void addQuantity(){
        String name = eName.getText().toString();
        Toast.makeText(EditQuantity.this,("Adding 1 more "  + name ),Toast.LENGTH_SHORT).show();
        String quantity = eQuantity.getText().toString();
        int quant = Integer.parseInt(quantity);
        quant++;
        String newQuantity = String.valueOf(quant);
        eQuantity.setText(newQuantity);
    }

    public void subtractQuantity(){
        String name = eName.getText().toString();
        Toast.makeText(EditQuantity.this,("subtracting 1 more "  + name ),Toast.LENGTH_SHORT).show();
        String quantity = eQuantity.getText().toString();
        int quant = Integer.parseInt(quantity);
        System.out.println(quant);
        quant--;
        String newQuantity = String.valueOf(quant);
        eQuantity.setText(newQuantity);
    }


    public void update(){
        String name = eName.getText().toString();
        String alert = String.valueOf(MainActivity.chocolateList.get(position).getAlert());
        String quantity = eQuantity.getText().toString();
        String popular = MainActivity.chocolateList.get(position).getPopular();


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating.....");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, Constants.UPDATE_URL, response -> {
            progressDialog.dismiss();
            Toast.makeText(EditQuantity.this,response, Toast.LENGTH_SHORT).show();
        }, error -> Toast.makeText(EditQuantity.this,error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Nullable
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

        RequestQueue requestQueue = Volley.newRequestQueue(EditQuantity.this);
        requestQueue.add(request);

    }
}