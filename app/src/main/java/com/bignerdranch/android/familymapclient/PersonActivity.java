package com.bignerdranch.android.familymapclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        // Set activity's action bar title
        getSupportActionBar().setTitle("Family Map: Person");
    }
}