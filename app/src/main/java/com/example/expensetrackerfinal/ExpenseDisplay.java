package com.example.expensetrackerfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseDisplay extends AppCompatActivity {

    Spinner spinner;
    RecyclerView rv2;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_display);

        // Initialize UI elements
        spinner = findViewById(R.id.ExpenseDuration);
        toolbar=findViewById(R.id.toolbar);
        rv2=findViewById(R.id.Recycle);
        rv2.setLayoutManager(new LinearLayoutManager(this));//layout to be used in recycle view
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
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //will store the selected option
                String selected=parent.getItemAtPosition(position).toString();
                Toast.makeText(ExpenseDisplay.this,"Display for this"+selected,Toast.LENGTH_SHORT).show();
                //db code goes here

                //retrieving the userID (we can also do that by calling the function and retrieving the userID using sql, but shared preference is comparatively faster and more suitable for this project)
                SharedPreferences pref= getSharedPreferences("user_data",MODE_PRIVATE);
                int userID=pref.getInt("userID",-1);
                if(userID==-1){
                    Toast.makeText(ExpenseDisplay.this,"User ID not found",Toast.LENGTH_LONG).show();
                }
                db_helper dbh=new db_helper(ExpenseDisplay.this);
                List<expense_db> expenseDisplay=dbh.displayExpense(selected,userID);
                if(expenseDisplay.isEmpty()){
                    Toast.makeText(ExpenseDisplay.this,"No expense entries found", Toast.LENGTH_SHORT).show();
                }
                //Calling the class where the list data is binded into an adapter
                ExpenseAdapterForRecycleView ie=new ExpenseAdapterForRecycleView(expenseDisplay);
                rv2.setAdapter(ie);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Back arrow click goes to MainPageExpense
        toolbar.setNavigationOnClickListener(v -> {
            Intent i9 = new Intent(ExpenseDisplay.this, MainPageExpense.class);
            startActivity(i9);
        });
    }
}
