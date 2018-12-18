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
    //Firebase variables
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseAuth mAuth;

    EditText phonenumber,lastname,firstname,code;
    Button register,getCode;
    String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initializating the view elements
        phonenumber = (EditText) findViewById(R.id.register_phonenumber);
        lastname = (EditText) findViewById(R.id.register_Lastname);
        firstname = (EditText) findViewById(R.id.register_FirstName);
        code = (EditText) findViewById(R.id.register_code);
        getCode = (Button) findViewById(R.id.button_getcode);
        register = (Button) findViewById(R.id.registerActivity_registerButton);

        //Setting up Firebase connection
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");

        //GetCode OncliCkListener
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
            }
        });

        //Register button OnClickListner
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

    //In this function we add new users to the database
    private void writeNewUsers(){
        //We create a new user
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Dummy");
        User user = new User(phonenumber.getText().toString(),lastname.getText().toString(),firstname.getText().toString(),"sadas",arrayList,false);
        //We inset the new user into the datbase
        ref.child(user.getPhonenumber()).setValue(user);
        System.out.println("TAGfasz "+ ref);

        //We notify the user that the registration was succssefull
        Toast.makeText(RegisterActivity.this,"Registered succssefuly",Toast.LENGTH_LONG).show();
    }

    //Here we send the werification code
    private void verifySignInCode(){
        String codes = code.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, codes);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    //If the verification code is valid we complete the registration
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            writeNewUsers();
                            //We start the login acctivity
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

    //Here we send the verification code
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