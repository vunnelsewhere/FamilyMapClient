package ServerProxy;

/*
 * A class that the client is going to use to communicate with your server
 */

import java.io.*;
import java.net.*;

import com.google.gson.Gson;

import Result.*;
import Request.*;

public class ServerProxy {


    // Variable Declarations
    private String serverHost;
    private String serverPort;

    protected Gson gson = new Gson();


    // Getters and Setters
    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerHost() {
        return serverHost;
    }

    public String getServerPort() {
        return serverPort;
    }


    // Register
    public RegisterResult register(RegisterRequest request) { // Method: POST

        // Initial Variable Declarations
        RegisterResult result;

        try {
            URL url = new URL("http://" + getServerHost() + ":" + getServerPort() + "/user/register");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);    // There is a request body

            // Authtoken Required: No

            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();
            String reqData = gson.toJson(request); // This is the JSON string we will send in the HTTP request body


            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody); // // Write the JSON data to the request body
            reqBody.close(); // Close the request body output stream, indicating that the request is complete


            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream resBody = http.getInputStream();
                String resData = readString(resBody);
                result = gson.fromJson(resData, RegisterResult.class);
            } else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                InputStream resBody = http.getErrorStream();
                String resData = readString(resBody);
                result = gson.fromJson(resData, RegisterResult.class);
            }


            // Return Result object
            return result;


        } catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            return null; // nothing to return
        }
    }


    // Login
    public LoginResult login(LoginRequest request) { // Method: POST

        // Initial Variable Declarations
        LoginResult result;

        try {
            URL url = new URL("http://" + getServerHost() + ":" + getServerPort() + "/user/login");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);    // There is a request body

            // Authtoken Required: No

            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();

            String reqData = gson.toJson(request); // This is the JSON string we will send in the HTTP request body


            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);  // Write the JSON data to the request body
            reqBody.close(); // Close the request body output stream, indicating that the request is complete


            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream resBody = http.getInputStream();
                String resData = readString(resBody);
                result = gson.fromJson(resData, LoginResult.class);
            } else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                InputStream resBody = http.getErrorStream();
                String resData = readString(resBody);
                result = gson.fromJson(resData, LoginResult.class);
            }


            // Return Result object
            return result;


        } catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            return null; // nothing to return
        }

    }

    // People
    public PersonResult getPeople(String authToken) { // Method: GET

        // Initial Variable Declarations
        PersonResult result;

        try {
            URL url = new URL("http://" + getServerHost() + ":" + getServerPort() + "/person");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false); // Indicate that this request will not contain an HTTP request body

            // Authtoken Required: Yes
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json"); // Specify that we would like to receive the server's response in JSON

            // Connect to the server and send the HTTP request
            http.connect();


            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream resBody = http.getInputStream();
                String resData = readString(resBody); // Extract JSON data from the HTTP response body
                result = gson.fromJson(resData,PersonResult.class);
            }
            else {
                // The HTTP response status code indicates an error
                InputStream resBody = http.getErrorStream();
                String resData = readString(resBody);
                result = gson.fromJson(resData,PersonResult.class);

            }

            // Return Result object
            return result;
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            return null;
        }

    }

    // Event
    public EventResult getEvents(String authToken) { // Method: GET

        // Initial Variable Declarations
        EventResult result;

        try {
            URL url = new URL("http://" + getServerHost() + ":" + getServerPort() + "/event");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false); // Indicate that this request will not contain an HTTP request body

            // Authtoken Required: Yes
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json"); // Specify that we would like to receive the server's response in JSON

            // Connect to the server and send the HTTP request
            http.connect();

            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream resBody = http.getInputStream();
                String resData = readString(resBody); // Extract JSON data from the HTTP response body
                result = gson.fromJson(resData,EventResult.class);
            }
            else {
                // The HTTP response status code indicates an error
                InputStream resBody = http.getErrorStream();
                String resData = readString(resBody);
                result = gson.fromJson(resData,EventResult.class);

            }

            // Return Result object
            return result;


        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            return null;
        }

    }


    // Helper methods


    private static String readString(InputStream is) throws IOException { // The readString method shows how to read a String from an InputStream.
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }


    private static void writeString(String str, OutputStream os) throws IOException { // The writeString method shows how to write a String to an OutputStream.
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}


    /*
     * login
     * register
     * getPeople
     * getEvents
     *
     *
     * // client never call these
     * clear
     * fill
     * getPerson
     * getEvent
     * load
     */

    /*
    LoginResult login(LoginRequest request) {}
    RegisterResult register(RegisterRequest request) {}
    GetPeopleResult getPeople(GetPeopleRequest request) {}
    GetEventResult getEvents(GetEventsRequest request) {}
     */


    /*
     Steps
     1. Serialize the request object as JSON
     2. Construct the HTTP request
     3. Send the request to the server, including the JSON in the body
     4. Receive back the HTTP response from the Server
     5. In that response, there would be a JSON result object in the body, and then the method would deserialize that JSON into a result and return it
     */
