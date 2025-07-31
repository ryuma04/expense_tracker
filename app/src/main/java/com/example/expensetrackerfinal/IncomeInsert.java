package com.example.expensetrackerfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class IncomeInsert extends AppCompatActivity {

    EditText detail, value;
    Spinner category;
    DatePicker date;
    Button income;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_income_insert);

        detail = findViewById(R.id.etdetail);
        value = findViewById(R.id.etvalue);
        category = findViewById(R.id.category);
        date = findViewById(R.id.date);
        income = findViewById(R.id.income);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back arrow

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //here setting the spinner
        String arr[]={"Select Category","Real_Estate","Gift", "Salary", "Others"};
        ArrayAdapter aa=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,arr);
        category.setAdapter(aa);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                //Toast.makeText(IncomeInsert.this, "Selected: " + selectedCategory, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        income.setOnClickListener(v -> {

            //validating the inputs and then saving it to db
            String in_detail = detail.getText().toString();
            int in_value = Integer.parseInt(value.getText().toString());
            String in_category=category.getSelectedItem().toString();
            //Setting the date to match the db requirement
            int day = date.getDayOfMonth();
            int month = date.getMonth();
            int year = date.getYear();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String in_date = sdf.format(calendar.getTime());

            if (in_detail.isEmpty()) {
                detail.setError("Fill this field");
                detail.requestFocus();
                return;
            }

            if (in_value<0||in_value==0) {
                value.setError("Enter a value");
                value.requestFocus();
                return;
            }

            //If nothing is selected in spinner
            if(in_category.equals("Select Category")){
                Toast.makeText(this,"Please select a category",Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(this,"Income Noted",Toast.LENGTH_LONG).show();

            //db code goes here

            //retrieving the userID (we can also do that by calling the function and retrieving the userID using sql, but shared preference is comparatively faster and more suitable for this project)
            SharedPreferences pref= getSharedPreferences("user_data",MODE_PRIVATE);
            int userID=pref.getInt("userID",-1);
            if(userID==-1){
                Toast.makeText(IncomeInsert.this,"User ID not found",Toast.LENGTH_LONG).show();
            }

            //inserting the expense into the expense db
            income_db ic;
            try {
                ic=new income_db(in_detail,in_value,in_category,in_date,userID);
            } catch (Exception e) {
                Toast.makeText(IncomeInsert.this,"Error inserting income in the database",Toast.LENGTH_SHORT).show();
                ic=new income_db("error",-1,"error","error",userID);
            }
            db_helper dbh=new db_helper(IncomeInsert.this);
            boolean b = dbh.addIncome(ic);
            if(!b){
                Toast.makeText(IncomeInsert.this,"Cannot insert the data",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(IncomeInsert.this,"Income Inserted Successful",Toast.LENGTH_SHORT).show();
                //clearing the values
                detail.setText("");
                value.setText("");
                category.setSelection(0);
                Calendar ClearCalendar = Calendar.getInstance(); // for clearing the date picker by setting it to the current date
                int Cyear = ClearCalendar.get(Calendar.YEAR);
                int Cmonth = ClearCalendar.get(Calendar.MONTH);
                int Cday = ClearCalendar.get(Calendar.DAY_OF_MONTH);
                date.updateDate(Cyear, Cmonth, Cday);
            }
        });

        // Back arrow click goes to MainPageInsert
        toolbar.setNavigationOnClickListener(v -> {
            Intent i7 = new Intent(IncomeInsert.this, MainPageIncome.class);
            startActivity(i7);
        });
    }
}
