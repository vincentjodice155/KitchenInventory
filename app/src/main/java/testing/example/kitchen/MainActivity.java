package testing.example.kitchen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import static testing.example.kitchen.R.layout.record;

public class MainActivity extends AppCompatActivity {



    //Intializing objects for search bar feature
    ListView listV;
    ArrayAdapter adapter;
    ArrayList<String> searchList;
    SearchView searchV;

    //initiliaze objects for database data fetching
    RecyclerView recyclerView;
    ChocolateAdapter cadapter;
    public static List<Chocolate> chocolateList;
    PriorityQueue<Chocolate> priorityChocolateList;

    //create button to change to the add chocolates to database screen
    Button changeToAddButton;
    Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creating Search feature
        listV = (ListView)findViewById(R.id.list);
        searchV = (SearchView)findViewById(R.id.search);


        //creating instance of button to change to the Add Chocolate Screen
        changeToAddButton = (Button) findViewById(R.id.changeToAddButton);
        changeToAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddScreen();
            }
        });



        //set up Recycler view with some settings
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);


        loadChocolates();

        refreshButton = (Button) findViewById(R.id.refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadChocolates();
            }
        });

    }
    //Method that creates an intent that changes to Add Chocolate List Screen
    public void openAddScreen(){

        //stops and pops up w/ a display box asking for a pin
        //won't go to add screen unless you enter proper pin

        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_box,null);

        final EditText txt_inputText = (EditText)mView.findViewById(R.id.txt_input);

        Button btn_cancel = (Button)mView.findViewById(R.id.btn_cancel_pin);
        Button btn_okay = (Button)mView.findViewById(R.id.btn_enter_pin);

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String stringPin = txt_inputText.getText().toString();
                int pinPass = Integer.parseInt(stringPin);
                if(pinPass == 6789) {
                    Toast.makeText(MainActivity.this, "This is the correct pin", Toast.LENGTH_SHORT).show();
                    startIntent();
                }
                else{
                    Toast.makeText(MainActivity.this, "This is not the correct pin", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void startIntent(){
        Intent intent = new Intent(this, AddChocolateList.class);
        startActivity(intent);
    }


    //Method that loads name of chocolates into into the search field
    // also loads Chocolate object into output table
    private void loadChocolates() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.CHOCOLATE_URL, response -> {
            try {
                //initialized Json array and grab elements in JSon form from Database
                JSONArray chocolates = new JSONArray(response);

                searchList = new ArrayList<String>();
                //initialized list to hold Chocolate Object that will be taken from database
                chocolateList = new ArrayList<>();
                priorityChocolateList = new PriorityQueue<>(25,new MyComparator());

                for (int i = 0; i < chocolates.length(); i++) {
                    JSONObject chocolateObject = chocolates.getJSONObject(i);

                    String name = chocolateObject.getString("name").toLowerCase();
                    int alert = chocolateObject.getInt("alert");
                    int quantity = chocolateObject.getInt("quantity");
                    int pop = chocolateObject.getInt("popular");
                    int priority = Chocolate.getPriority(alert, quantity,pop);
                    String popular = isPop(pop);

                    //adds names of chocolates to search list
                    searchList.add(name);

                    //creates the instance of Chocolate object and adds to a list
                    Chocolate chocolate = new Chocolate(name, alert, quantity, popular,priority);
                    chocolateList.add(chocolate);
                    priorityChocolateList.add(chocolate);
                }

                //sets up adapter to output table of chocolate items
                cadapter = new ChocolateAdapter(MainActivity.this, priorityChocolateList);
                recyclerView.setAdapter(cadapter);

                //sets up adapter to set up search feature
                adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, searchList);
                listV.setAdapter(adapter);
                listV.setOnItemClickListener((parent, view, position, id) -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    ProgressDialog progressDialog = new ProgressDialog(view.getContext());

                    CharSequence[] dialogItem = {"Edit Chocolate Quantity", "Delete Chocolate"};

                    String chocolateName = searchList.get(position);

                    builder.setTitle(chocolateName);
                    builder.setItems(dialogItem, (dialog,i) -> {

                        switch (i) {
                            case 0:
                                startActivity(new Intent(getApplicationContext(),EditQuantity.class).putExtra("position",position));
                                break;

                            case 1:

                                deleteChocolate(chocolateName);
                                break;
                        }
                    });
                    builder.create().show();

                });

                //Operations for search field to filter for only searched results
                searchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        adapter.getFilter().filter(newText);
                        return true;
                    }
                });
            } catch (JSONException e) {

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }


    //converts popular into a value of Yes for 1 and No for 0

    public String isPop(int value){
        String response = "";
        if(value == 1) {
            response = "Yes";
        }
        else{
            response = "No";
        }
        return response;
    }
    private void deleteChocolate(final String name){
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setMessage("Please Select on of the two options");
        dialog.setTitle("Deleting " + name);
        dialog.setPositiveButton("Delete Anyways",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest request = new StringRequest(Request.Method.POST, Constants.DELETE_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equalsIgnoreCase("Data Deleted")) {
                                            Toast.makeText(MainActivity.this, "Data Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Data not Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();

                                params.put("name", name);

                                return params;
                            }
                        };

                        requestingQueue(request);
                        }
                    });


                dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Chocolate Item was Not Deleted", Toast.LENGTH_LONG).show();
                }
             });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

    }

    private void requestingQueue(Request r){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(r);
    }
}