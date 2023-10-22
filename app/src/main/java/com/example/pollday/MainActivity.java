package com.example.pollday;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pollday.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String[] item = {"04-Premnagar", "05-Bhatgaon", "06-Pratappur"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItem;
    EditText etName, etPhone, etAddress;
    Button btnInsert;
    ProgressDialog progressDialog;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItem = new ArrayAdapter<>(this, R.layout.list_item, item);
        autoCompleteTextView.setAdapter(adapterItem);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "Item: "+ item,Toast.LENGTH_SHORT).show();
            }
        });

        etName = findViewById(R.id.et_student_id);
        etAddress = findViewById(R.id.et_address_id);
        etPhone = findViewById(R.id.et_phone_no);
        btnInsert = findViewById(R.id.btn_insert_id);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudentData();
                progressDialog.show();
            }
        });
    }


    public void addStudentData() {
        String sName = etName.getText().toString();
        String sPhone = etPhone.getText().toString();
        String sAddress = etAddress.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbyD0T-c7efnb26hpsMvfO-EXrk1Og-OsuJN04SOZEh-vG492a1GhHVbr8KlnnkvDUAtmw/exec", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(getApplicationContext(), activity_success.class);
                startActivity(intent);
                progressDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "addStudent");
                params.put("vName", sName);
                params.put("vPhone", sPhone);
                params.put("vAddress", sAddress);
                return params;
            }
        };
            int socketTimeOut = 50000;
            RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(retryPolicy);

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
}