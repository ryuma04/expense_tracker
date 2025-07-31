package com.example.expensetrackerfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//next task
//1. Sort the genearate report feature
public class MainActivity extends AppCompatActivity {

    EditText etName, etPassword;
    Button btnSubmit;
    TextView toregisterpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        btnSubmit = findViewById(R.id.sub);
        toregisterpage=findViewById(R.id.tvtoregisterpage);

        //pressing button will perform validation and then save it to the db
        btnSubmit.setOnClickListener(v -> {
            if(!validateLogin()){
                return;
            };

            //db code goes here
            db_helper dbh=new db_helper(MainActivity.this);
            boolean b = dbh.checkUser(etName.getText().toString(), etPassword.getText().toString());
            if(!b){
                Toast.makeText(this,"Wrong username or password",Toast.LENGTH_LONG).show();
                return;
            }
            else{
                Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show();

                //-------------------Retrieving user id and storing it using shared preference---------------------
                //retrieving the userid so that it will be use to store and retrieve the data of a particular user only
                int userID=dbh.retriveUserID(etName.getText().toString(), etPassword.getText().toString());

                //Using shared preference, it is used to create a file which will consist of the data that can be used across the entire app
                SharedPreferences sharedPreference= getSharedPreferences("user_data", MODE_PRIVATE);//here file name is user_data and mode is private ie only this app can use it

                //making the editor obj so that it will allow for edit in the shared preference, in this case it will be used to store userID
                SharedPreferences.Editor editor=sharedPreference.edit();

                //Storing user id in the shared preference
                editor.putInt("userID",userID);

                //Saving it
                editor.commit();
                //--------------------------------userID saved-----------------------------------------------------------

                Intent intent=new Intent(MainActivity.this,MainPage.class);
                startActivity(intent);
            }
        });

        //making the link clickable
        toregisterpage.setOnClickListener(v -> {
            Intent i=new Intent(MainActivity.this, Register.class);
            startActivity(i);
        });

    }

    private boolean validateLogin() {
        String email = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etName.setError("Enter the email");
            etName.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("Enter the email");
            etPassword.requestFocus();
            return false;
        }
        if (password.length()<6) {
            etPassword.setError("Password should be greater than 6 characters");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }
}