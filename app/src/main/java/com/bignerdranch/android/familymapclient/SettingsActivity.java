package com.bignerdranch.android.familymapclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import DataCache.DataCache;
import Setting.Settings;


public class SettingsActivity extends AppCompatActivity  {

    // Variable Declarations
    private DataCache dataCache;
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set activity's action bar title
        getSupportActionBar().setTitle("Family Map: Settings");

        // get instance
        dataCache = DataCache.getInstance();
        settings = settings.getInstance();

        Switch lifeStoryLineSwitch = findViewById(R.id.lifeStoryLineButton);
        Switch familyTreeLineSwitch = findViewById(R.id.familyTreeLineButton);
        Switch spouseLineSwitch = findViewById(R.id.spouseLineButton);
        Switch fatherSideSwitch = findViewById(R.id.fatherSideButton);
        Switch motherSideSwitch = findViewById(R.id.motherSideButton);
        Switch maleEventSwitch = findViewById(R.id.maleEventButton);
        Switch femaleEventSwitch = findViewById(R.id.femaleEventButton);
        LinearLayout logoutScreen = findViewById(R.id.logout);

        // set all buttons to be checked
        lifeStoryLineSwitch.setChecked(settings.isLifeStoryLineOn()); // now is on, so return true
        familyTreeLineSwitch.setChecked(settings.isFamilyTreeLineOn());
        spouseLineSwitch.setChecked(settings.isSpouseLineOn());
        fatherSideSwitch.setChecked(settings.isShowFathersSide());
        motherSideSwitch.setChecked(settings.isShowMothersSide());
        maleEventSwitch.setChecked(settings.isShowMaleEvents());
        femaleEventSwitch.setChecked(settings.isShowFemaleEvents());

        // switch 1
        lifeStoryLineSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.setIsLifeStoryLineOn(lifeStoryLineSwitch.isChecked());
            }
        });

        // switch 2
        familyTreeLineSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.setIsFamilyTreeLineOn(familyTreeLineSwitch.isChecked());
            }
        });

        // switch 3
        spouseLineSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.setIsSpouseLineOn(spouseLineSwitch.isChecked());
            }
        });

        // switch 4
        fatherSideSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.setShowFathersSide(fatherSideSwitch.isChecked());
            }
        });

        // switch 5
        motherSideSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.setShowMothersSide(motherSideSwitch.isChecked());
            }
        });

        // switch 6
        maleEventSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.setShowMaleEvents(maleEventSwitch.isChecked());
            }
        });

        // switch 7
        femaleEventSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.setShowFemaleEvents(femaleEventSwitch.isChecked());
            }
        });



        // Functions to check
        addListenerForLogout(logoutScreen);
    }

    private void addListenerForLogout(LinearLayout logout) {
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dataCache.clearAll();
                settings.reset(); // newly added

                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
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