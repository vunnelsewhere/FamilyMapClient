package com.bignerdranch.android.familymapclient;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;

import DataCache.DataCache;
import Setting.Settings;
import Model.*;

public class PersonActivity extends AppCompatActivity {

    // Variable Declarations
    private DataCache dataCache;
    private Settings settings;
    private static final String PERSONID_KEY = "personID";
    private static final String EVENTID_KEY = "eventID";

    private Person clickedPerson;
    private List<Event> lifeEventsList;
    private List<Person> familyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        // Set activity's action bar title
        getSupportActionBar().setTitle("Family Map: Person");

        dataCache = DataCache.getInstance();
        settings = Settings.getInstance();

        // Grab the clicked person
        Intent intent = getIntent();
        String personID = intent.getStringExtra(PERSONID_KEY);
        clickedPerson = dataCache.getPersonByID(personID);

        TextView clickedFirstName = findViewById(R.id.clickedPersonFirstName);
        TextView clickedLastName = findViewById(R.id.clickedPersonLastName);
        TextView clickedGender = findViewById(R.id.clickedPersonGender);

        clickedFirstName.setText(clickedPerson.getFirstName());
        clickedLastName.setText(clickedPerson.getLastName());

        String gender = clickedPerson.getGender();
        if (gender.equals("m")) {
            clickedGender.setText("Male");
        } else if (gender.equals("f")) {
            clickedGender.setText("Female");
        }

        familyList = dataCache.getFamilyList(clickedPerson);

        if((clickedPerson.getGender().equals("m") && !settings.isShowMaleEvents())
                ||(clickedPerson.getGender().equals("f") && !settings.isShowFemaleEvents())) {
            lifeEventsList = new ArrayList<>(); // don't set that to null
        }
        else {
            lifeEventsList = dataCache.getLifeStoryEventsForSpecifiedPerson(clickedPerson.getPersonID());
        }


        // Expandable List - set the adapter for the ExpandableListView in onCreate(Bundle)
        ExpandableListView expandable = findViewById(R.id.expandableListView);
        expandable.setAdapter(new ExpandableListAdapter());

    }

    // Subclass BaseExpandableListAdapter
    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        // Variable Declarations
        private static final int LIFE_EVENTS_POSITION = 0;
        private static final int FAMILY_POSITION = 1;


        // Override Methods
        @Override
        public int getGroupCount() {
            return 2; // LIFE EVENTS & FAMILY
        }

        @Override
        public int getChildrenCount(int groupPosition) { // return the number of children in the group
            switch (groupPosition) {
                case LIFE_EVENTS_POSITION:
                    return lifeEventsList.size();
                case FAMILY_POSITION:
                    return familyList.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position" + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_POSITION:
                    return lifeEventsList.size();
                case FAMILY_POSITION:
                    return familyList.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position" + groupPosition);
            }
        }

        // each group with different child
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_POSITION:
                    return lifeEventsList.get(childPosition);
                case FAMILY_POSITION:
                    return familyList.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position" + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }


        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case LIFE_EVENTS_POSITION:
                    titleView.setText(R.string.eventTitle);
                    break;
                case FAMILY_POSITION:
                    titleView.setText(R.string.familyTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position" + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch (groupPosition) {
                case LIFE_EVENTS_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event, parent, false);
                    initializeLifeEventView(itemView, childPosition);
                    break;
                case FAMILY_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.family, parent, false);
                    initializeFamilyView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeLifeEventView(View lifeEventView, int childPosition) {
            Event childEvent = lifeEventsList.get(childPosition);
            Person eventPerson = dataCache.getPersonByID(childEvent.getPersonID());


            if (childEvent != null) {

                // Image View
                ImageView icon = lifeEventView.findViewById(R.id.markerIcon);
                Drawable eventIcon;
                eventIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker)
                        .colorRes(R.color.mapIcon);
                icon.setImageDrawable(eventIcon);

                // Text View - event info
                TextView eventView = lifeEventView.findViewById(R.id.eventInfo);
                String eventInfo = childEvent.getEventType().toUpperCase() + ": " + childEvent.getCity()
                        + ", " + childEvent.getCountry() + " (" + childEvent.getYear() + ")";
                eventView.setText(eventInfo);

                // Text View - person name
                TextView personName = lifeEventView.findViewById(R.id.eventPerson);
                String fullName = eventPerson.getFirstName() + " " + eventPerson.getLastName();
                personName.setText(fullName);

                // add listener
                lifeEventView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                        intent.putExtra(EVENTID_KEY, childEvent.getEventID());
                        startActivity(intent);
                    }
                });
            }


        }

        private void initializeFamilyView(View familyView, int childPosition) {
            Person childPerson = familyList.get(childPosition);

            if (childPerson != null) {

                // Image View
                ImageView icon = familyView.findViewById(R.id.genderIcon);
                Drawable genderIcon;

                if (childPerson.getGender().equals("m")) {
                    genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male)
                            .colorRes(R.color.maleIcon);
                } else {
                    genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female)
                            .colorRes(R.color.femaleIcon);
                }

                icon.setImageDrawable(genderIcon);

                // Text View - person name
                TextView name = familyView.findViewById(R.id.thePersonName);
                String fullName = childPerson.getFirstName() + " " + childPerson.getLastName();
                name.setText(fullName);

                // Text View - relationship
                TextView relationship = familyView.findViewById(R.id.relationship);
                String relation = "";

                if (childPerson.getPersonID().equals(clickedPerson.getFatherID())) {
                    relation = "Father";
                }

                if (childPerson.getPersonID().equals(clickedPerson.getMotherID())) {
                    relation = "Mother";
                }

                if (childPerson.getPersonID().equals(clickedPerson.getSpouseID())) {
                    relation = "Spouse";
                }

                if (childPerson.getFatherID() != null) {
                    if (childPerson.getFatherID().equals(clickedPerson.getPersonID())) {
                        relation = "Child";
                    }
                }

                if (childPerson.getMotherID() != null) {
                    if (childPerson.getMotherID().equals(clickedPerson.getPersonID())) {
                        relation = "Child";
                    }
                }

                relationship.setText(relation);

                // add listener
                familyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                        intent.putExtra(PERSONID_KEY, childPerson.getPersonID());
                        startActivity(intent);
                    }
                });


            }

        }


        @Override
        public boolean isChildSelectable(int i, int i1) {

            return true;

        }
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



/*
  if((clickedPerson.getGender().equals("m") && !settings.isShowMaleEvents())
                    ||(clickedPerson.getGender().equals("f") && !settings.isShowFemaleEvents())) {





 */



