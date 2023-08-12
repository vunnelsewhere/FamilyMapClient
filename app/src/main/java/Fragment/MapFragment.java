package Fragment;

import android.content.Intent;
import android.graphics.Color; // draw polyline
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

import com.bignerdranch.android.familymapclient.PersonActivity;
import com.bignerdranch.android.familymapclient.SearchActivity;
import com.bignerdranch.android.familymapclient.SettingsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.bignerdranch.android.familymapclient.R;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import Model.Person;
import Model.Event;

import DataCache.DataCache;
import Setting.Settings;

// Android Iconify Library - draw dynamically customizable icons
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener { // Beginning of class
    private GoogleMap map;
    private DataCache dataCache;

    private TextView textViewBox;
    private ImageView textViewIcon;

    private Map<String, Float> markerColour = new HashMap<>();

    private Settings settings;
    private Polyline lifeStoryLine;
    private Polyline spouseLine;

    private Polyline familyTreeLine;

    private ArrayList<Polyline> fatherSideLine = new ArrayList<>();
    private ArrayList<Polyline> motherSideLine = new ArrayList<>();
    public static final int ORANGE_DARK = 0xffff8800;
    public static final int PURPLE = 0xFFAC4FC6;
    public static final int BLUE_DARK = 0xff00008b;

    private final float DEFAULT_LINE_WIDTH = 20.0f;

    private Person storeCurrentPerson;
    private Event storeCurrentEvent;

    private static final String PERSONID_KEY = "personID";
    private static final String EVENTID_KEY = "eventID";

    public MapFragment() {
        // Required empty public constructor
    }


    // Overridden to inflate the layout, set up the map, and initialize other necessary components
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        dataCache = DataCache.getInstance(); // don't forget to get an instance of the datacache class!!!!! Otherwise cannot invoke anyyyyy methods
        settings = Settings.getInstance();
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

        // interactive map allows the user to zoom in and out
        UiSettings setting = map.getUiSettings();
        setting.setZoomControlsEnabled(true);

        getDefaultSettings(); // all settings should be on

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.defaultMarker(200)));
        // map.animateCamera(CameraUpdateFactory.newLatLng(sydney));

        // Set markers for all events (appear when the map is first shown)
        createEventMarkersBySetting();


        if(settings.isEventActivity()) {
            setHasOptionsMenu(false);

            if(getArguments().getString("eventID") != null) {
                storeCurrentEvent = dataCache.getEventWithID(getArguments().getString("eventID"));
                storeCurrentPerson = dataCache.getPersonByID(storeCurrentEvent.getPersonID());

                // center event
                map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(storeCurrentEvent.getLatitude(), storeCurrentEvent.getLongitude())));

                updatetextViewIcon(storeCurrentPerson);
                updatetextViewBox(storeCurrentPerson, storeCurrentEvent);

                drawLinesBySettings(storeCurrentPerson, storeCurrentEvent);

            }
        }



        map.setOnMarkerClickListener(this); // need this for markers to be clickable, so onMarkerClick will function

        // textview is not clickable for now
        textViewBox.setClickable(false);
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



    // used to show all event markers on the map
    public void createEventMarkersBySetting() {


        Set<Event> allEvents = settings.settingFilters(); // settings.settingFilters(); // dataCache.getAllEvent()
        markerColour = dataCache.getEventTypeColours();

        for(Event event: allEvents) {
            LatLng currentPosition = new LatLng(event.getLatitude(),event.getLongitude());
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColour.get(event.getEventType().toLowerCase()))));
            marker.setTag(event);
        }

    }

    // method is called when a marker on the map is clicked.
    // It sets up the UI to display information related to the clicked event/person
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Event currentEvent = (Event) marker.getTag(); // current clicked event
        storeCurrentEvent = currentEvent;
        Person currentPerson = dataCache.getPersonByID(currentEvent.getPersonID()); // person associated with the event
        storeCurrentPerson = currentPerson;

        updatetextViewIcon(currentPerson);
        updatetextViewBox(currentPerson,currentEvent);

        textViewBox.setClickable(true); // set it to be clickable when a marker is clicked
        addListenerForTextView(textViewBox,currentPerson); // question here

        // draw the lines
        drawLinesBySettings(currentPerson, currentEvent);

        return false; // default(?)
    }

    private void updatetextViewIcon(Person currentClickedPerson) {
        if(currentClickedPerson.getGender().equalsIgnoreCase("m")) {
            Drawable maleIcon;
            maleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.maleIcon);
            textViewIcon.setImageDrawable(maleIcon);
        }
        else if(currentClickedPerson.getGender().equalsIgnoreCase("f")) {
            Drawable femaleIcon;
            femaleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.femaleIcon);
            textViewIcon.setImageDrawable(femaleIcon);
        }
    }

    private void updatetextViewBox(Person currentClickedPerson, Event currentClickedEvent) {
        String textMessage = currentClickedPerson.getFirstName() + " " + currentClickedPerson.getLastName()
                + "\n" + currentClickedEvent.getEventType() + ": " + currentClickedEvent.getCity() +
                " " + currentClickedEvent.getCountry() + " (" + currentClickedEvent.getYear() + ")";
        textViewBox.setText(textMessage);
    }

    private void addListenerForTextView(TextView theBox, Person currentClickedPerson) {
        theBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra(PERSONID_KEY, currentClickedPerson.getPersonID()); // need person ID to pull up info
                startActivity(intent);
            }
        });
    }

    private void getDefaultSettings() {
        settings.setIsLifeStoryLineOn(true);
        settings.setIsFamilyTreeLineOn(true);
        settings.setIsSpouseLineOn(true);
        settings.setShowFathersSide(true);
        settings.setShowMothersSide(true);
        settings.setShowMaleEvents(true);
        settings.setShowFemaleEvents(true);
    }



    // method creates lines between events based on the selected settings
    private void drawLinesBySettings(Person selectedPerson, Event selectedEvent) {
        // clear the 3 lines beforehead
        String gender = selectedPerson.getGender();

        if((gender.equals("m") && settings.isShowMaleEvents())
                || (gender.equals("f") && settings.isShowFemaleEvents())) {
            // Create Life Story Line
            if (settings.isLifeStoryLineOn()) {
                createLifeEventsLines(selectedPerson.getPersonID());
            }

            if((gender.equals("m") && settings.isShowFemaleEvents())
                    || (gender.equals("f") && settings.isShowMaleEvents()))
            // Create Spouse Line
            if (settings.isSpouseLineOn()) {
                createSpouseLine(selectedPerson, selectedEvent);
            }


            if(settings.isShowMothersSide() || settings.isShowFathersSide()) {
                // Create Family Tree Line
                if (settings.isFamilyTreeLineOn()) {

                    if (fatherSideLine != null) {
                        for (Polyline line : fatherSideLine) {
                            line.remove();
                        }
                    }

                    if (motherSideLine != null) {
                        for (Polyline line : motherSideLine) {
                            line.remove();
                        }
                    }

                    createFamilyTreeLines(selectedPerson, selectedEvent, DEFAULT_LINE_WIDTH);
                }
            }
        }
    }


    // method creates lines representing a person's life events on the map
    private void createLifeEventsLines(String personID) {
        if(lifeStoryLine != null) { // clear previously generated lifestoryline for other person (?)
            lifeStoryLine.remove();
        }

        ArrayList<Event> lifeStoryList = dataCache.getLifeStoryEventsForSpecifiedPerson(personID);
        ArrayList<LatLng> eventPoints = new ArrayList<>();
        for(int i = 0; i < lifeStoryList.size(); i++) {
            Event currentEvent = lifeStoryList.get(i);
            Float currentLatitude = currentEvent.getLatitude();
            Float currentLongitude = currentEvent.getLongitude();
            eventPoints.add(new LatLng(currentLatitude,currentLongitude));
        }

        lifeStoryLine = map.addPolyline(new PolylineOptions().addAll(eventPoints).color(ORANGE_DARK)); // .color(Color.GREEN) OR .color(selfDefined)

    }

    // method creates a line connecting a person to their spouse's birth event
    private void createSpouseLine(Person person, Event event) {
        if(spouseLine != null) { // clear (?)
            spouseLine.remove();
        }
        // no line is drawn if the person has no recorded spouse
        if(person.getSpouseID() != null) {
            Person spouse = dataCache.getPersonByID(person.getSpouseID());
            Event spouseFirstEvent = dataCache.getLifeStoryEventsForSpecifiedPerson(spouse.getPersonID()).get(0); // spouseFirstEvent only accessible inside the if-block

            LatLng startPoint = new LatLng(event.getLatitude(),event.getLongitude());
            LatLng endPoint = new LatLng(spouseFirstEvent.getLatitude(),spouseFirstEvent.getLongitude());

            spouseLine = map.addPolyline(new PolylineOptions().add(startPoint).add(endPoint).color(PURPLE));
        }


    }


    // method creates lines connecting a person to their parents and recursively create lines for their parents' parents
    private void createFamilyTreeLines(Person currentPerson, Event currentEvent, float lineWidth) {

        // father
        if(currentPerson.getFatherID()!=null && settings.isShowFathersSide() && settings.isShowMaleEvents()) {
            Person userFather = dataCache.getPersonByID(currentPerson.getFatherID());
            ArrayList<Event> fatherEvents = dataCache.getLifeStoryEventsForSpecifiedPerson(userFather.getPersonID());
            Event fatherFirstEvent = fatherEvents.get(0);
            makeLinesForChildAndParent(currentEvent,fatherFirstEvent,"father",lineWidth);
            createFamilyTreeLines(userFather,fatherFirstEvent,lineWidth - 5.0f);
        }

        // mother
        if(currentPerson.getMotherID()!=null && settings.isShowMothersSide() && settings.isShowFemaleEvents()) {
            Person userMother = dataCache.getPersonByID(currentPerson.getMotherID());
            ArrayList<Event> motherEvents = dataCache.getLifeStoryEventsForSpecifiedPerson(userMother.getPersonID());
            Event motherFirstEvent = motherEvents.get(0);
            makeLinesForChildAndParent(currentEvent,motherFirstEvent,"mother",lineWidth);
            createFamilyTreeLines(userMother,motherFirstEvent,lineWidth - 5.0f);
        }

    }


    // method creates a line between a child event and its parent event
    private void makeLinesForChildAndParent(Event childEvent, Event parentEvent, String parent, float lineWidth) {

        LatLng startPoint = new LatLng(childEvent.getLatitude(),childEvent.getLongitude());
        LatLng endPoint = new LatLng(parentEvent.getLatitude(),parentEvent.getLongitude());

        PolylineOptions options = new PolylineOptions()
                .add(startPoint)
                .add(endPoint)
                .color(BLUE_DARK)
                .width(lineWidth);

        familyTreeLine = map.addPolyline(options);

        if(parent.equalsIgnoreCase("father")) {
            fatherSideLine.add(familyTreeLine);
        }
        else if(parent.equalsIgnoreCase("mother")) {
            motherSideLine.add(familyTreeLine);
        }
    }


    // Go back to the map with new Settings!!!!!
    @Override
    public void onResume() {
        super.onResume();

        if(map != null) {
            map.clear();
            createEventMarkersBySetting();
            drawLinesBySettings(storeCurrentPerson,storeCurrentEvent);
        }
    }



    // method removes all previously drawn lines from the map
    private void clearLines() {

    }

    // helper method for clearLines
    // remove a specific line from the map
    private void removeLine(ArrayList<Polyline> lines) {

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




 /*

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

         */