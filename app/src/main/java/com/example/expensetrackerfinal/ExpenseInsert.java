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
import java.util.Locale;

public class ExpenseInsert extends AppCompatActivity {

    EditText detail, value;
    Spinner category;
    DatePicker date;
    Button expense;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_insert);

        detail = findViewById(R.id.expense_detail);
        value = findViewById(R.id.expense_value);
        category = findViewById(R.id.expense_category);
        date = findViewById(R.id.expense_date);
        expense = findViewById(R.id.expense);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back arrow

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //here setting the spinner
        String arr[]={"Select Category","Groceries","Real Estate","Gifts", "Travel","Children's expense", "Others"};
        ArrayAdapter aa=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,arr);
        category.setAdapter(aa);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = category.getSelectedItem().toString();
                //Toast.makeText(ExpenseInsert.this, "Selected: " + selectedCategory, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        expense.setOnClickListener(v -> {
            //here i havent created a seperate method for all the validation, as the values of the edittext and date are already stored in the variable which makes it easy to insert in the db
            //validating the inputs and then saving it to db
            String ex_detail = detail.getText().toString();
            int ex_value = Integer.parseInt(value.getText().toString());
            String ex_category=category.getSelectedItem().toString();
            //Setting the date to match the db requirement
            int day = date.getDayOfMonth();
            int month = date.getMonth();
            int year = date.getYear();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String ex_date = sdf.format(calendar.getTime());
            if (ex_detail.isEmpty()) {
                detail.setError("Fill this field");
                detail.requestFocus();
                return;
            }

            if (ex_value<0||ex_value==0) {
                value.setError("Enter a valid value");
                value.requestFocus();
                return;
            }

            //If nothing is selected in spinner
            if(ex_category.equals("Select Category")){
                Toast.makeText(this,"Please select a category",Toast.LENGTH_SHORT).show();
            }
            // Toast.makeText(this,"Expense noted",Toast.LENGTH_LONG).show();

            //db code goes here

            //retrieving the userID (we can also do that by calling the function and retrieving the userID using sql, but shared preference is comparatively faster and more suitable for this project)
            SharedPreferences pref= getSharedPreferences("user_data",MODE_PRIVATE);
            int userID=pref.getInt("userID",-1);
            if(userID==-1){
                Toast.makeText(ExpenseInsert.this,"User ID not found",Toast.LENGTH_LONG).show();
                return;
            }

            //inserting the expense into the expense db
            expense_db ex;
            try {
                ex=new expense_db(ex_detail,ex_value,ex_category,ex_date,userID);
            } catch (Exception e) {
                Toast.makeText(ExpenseInsert.this,"Error inserting expense in the database",Toast.LENGTH_SHORT).show();
                ex=new expense_db("error",-1,"error","error",userID);
            }
            db_helper dbh=new db_helper(ExpenseInsert.this);
            boolean b = dbh.addExpense(ex);
            if(!b){
                Toast.makeText(ExpenseInsert.this,"Cannot insert the data",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(ExpenseInsert.this,"Expense Inserted Successful",Toast.LENGTH_SHORT).show();
                detail.setText("");
                value.setText("");
                category.setSelection(0);
                Calendar Ecalendar = Calendar.getInstance(); // for clearing the date picker by setting it to the current date
                int Eyear = Ecalendar.get(Calendar.YEAR);
                int Emonth = Ecalendar.get(Calendar.MONTH);
                int Eday = Ecalendar.get(Calendar.DAY_OF_MONTH);
                date.updateDate(Eyear, Emonth, Eday);
            }
        });

        // Back arrow click goes to MainPageExpense
        toolbar.setNavigationOnClickListener(v -> {
            Intent i11 = new Intent(ExpenseInsert.this, MainPageExpense.class);
            startActivity(i11);
        });
    }
}
