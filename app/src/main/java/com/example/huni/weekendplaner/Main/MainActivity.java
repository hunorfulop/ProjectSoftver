package com.example.huni.weekendplaner.Main;

import android.content.Intent;
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
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.huni.weekendplaner.Sidebar.IntrestsActivity;
import com.example.huni.weekendplaner.Login.LoginActivity;
import com.example.huni.weekendplaner.Sidebar.ProfilActivity;
import com.example.huni.weekendplaner.R;
import com.example.huni.weekendplaner.Sidebar.Upload;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mtoogle;
    NavigationView navigationView;
    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        //Setting up the DrawerLayout for the sidebar
        mdrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mtoogle = new ActionBarDrawerToggle(this,mdrawerlayout,R.string.action_open,R.string.action_close);
        mdrawerlayout.addDrawerListener(mtoogle);
        mtoogle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setting up the database connection
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Event");
        List<ListDataEvent> listDataEvents =  getDataFromDatabase();

        //Setting up the Recyclerview and calling its adapter
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(listDataEvents,this);
        recyclerView.setAdapter(adapter);

        //With SharedPreferences we get the current user
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        final String s = settings.getString("username","Dummy");

        //Here we call the function which sets profile picture in the sidebar imageview
        getProfilePicture(s);
    }

    //With this function we get the profile picture form the database
    public void getProfilePicture(final String s){
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("uploads");
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if(Objects.equals(s, postSnapshot.getKey())){
                        setProfilePicture(upload.getmImageUrl());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //With this function we load the profile picture in the sidebar imageview
    public void setProfilePicture(String url){
        ImageView imageView = findViewById(R.id.header_profile_picture);
        Glide.with(this).load(url).into(imageView);
    }

    //In this function we get all the events from the database in an arrylist
    public List<ListDataEvent> getDataFromDatabase(){
        final List<ListDataEvent> list = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    ListDataEvent Event = dataSnapshot1.getValue(ListDataEvent.class);
                    ListDataEvent listDataEvent = new ListDataEvent();
                    assert Event != null;
                    String start_date = Event.getStart_date();
                    String nameOfEvent = Event.getNameOfEvent();
                    String imageOfEvent = Event.getImage();
                    String end_date = Event.getEnd_date();
                    String address =  Event.getAddress();
                    String description = Event.getDescriptionOfEvent();
                    String author = Event.getAuthor();
                    String id = dataSnapshot1.getKey();
                    listDataEvent.setStart_date(start_date);
                    listDataEvent.setNameOfEvent(nameOfEvent);
                    listDataEvent.setImage(imageOfEvent);
                    listDataEvent.setEnd_date(end_date);
                    listDataEvent.setAddress(address);
                    listDataEvent.setDescriptionOfEvent(description);
                    listDataEvent.setAuthor(author);
                    listDataEvent.setId(id);
                    list.add(listDataEvent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Something Gone wrong",Toast.LENGTH_LONG).show();
            }
        });
                return list;
    }


    //In this function we set up the sidebar wit its elements and the navigation of its elements
    public boolean onNavigationItemSelected(MenuItem menuItem){
        menuItem.setChecked(true);

        switch (menuItem.getItemId()){
            case R.id.sidebar_Profile:
                startActivity(new Intent(getApplicationContext(),ProfilActivity.class));
                break;

            case R.id.sidebar_Intrests:
                startActivity(new Intent(getApplicationContext(), IntrestsActivity.class));
                break;

            case R.id.sidebar_Logout:
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("username");
                editor.apply();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
