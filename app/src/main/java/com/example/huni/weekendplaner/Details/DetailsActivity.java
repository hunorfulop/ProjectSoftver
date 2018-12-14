package com.example.huni.weekendplaner.Details;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.huni.weekendplaner.R;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView textView_nameOfEvent = findViewById(R.id.textView_nameOfEvent);
        TextView textView_start = findViewById(R.id.textView2_start);
        TextView textView_end = findViewById(R.id.textView_end);
        TextView textView_address = findViewById(R.id.textView3_address);
        TextView textView_desc = findViewById(R.id.textView4_desc);
        ImageView imageView_img = findViewById(R.id.imageView2);

        String NameofEvent = getIntent().getStringExtra("NameofEvent");
        String DescofEvent = getIntent().getStringExtra("DescofEvent");
        String StartofEvent = getIntent().getStringExtra("StartofEvent");
        String EndofEvent = getIntent().getStringExtra("EndofEvent");
        String AdresofEvent = getIntent().getStringExtra("AdresofEvent");
        String AuthorfEvent = getIntent().getStringExtra("AuthorfEvent");
        String ImageofEvent = getIntent().getStringExtra("ImageofEvent");

        textView_nameOfEvent.setText(NameofEvent);
        textView_start.setText(StartofEvent);
        textView_end.setText(EndofEvent);
        textView_address.setText(AdresofEvent);
        textView_desc.setText(DescofEvent);

        Glide.with(this)
                .load(ImageofEvent)
                .into(imageView_img);
    }
}
