package Setting;

import DataCache.DataCache;

public class Settings {

    // Basic Setup for DataCache
    private static Settings instance = new Settings();
    public static Settings getInstance() {
        return instance;
    }

    private Settings() {

    }

    // Variable Declarations - Settings menu
    private static boolean isLifeStoryLineOn = true;
    private static boolean isFamilyTreeLineOn = true;
    private static boolean isSpouseLineOn = true;
    private static boolean showFathersSide = true;
    private static boolean showMothersSide = true;
    private static boolean showMaleEvents = true;
    private static boolean showFemaleEvents = true;

    private boolean isEventActivity = false;


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
}