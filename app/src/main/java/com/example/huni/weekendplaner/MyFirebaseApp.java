package com.example.huni.weekendplaner;

import com.google.firebase.database.FirebaseDatabase;

//This class is used to optimize the data loading form the database
public class MyFirebaseApp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

