package com.example.expensetrackerfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register extends AppCompatActivity {

    EditText etmail, etpass, etconfirmpass,etname;
    Button sub;
    TextView tologinpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etmail = findViewById(R.id.etmail);
        etpass = findViewById(R.id.etpass);
        etname = findViewById(R.id.etname);
        etconfirmpass = findViewById(R.id.etconfirmpass);
        sub = findViewById(R.id.sub);
        tologinpage=findViewById(R.id.tvtoLoginPage);


        //pressing button will perform validation and then save it to the db
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the function will return true then further execution will take place
                if (!validateRegister()) {
                    return;
                };

                //db code goes here
                //extracting the data from the user
                user_db ud;
                try {
                    ud=new user_db(-1,etname.getText().toString().trim(),etmail.getText().toString(),etpass.getText().toString().trim());
                } catch (Exception e) {
                    Toast.makeText(Register.this,"Error creating User",Toast.LENGTH_SHORT).show();
                    ud=new user_db(-1,"error","error","error");
                }
                //cresting a class of the db helper and passing the context of this current file in the parameter
                db_helper db = new db_helper(Register.this);
                boolean b = db.addUser(ud);
                if(!b){
                    Toast.makeText(Register.this,"Cannot insert the data",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Register.this,"Register Successful",Toast.LENGTH_SHORT).show();
                }

                //After saving the data in db, it will redirect to the login page
                Intent i1=new Intent(Register.this,MainActivity.class);
                startActivity(i1);

            }
        });

        //making the textview clickable
        tologinpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Register.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //this function will return a boolean value, if the function will return false, then
    //error will be thrown and the further execution will not work
    private boolean validateRegister() {
        String name=etname.getText().toString().trim();
        String mail = etmail.getText().toString().trim();
        String pass = etpass.getText().toString().trim();
        String cpass = etconfirmpass.getText().toString().trim();

        if (name.isEmpty()) {
            etmail.setError("Please enter an email");
            etmail.requestFocus();
            return false;
        }

        if (mail.isEmpty()) {
            etmail.setError("Please enter an email");
            etmail.requestFocus();
            return false;
        }

        if (pass.isEmpty()) {
            etpass.setError("Please enter password");
            etpass.requestFocus();
            return false;
        }

        if (cpass.isEmpty()) {
            etconfirmpass.setError("Please re-enter your password");
            etconfirmpass.requestFocus();
            return false;
        }

        if (pass.length() < 6) {
            etpass.setError("Password should be greater than 6 characters");
            etpass.requestFocus();
            return false;
        }

        if (!pass.equals(cpass)) {
            etpass.setError("Passwords do not match");
            etconfirmpass.setError("Passwords do not match");
            return false;
        }
        return true;
    }
}
