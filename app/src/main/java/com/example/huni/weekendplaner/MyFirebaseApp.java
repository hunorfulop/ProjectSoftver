package com.example.huni.weekendplaner;

import com.google.firebase.database.FirebaseDatabase;

//Class used for optimizing data loading from Firebase
public class MyFirebaseApp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

