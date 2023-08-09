package Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.familymapclient.SearchActivity;
import com.bignerdranch.android.familymapclient.SettingsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.bignerdranch.android.familymapclient.R;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Set;

import Model.Person;
import Model.Event;

import DataCache.DataCache;


// Android Iconify Library - draw dynamically customizable icons
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener { // Beginning of class
    private GoogleMap map;
    private DataCache dataCache;

    private TextView textViewBox;
    private ImageView textViewIcon;


    // Overridden to inflate the layout, set up the map, and initialize other necessary components
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);


        // Menu Options: display search and setting buttons
        setHasOptionsMenu(true); // map fragment has its own options menu
        Iconify.with(new FontAwesomeModule()); // in onCreate method, initialize the Iconify library

        // textview (icon, text)
        textViewBox = view.findViewById(R.id.textViewBox);
        textViewIcon = view.findViewById(R.id.textViewIcon);

        Drawable currentIcon;
        currentIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android).colorRes(R.color.mapIcon);
        textViewIcon.setImageDrawable(currentIcon);

        // Get the Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return view;
    }


    // method is called when the map is ready to be used
    // it initializes the map, clears it, and set up various markers and lines based on settings and data provided
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.clear();
        map.setOnMapLoadedCallback(this);


        // Set markers for all events (appear when the map is first shown)
        //createEventMarkers();

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.defaultMarker(200)));
        map.animateCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    // overridden to create options menu with search and settings buttons
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        // Set activity's action bar title
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.getSupportActionBar().setTitle("Family Map: Map");

        // Create the option menu (inflate the menu resource)
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_mapmenu, menu);

        // Find specific MenuItem objects
        MenuItem searchItem = menu.findItem(R.id.searchItem);
        MenuItem settingsItem = menu.findItem(R.id.settingsItem);

        // Customize MenuItems with FontAwesome icons
        searchItem.setIcon(new IconDrawable(getContext(),FontAwesomeIcons.fa_search)
                .colorRes(R.color.white)
                .actionBarSize());
        settingsItem.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_gear).actionBarSize()
                .colorRes(R.color.white)
                .actionBarSize());

    }


    // Switch to Search or Settings Activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;

        if(item.getItemId() == R.id.searchItem) {
            intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == R.id.settingsItem) {
            intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onContextItemSelected(item); // default
    }

    // method is called when a marker on the map is clicked.
    // It sets up the UI to display information related to the clicked event/person
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        return false;
    }


    // used to show all event markers on the map
    public void createEventMarkers() {


        Set<Event> allEvents = dataCache.getAllEvent();
        float customHue = 220;

        // Create event markers pulled from data cache
        for(Event event: allEvents) {
            LatLng currentPosition = new LatLng(event.getLatitude(),event.getLongitude());
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(customHue)));
            marker.setTag(event);
        }


    }

    // method sets up event parkers on the map based on the provided settings
    private void setEventsMarkersBySetting() {

    }

    // method creates the text to be displayed when a marker is clicked, showing information about the person and event
    private void makeEventText(Person person, Event event) {

    }

    // method creates lines between events based on the selected settings
    private void makeLinesBySettings(Person selectedPerson, Event selectedEvent) {

    }

    // method reads the user's preferences for displaying events and sets them accordingly
    private void setSetting() {

    }

    // method creates lines connecting a person to their parents and recursively create lines for their parents' parents
    private void createFamilyTreeLines(Person person, Event event) {

    }

    // helper method for createFamilyTreeLines
    // creates lines for the father and mother's sides
    private void createParentSideLine(Person person, Event event, float lineWidth) {

    }

    // method creates a line between a child event and its parent event
    private void makeLinesBetweenChildAndParent(Event childEvent, Event parentEvent, String parent, float lineWidth) {

    }

    // method removes all previously drawn lines from the map
    private void clearLines() {

    }

    // helper method for clearLines
    // remove a specific line from the map
    private void removeLine(ArrayList<Polyline> lines) {

    }

    // method creates lines representing a person's life events on the map
    private void createLifeEventsLines(String personID) {

    }

    // method creates a line connecting a person to their spouse's birth event
    private void createSpouseLine(Person person, Event event) {

    }




    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }
} // End of Class