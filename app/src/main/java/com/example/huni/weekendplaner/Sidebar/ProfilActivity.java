package com.example.huni.weekendplaner.Sidebar;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huni.weekendplaner.Login.User;
import com.example.huni.weekendplaner.Main.ListDataEvent;
import com.example.huni.weekendplaner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfilActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        final String s = settings.getString("username","Dummy");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("User");
        List<User> listDataUser =  getUserDataFromDatabase(s);

        System.out.println("TAGIsten "+ s);
        System.out.println("TAGIsten "+ ref);

        TextView textView_firstName = findViewById(R.id.textView_firstName);
        TextView textView_lastName = findViewById(R.id.textView2_lastName);
        if(!listDataUser.isEmpty()) {
            textView_firstName.setText(listDataUser.get(0).getFirstname());
            textView_lastName.setText(listDataUser.get(0).getLastname());
            System.out.println("TAG771 " + listDataUser.size());
        }
    }


    public List<User> getUserDataFromDatabase(final String id){
        final List<User> list = new ArrayList<>();
        System.out.println("TAG771 1111");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    System.out.println("TAG771 " + dataSnapshot1.getKey() + '\t' + id);
                    if(Objects.equals(dataSnapshot1.getKey(), id)) {

                        User user = dataSnapshot1.getValue(User.class);
                        User listDataUser = new User();
                        assert user != null;
                        String firstname = user.getFirstname();
                        String lastname = user.getLastname();
                        listDataUser.setFirstname(firstname);
                        listDataUser.setLastname(lastname);
                        list.add(listDataUser);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Gone wrong",Toast.LENGTH_LONG).show();
            }
        });
        return list;
    }
}
