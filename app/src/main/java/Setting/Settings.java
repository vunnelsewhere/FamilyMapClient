package Setting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import DataCache.DataCache;
import Model.*;

public class Settings {

    // Basic Setup for DataCache
    private static Settings instance = new Settings();
    public static Settings getInstance() {
        return instance;
    }

    private Settings() {

    }

    // Variable Declarations - Settings menu
    private DataCache dataCache;
    private static boolean isLifeStoryLineOn = true;
    private static boolean isFamilyTreeLineOn = true;
    private static boolean isSpouseLineOn = true;
    private static boolean showFathersSide = true;
    private static boolean showMothersSide = true;
    private static boolean showMaleEvents = true;
    private static boolean showFemaleEvents = true;

    private boolean isEventActivity = false;


    // Filter Settings (4)
    public Set<Event> settingFilters() {
        dataCache = DataCache.getInstance();
        Set<Event> allEvents = dataCache.getAllEvent();
        Set<Event> filteredEvent = new HashSet<>();
        filteredEvent.addAll(allEvents);

        if(!showFathersSide) { // don't just do filteredEvent.removeAll(paternalAncestors) bcuz also removed mother's paternalAncestors

            for(Person person: dataCache.getAllPeople()) {
                if(dataCache.getPaternalAncestors().contains(person)) {
                    ArrayList<Event> eventList = dataCache.getLifeStoryEventsForSpecifiedPerson(person.getPersonID());
                    filteredEvent.removeAll(eventList); // remove all father events with specified user

                }
            }


            // filteredEvent.removeAll(dataCache.getPaternalAncestors());
        }

        if(!showMothersSide) { // only father side events

            for(Person person: dataCache.getAllPeople()) {
                if(dataCache.getMaternalAncestors().contains(person)) {
                    ArrayList<Event> eventList = dataCache.getLifeStoryEventsForSpecifiedPerson(person.getPersonID());
                    filteredEvent.removeAll(eventList); // remove all father events with specified user
                }
            }


            // filteredEvent.removeAll(dataCache.getMaternalAncestors()); // cannot remove person from event
        }

        if(!showMaleEvents) {
            filteredEvent.removeAll(dataCache.getMaleEvents());
        }

        if(!showFemaleEvents) {
            filteredEvent.removeAll(dataCache.getFemaleEvents());
        }

        return filteredEvent;
    }

    public void reset() {
        isLifeStoryLineOn = true;
        isFamilyTreeLineOn = true;
        isSpouseLineOn = true;
        showFathersSide = true;
        showMothersSide = true;
        showMaleEvents = true;
        showFemaleEvents = true;

        isEventActivity = false;
    }


    // Setters and Getters

    public static boolean isLifeStoryLineOn() {
        return isLifeStoryLineOn;
    }

    public static void setIsLifeStoryLineOn(boolean isLifeStoryLineOn) {
        Settings.isLifeStoryLineOn = isLifeStoryLineOn;
    }

    public static boolean isFamilyTreeLineOn() {
        return isFamilyTreeLineOn;
    }

    public static void setIsFamilyTreeLineOn(boolean isFamilyTreeLineOn) {
        Settings.isFamilyTreeLineOn = isFamilyTreeLineOn;
    }

    public static boolean isSpouseLineOn() {
        return isSpouseLineOn;
    }

    public static void setIsSpouseLineOn(boolean isSpouseLineOn) {
        Settings.isSpouseLineOn = isSpouseLineOn;
    }

    public static boolean isShowFathersSide() {
        return showFathersSide;
    }

    public static void setShowFathersSide(boolean showFathersSide) {
        Settings.showFathersSide = showFathersSide;
    }

    public static boolean isShowMothersSide() {
        return showMothersSide;
    }

    public static void setShowMothersSide(boolean showMothersSide) {
        Settings.showMothersSide = showMothersSide;
    }

    public static boolean isShowMaleEvents() {
        return showMaleEvents;
    }

    public static void setShowMaleEvents(boolean showMaleEvents) {
        Settings.showMaleEvents = showMaleEvents;
    }

    public static boolean isShowFemaleEvents() {
        return showFemaleEvents;
    }

    public static void setShowFemaleEvents(boolean showFemaleEvents) {
        Settings.showFemaleEvents = showFemaleEvents;
    }

    public boolean isEventActivity() {
        return isEventActivity;
    }

    public void setEventActivity(boolean eventActivity) {
        isEventActivity = eventActivity;
    }
}
