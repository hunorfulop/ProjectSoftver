package com.example.huni.weekendplaner.Sidebar;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.huni.weekendplaner.Login.User;
import com.example.huni.weekendplaner.Main.ListDataEvent;
import com.example.huni.weekendplaner.Main.RecyclerAdapter;
import com.example.huni.weekendplaner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IntrestsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mtoogle;
    NavigationView navigationView;
    DatabaseReference ref;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intrests);

        //Setting up the view elements
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Getting the current logged ind user
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        s = settings.getString("username", "Dummy");

        //Setting up the database connection
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");
        getIntrestsFromDatabase();

    }

    private void getIntrestsFromDatabase() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = new User(s);
                if (dataSnapshot.child(Objects.requireNonNull(ref.child(user.getPhonenumber()).getKey())).exists()){
                    //Getting the interest array form the user node
                    ArrayList<String> arrayList = new ArrayList<String>();
                    User newuser = dataSnapshot.child(s).getValue(User.class);
                    ArrayList<String> arrayList2 = newuser.getIntrests();
                    for(String arrayiterator : arrayList2){
                        arrayList.add(arrayiterator);
                    }
                    arrayList.remove("Dummy");
                    setEventForRecyclerView(arrayList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setEventForRecyclerView(ArrayList<String> arrayList){
        //Checking if the array is empty
        if(!arrayList.isEmpty()){
             getEvent(arrayList);
        }
    }

    public void getEvent(final ArrayList<String> nameOfEvent){
        //Setting up the Firebase reference
        FirebaseDatabase databaseEvent = FirebaseDatabase.getInstance();
        DatabaseReference refEvent = databaseEvent.getReference("Event");
        final ArrayList<ListDataEvent> listDataEvent = new ArrayList<ListDataEvent>();
        refEvent.addValueEventListener(new ValueEventListener() {
            @Override
            //Here we get all the data about the events form the intersts array
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (String eventName : nameOfEvent) {
                    if (!Objects.requireNonNull(dataSnapshot.child(eventName).getKey()).isEmpty()) {
                        ListDataEvent Event = dataSnapshot.child(eventName).getValue(ListDataEvent.class);
                        ListDataEvent dataEvent = new ListDataEvent();
                        String start_date = Event.getStart_date();
                        String nameOfEvent = Event.getNameOfEvent();
                        String imageOfEvent = Event.getImage();
                        String end_date = Event.getEnd_date();
                        String address = Event.getAddress();
                        String description = Event.getDescriptionOfEvent();
                        String author = Event.getAuthor();
                        String id = dataSnapshot.getKey();
                        dataEvent.setStart_date(start_date);
                        dataEvent.setNameOfEvent(nameOfEvent);
                        dataEvent.setImage(imageOfEvent);
                        dataEvent.setEnd_date(end_date);
                        dataEvent.setAddress(address);
                        dataEvent.setDescriptionOfEvent(description);
                        dataEvent.setAuthor(author);
                        dataEvent.setId(id);
                        listDataEvent.add(dataEvent);
                    }
                }
                setReyclerView(listDataEvent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setReyclerView(ArrayList<ListDataEvent> events){
        //Setting up the RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(events,this);
        recyclerView.setAdapter(adapter);
    }
}