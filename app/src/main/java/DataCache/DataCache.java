package DataCache;

/*
 * A class that will store all the data for the application
 * Once we've downloaded the family tree data from the server, we're going to store it in the DataCache
 */

import com.google.android.gms.maps.model.BitmapDescriptorFactory;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



import Model.Person;
import Model.Event;
import Result.EventResult;
import Result.PersonResult;

public class DataCache {

    // Basic Setup for DataCache
    private static DataCache instance = new DataCache();

    public static DataCache getInstance() {
        return instance;
    }

    private DataCache() {

    }

    // Variable Declarations

    // Main Activity
    private Person user;

    private final Set<Person> allPeople = new HashSet<>();
    private final Map<String, Person> peopleWithID = new HashMap<>(); // personID, person

    private final Set<Event> allEvent = new HashSet<>();
    private final Map<String, Event> eventWithID = new HashMap<>();
    private final Set<String> eventTypes = new HashSet<>(); // types of events (different colors)

    // Color for Markers
    private float[] colourBank;
    private final Map<String, Float> eventTypeColours = new HashMap<>(); // store colors associated with different event type




    // First major step to set data after register/sign-in
    public void setData(String userPersonID, PersonResult people, EventResult events) {

        // Main Activity
        setPeople(people);
        user = getPersonByID(userPersonID);

        // event data
        setEvents(events);

        // Set Color for different event type
        setColorBank();
        setMarkerColours();

    }



    // Getters and Setters - Main Activity

    public Person getUser() {
        return user;
    }

    public Person getPersonByID(String personID) {
        return peopleWithID.get(personID); // return the associated person (user) with personID
    }

    public void setPeople(PersonResult people) {
        ArrayList<Person> allPeopleList = people.getData();

        // Loop through all the people
        for (Person person : allPeopleList) {
            // Add the people for the user
            allPeople.add(person);
            peopleWithID.put(person.getPersonID(), person);
        }
    }

    public void setEvents(EventResult result) {
        ArrayList<Event> allEventsList = result.getData();

        // Loop through all the event
        for(Event event: allEventsList) {
            allEvent.add(event);
            eventWithID.put(event.getEventID(),event);

            setEventTypesArray(event);
        }

    }

    private void setEventTypesArray(Event currentEvent) {
        String eventType = currentEvent.getEventType().toLowerCase();
        eventTypes.add(eventType);
    }
    private void setColorBank() {
        colourBank = new float[]{BitmapDescriptorFactory.HUE_AZURE,
                BitmapDescriptorFactory.HUE_BLUE,
                BitmapDescriptorFactory.HUE_CYAN,
                BitmapDescriptorFactory.HUE_GREEN,
                BitmapDescriptorFactory.HUE_MAGENTA,
                BitmapDescriptorFactory.HUE_ORANGE,
                BitmapDescriptorFactory.HUE_RED,
                BitmapDescriptorFactory.HUE_ROSE,
                BitmapDescriptorFactory.HUE_VIOLET,
                BitmapDescriptorFactory.HUE_YELLOW};

    }

    public void setMarkerColours() { // 10 different types in database
        int count = 0;
        for(String eventType: eventTypes) {
            int colourIndex = count % 10;
            eventTypeColours.put(eventType, getColourBank()[colourIndex]);
            count++;
        }

    }


    /*************************** General Getters ***************************/

    public Set<Person> getAllPeople() {
        return allPeople;
    }

    public Map<String, Person> getPeopleWithID() {
        return peopleWithID;
    }

    public Set<Event> getAllEvent() {
        return allEvent;
    }

    public Map<String, Event> getEventWithID() {
        return eventWithID;
    }

    public Set<String> getEventTypes() {
        return eventTypes;
    }

    public float[] getColourBank() {
        return colourBank;
    }

    public Map<String, Float> getEventTypeColours() {
        return eventTypeColours;
    }
}


// List<Person> people;
// List<Event> event;

// Map<PersonId,Person> people
// Map<EventId, Event> events;
// Map<PersonId, List<Event>> personEvents;

// Set<PersonId> paternalAncestors;
// Set<PersonId> maternalAncestors;

// Settings settings;

// Person getPersonById(Personid id) {}
// Event getEventById(Eventid id) {}
// List<Event> getPersonEvents(Personid id) {}