package com.bignerdranch.android.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import Fragment.MapFragment;
import DataCache.DataCache;
import Setting.Settings;
import Model.*;

public class EventActivity extends AppCompatActivity {

    // Variable Declarations
    private DataCache dataCache;
    private Settings settings;

    private static final String EVENTID_KEY = "eventID";

    private Event clickedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Set activity's action bar title
        getSupportActionBar().setTitle("Family Map: Event");

        dataCache = DataCache.getInstance();
        settings = Settings.getInstance(); // forget again!! that's why event activity don't work
        // Grab the clicked person
        Intent intent = getIntent();
        String eventID = intent.getStringExtra(EVENTID_KEY);
        //clickedEvent = dataCache.getEventFromALL(eventID);

        // Set Map Fragment to be "Event" version
        settings.setEventActivity(true);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.eventFragmentFrameLayout); // don't use the map fragment layout first!!

        if(fragment == null) {
            fragment = createMapFragment(eventID);

            fragmentManager.beginTransaction()
                    .replace(R.id.eventFragmentFrameLayout, fragment)
                    .commit();
        }
    }

    private Fragment createMapFragment(String eventID) {
        MapFragment fragment = new MapFragment();

        // Map fragment returns clicked event
        Bundle arguments = new Bundle();
        arguments.putString(EVENTID_KEY, eventID);
        fragment.setArguments(arguments);

        // fragment.registerListener(this);
        return fragment;
    }



    // UP button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}