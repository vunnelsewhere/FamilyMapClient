package DataCache;

/*
 * A class that will store all the data for the application
 * Once we've downloaded the family tree data from the server, we're going to store it in the DataCache
 */

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
    private Person user;
    private final Set<Person> userPeople = new HashSet<>();
    private final Map<String, Person> personByID = new HashMap<>();

    public void setData(String userPersonID, PersonResult people, EventResult events) {
        setPeople(people);

        user = getPersonByID(userPersonID);

    }

    public Person getUser() {
        return user;
    }

    public Person getPersonByID(String personID) {
        return personByID.get(personID);
    }

    public void setPeople(PersonResult people) {
        List<Person> allPeopleList = people.getData();

        // Loop through all the people
        for (Person person : allPeopleList) {
            // Add the people for the user
            userPeople.add(person);
            personByID.put(person.getPersonID(), person);
        }
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