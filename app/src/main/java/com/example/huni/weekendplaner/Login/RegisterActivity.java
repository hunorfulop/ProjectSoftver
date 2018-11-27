package com.example.huni.weekendplaner.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.huni.weekendplaner.Main.MainActivity;
import com.example.huni.weekendplaner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    FirebaseDatabase database;
    EditText phonenumber,lastname,firstname,code;
    Button register,getCode;
    DatabaseReference ref;
    FirebaseAuth mAuth;
    String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        phonenumber = (EditText) findViewById(R.id.register_phonenumber);
        lastname = (EditText) findViewById(R.id.register_Lastname);
        firstname = (EditText) findViewById(R.id.register_FirstName);
        code = (EditText) findViewById(R.id.register_code);
        getCode = (Button) findViewById(R.id.button_getcode);
        register = (Button) findViewById(R.id.registerActivity_registerButton);


        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");

        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(code.length() == 0) {
                    Toast.makeText(getApplicationContext(),"Code is empty",Toast.LENGTH_LONG).show();
                }else {
                    verifySignInCode();
                }
            }
        });

    }

    private void writeNewUsers(){
        User user = new User(phonenumber.getText().toString(),lastname.getText().toString(),firstname.getText().toString(),"sadas",new ArrayList<String>(),false);

        ref.child(user.getPhonenumber()).setValue(user);

        Toast.makeText(RegisterActivity.this,"Registered succssefuly",Toast.LENGTH_LONG).show();
    }


    private void verifySignInCode(){
        String codes = code.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, codes);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            writeNewUsers();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(),"Invalid Code",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationCode() {

        String phone = phonenumber.getText().toString();

        if(phone.isEmpty()){
            phonenumber.setError("Phone number is required");
            phonenumber.requestFocus();
            return;
        }

        if(phone.length() < 10){
            phonenumber.setError("Enter a valid phonenumber");
            phonenumber.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            //super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }
    };


}


/*

public void register(View view){
        ///Toast.makeText(RegisterActivity.this,"Data Inserted111111",Toast.LENGTH_LONG).show();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getValues();
                Toast.makeText(RegisterActivity.this,"Data Inserted22222222222222222222",Toast.LENGTH_LONG).show();
                User user1 = new User();
                user1.setPhonenumber(phonenumber.getText().toString());
                user1.setNickname(nickname.getText().toString());
                user1.setAdmin(false);
                user1.setImage("asdas");
                user1.setIntrests(new ArrayList<String>());
                ref.child("User1").setValue(user1);
                Toast.makeText(RegisterActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


        Button buttton = findViewById(R.id.registerActivity_registerButton);

        buttton.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        });
        */