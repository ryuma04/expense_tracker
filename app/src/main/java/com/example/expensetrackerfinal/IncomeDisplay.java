package com.example.expensetrackerfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class IncomeDisplay extends AppCompatActivity {

    Spinner spinnerDuration;
    RecyclerView rv;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_income_display);

        // Initialize UI elements
        spinnerDuration = findViewById(R.id.IncomeDuration);
        rv = findViewById(R.id.recycleview);
        rv.setLayoutManager(new LinearLayoutManager(this));//layout to be used in recycle view
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back arrow


        // Adjust insets (for edge-to-edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.incomeDisplayLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String[] options = {"--Select--","Day", "Week", "Month"};

        //Array for storing item in the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                options
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuration.setAdapter(adapter);

        spinnerDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //will store the selected option
                String selected=parent.getItemAtPosition(position).toString();
                Toast.makeText(IncomeDisplay.this,"Display for this "+selected,Toast.LENGTH_SHORT).show();

                //db code goes here

                //retrieving the userID (we can also do that by calling the function and retrieving the userID using sql, but shared preference is comparatively faster and more suitable for this project)
                SharedPreferences pref= getSharedPreferences("user_data",MODE_PRIVATE);
                int userID=pref.getInt("userID",-1);
                if(userID==-1){
                    Toast.makeText(IncomeDisplay.this,"User ID not found",Toast.LENGTH_LONG).show();
                }
                db_helper dbh=new db_helper(IncomeDisplay.this);
                List <income_db> incomeDisplay=dbh.displayIncome(selected,userID);
                //for testing
                Log.d("IncomeDisplay", "Fetched rows: " + incomeDisplay.size());



                if(incomeDisplay.isEmpty()){
                    Toast.makeText(IncomeDisplay.this,"No income entries found", Toast.LENGTH_SHORT).show();
                }
                //Calling the class where the list data is binded into an adapter
                IncomeAdapterForRecycleView ia=new IncomeAdapterForRecycleView(incomeDisplay);
                rv.setAdapter(ia);
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Back arrow click goes to MainPageInsert
        toolbar.setNavigationOnClickListener(v -> {
            Intent i5 = new Intent(IncomeDisplay.this, MainPageIncome.class);
            startActivity(i5);
        });
    }
}
