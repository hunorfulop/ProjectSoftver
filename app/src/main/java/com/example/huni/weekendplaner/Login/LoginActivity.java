package com.example.huni.weekendplaner.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class  LoginActivity extends AppCompatActivity {

    EditText editTextPhonenumber, editTextCode;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Setting up the database connection
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");

        //Declaring fields form the view
        editTextPhonenumber = findViewById(R.id.phoneNumber_editText);
        editTextCode = findViewById(R.id.code_editText);

        findViewById(R.id.Main_creen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        //Login button OnClickListener
        findViewById(R.id.login_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                }
        });

        //Get code OnClikListener
        findViewById(R.id.login_Activity_GetCode_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = new User(editTextPhonenumber.getText().toString());
                        //In the if statement we verify if the user has aleready registerd
                        if (dataSnapshot.child(Objects.requireNonNull(ref.child(user.getPhonenumber()).getKey())).exists()){
                            //If he is in the database we send a verification code
                            Toast.makeText(getApplicationContext(),"You exist",Toast.LENGTH_LONG).show();
                            sendVerificationCode();
                        }
                        else{
                            //If he isn`t in the database we notify him about that
                            Toast.makeText(getApplicationContext(),"Please register first",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        //Login button OnClickListener
        findViewById(R.id.login_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextCode.length() == 0) {
                    Toast.makeText(getApplicationContext(),"Code is empty",Toast.LENGTH_LONG).show();
                }else {
                    verifySignInCode();
                }
            }
        });

    }

    //This function verifys the sign in code
    private void verifySignInCode(){
        String code = editTextCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //If sign in code is correct we save the user with the help of shared preferences a the logged in user
                            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("username",editTextPhonenumber.getText().toString());
                            editor.apply();
                            System.out.println("Ez egy Tag"+ editTextPhonenumber.getText().toString());
                            //We start the main screen
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
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

    //This function sends the verification code
    private void sendVerificationCode() {

        String phone = editTextPhonenumber.getText().toString();

        if(phone.isEmpty()){
            editTextPhonenumber.setError("Phone number is required");
            editTextPhonenumber.requestFocus();
            return;
        }

        if(phone.length() < 10){
            editTextPhonenumber.setError("Enter a valid phonenumber");
            editTextPhonenumber.requestFocus();
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


