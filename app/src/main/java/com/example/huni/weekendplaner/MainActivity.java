package com.example.huni.weekendplaner;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mtoogle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        mdrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mtoogle = new ActionBarDrawerToggle(this,mdrawerlayout,R.string.action_open,R.string.action_close);
        mdrawerlayout.addDrawerListener(mtoogle);
        mtoogle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }


    public boolean onOptionsItemSeleted(MenuItem itme){
        if(mtoogle.onOptionsItemSelected(itme)){
            return true;
        }
        return super.onOptionsItemSelected(itme);
    }

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
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
