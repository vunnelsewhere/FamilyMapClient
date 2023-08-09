package Task;

// Main Activity
import DataCache.DataCache;
import Result.EventResult;
import Result.PersonResult;
import ServerProxy.ServerProxy;

public class DataTask {

    // Variable Declaration
    private String authToken;
    private final ServerProxy proxy;
    private final DataCache dataCache;

    private String firstName;
    private String lastName;

    public DataTask(String authToken, String serverHost, String serverPort) {
        this.authToken = authToken;
        proxy = new ServerProxy();
        proxy.setServerHost(serverHost);
        proxy.setServerPort(serverPort);
        dataCache = DataCache.getInstance();
    }

    public void setData(String personID) {
        PersonResult result1 = proxy.getPeople(authToken);
        EventResult result2 = proxy.getEvents(authToken);
        dataCache.setData(personID, result1, result2);

        // Main Activity
        firstName = dataCache.getUser().getFirstName();
        lastName = dataCache.getUser().getLastName();
    }



    // Getters and Setters - Main Activity
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}