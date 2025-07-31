package com.example.expensetrackerfinal;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainPageExpense extends AppCompatActivity {

    CardView EnterExpense, ViewExpense;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page_expense);

        EnterExpense = findViewById(R.id.EnterExpense);
        ViewExpense = findViewById(R.id.ViewExpense);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back arrow

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Navigate to insert income
        EnterExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageExpense.this, ExpenseInsert.class);
            startActivity(intent);
        });

        // Navigate to display income
        ViewExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageExpense.this, ExpenseDisplay.class);
            startActivity(intent);
        });

        // Back arrow click goes to MainPage
        toolbar.setNavigationOnClickListener(v -> {
            Intent i = new Intent(MainPageExpense.this, MainPage.class);
            startActivity(i);
        });
    }
}
