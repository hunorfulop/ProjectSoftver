package com.example.huni.weekendplaner;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class  LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_Button = findViewById(R.id.login_login_button);
        Button register_Button = findViewById(R.id.login_register_button);
        TextInputLayout phoneNumber_textinputLayout = findViewById(R.id.textInputLayout);
        TextInputLayout code_textinputLayout = findViewById(R.id.textInputLayout2);
        final EditText phoneNumber = findViewById(R.id.phoneNumber_editText);
        final EditText code = findViewById(R.id.code_editText);

        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if((phoneNumber.getText().toString().equals("1234") && (code.getText().toString().equals("1234")))){
                   startActivity(new Intent(getApplicationContext(),MainActivity.class));
                   finish();
               }
               else{
                   Toast.makeText(getApplicationContext(),"Invalid phonenumber or code",Toast.LENGTH_LONG).show();
               }
            }
        });

        register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

    }
}
