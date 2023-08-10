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

    private static final String PERSONID_KEY = "PERSONID"; // person activity


    // Lines
    private final Map<String, ArrayList<Event>> lifeStoryEvents = new HashMap<>(); // get person's event order chronologically

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
            setLifeStoryEvents(event,event.getPersonID());
        }

    }

    private void setEventTypesArray(Event currentEvent) {
        String eventType = currentEvent.getEventType().toLowerCase();
        eventTypes.add(eventType);
    }

    private void setLifeStoryEvents(Event currentEvent, String personID) {
        if(lifeStoryEvents.containsKey(personID)) { // already hv key value
            ArrayList<Event> oldLifeStoryList = lifeStoryEvents.get(personID);

            // Birth event
            if(currentEvent.getEventType().equalsIgnoreCase("birth")) {
                oldLifeStoryList.add(0,currentEvent);
            }
            // Death event
            else if(currentEvent.getEventType().equalsIgnoreCase("death")) {
                int lastIndex = oldLifeStoryList.size();
                oldLifeStoryList.add(lastIndex, currentEvent);

            }
            // Events other than Birth & Death
            else {

                for(int i = 0; i < oldLifeStoryList.size(); i++) {
                    Event eventToCompare = oldLifeStoryList.get(i);


                    // events sorted primarily by year,
                    if(currentEvent.getYear() < eventToCompare.getYear()) {
                        oldLifeStoryList.add(i, currentEvent);
                        break;
                    }
                    // and secondarily by event type normalized to lower-case
                    else if(currentEvent.getYear() == eventToCompare.getYear()) {
                        if(currentEvent.getEventType().toLowerCase().compareTo(eventToCompare.getEventType().toLowerCase()) < 0) {
                            oldLifeStoryList.add(i, currentEvent);
                            break;
                        }
                    }

                    // if all the above criteria doesn't fit? none of the previous conditions are met
                    if(i == oldLifeStoryList.size() - 1 ) {
                        oldLifeStoryList.add(oldLifeStoryList.size(), currentEvent);
                        break;
                    }
                }

            }

            // don't forget to update the List!!
            lifeStoryEvents.put(personID, oldLifeStoryList);

        }
        // no key generated yet, nothing in the array so order does not matter
        else {
            ArrayList<Event> newLifeStoryList = new ArrayList<>();
            newLifeStoryList.add(currentEvent);
            lifeStoryEvents.put(personID, newLifeStoryList);
        }
    }

    public ArrayList<Event> getLifeStoryEventsForSpecifiedPerson(String personID) {
        return lifeStoryEvents.get(personID);
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