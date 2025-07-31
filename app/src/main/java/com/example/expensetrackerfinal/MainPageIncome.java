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

public class MainPageIncome extends AppCompatActivity {

    CardView EnterIncome, ViewIncome;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page_income);

        EnterIncome = findViewById(R.id.EnterIncome);
        ViewIncome = findViewById(R.id.ViewIncome);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar); //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back arrow

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Navigate to insert income
        EnterIncome.setOnClickListener(v -> {
            Intent ienter = new Intent(MainPageIncome.this, IncomeInsert.class);
            startActivity(ienter);
        });

        // Navigate to display income
        ViewIncome.setOnClickListener(v -> {
            Intent i2 = new Intent(MainPageIncome.this, IncomeDisplay.class);
            startActivity(i2);
        });

        // Back arrow click goes to MainPage
        toolbar.setNavigationOnClickListener(v -> {
            Intent i = new Intent(MainPageIncome.this, MainPage.class);
            startActivity(i);
        });
    }
}
