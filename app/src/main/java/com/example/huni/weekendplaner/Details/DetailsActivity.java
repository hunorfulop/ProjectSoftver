package com.example.huni.weekendplaner.Details;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.huni.weekendplaner.R;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView textView = findViewById(R.id.textView_nameOfEvent);


        String NameofEvent = getIntent().getStringExtra("NameofEvent");
        String DescofEvent = getIntent().getStringExtra("DescofEvent");
        String StartofEvent = getIntent().getStringExtra("StartofEvent");
        String EndofEvent = getIntent().getStringExtra("EndofEvent");
        String AdresofEvent = getIntent().getStringExtra("AdresofEvent");
        String AuthorfEvent = getIntent().getStringExtra("AuthorfEvent");
        String ImageofEvent = getIntent().getStringExtra("ImageofEvent");
        textView.setText(NameofEvent);
    }
}
