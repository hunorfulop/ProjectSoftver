package com.example.huni.weekendplaner.Details;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.huni.weekendplaner.Login.RegisterActivity;
import com.example.huni.weekendplaner.Login.User;
import com.example.huni.weekendplaner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Initializating the view elements
        TextView textView_nameOfEvent = findViewById(R.id.textView_nameOfEvent);
        TextView textView_start = findViewById(R.id.textView2_start);
        TextView textView_end = findViewById(R.id.textView_end);
        TextView textView_address = findViewById(R.id.textView3_address);
        TextView textView_desc = findViewById(R.id.textView4_desc);
        ImageView imageView_img = findViewById(R.id.imageView2);
        FloatingActionButton intrests_floatingActionButton = (FloatingActionButton) findViewById(R.id.intersts_floatingAction);

        //Getting the date form the MainActivity
        String NameofEvent = getIntent().getStringExtra("NameofEvent");
        String DescofEvent = getIntent().getStringExtra("DescofEvent");
        String StartofEvent = getIntent().getStringExtra("StartofEvent");
        String EndofEvent = getIntent().getStringExtra("EndofEvent");
        String AdresofEvent = getIntent().getStringExtra("AdresofEvent");
        String AuthorfEvent = getIntent().getStringExtra("AuthorfEvent");
        String ImageofEvent = getIntent().getStringExtra("ImageofEvent");
        final String id = getIntent().getStringExtra("IdofEvent");

        //Setting up the data in their view elements
        textView_nameOfEvent.setText(NameofEvent);
        textView_start.setText(StartofEvent);
        textView_end.setText(EndofEvent);
        textView_address.setText(AdresofEvent);
        textView_desc.setText(DescofEvent);

        //Setting up the database connection
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("User");

        //With SharedPreferences we get the current user
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        final String s = settings.getString("username","Dummy");

        //Using Glide to set an image to an ImageView with an URL form the internet
        Glide.with(this)
                .load(ImageofEvent)
                .into(imageView_img);

        intrests_floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = new User(s);
                        //In the if statement we verify if the user has aleready registerd
                        if (dataSnapshot.child(Objects.requireNonNull(ref.child(user.getPhonenumber()).getKey())).exists()){
                           User newuser = dataSnapshot.child(s).getValue(User.class);
                           user.setLastname(newuser.getLastname());
                           user.setFirstname(newuser.getFirstname());
                           user.setImage(newuser.getImage());
                           user.setPhonenumber(newuser.getPhonenumber());
                           user.setAdmin(newuser.getAdmin());
                           ArrayList<String> arrayList = new ArrayList<String>();
                           ArrayList<String> arrayList2 = newuser.getIntrests();
                           for(String arrayiterator : arrayList2){
                               arrayList.add(arrayiterator);
                           }
                           if(!arrayList.contains(id)){
                               arrayList.add(id);
                           }
                            user.setIntrests(arrayList);
                            ref.child(s).setValue(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }


}
