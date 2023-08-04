package servertest;

// From other package
import ServerProxy.ServerProxy;
import Request.*;
import Result.*;

// From JUnit test
import org.junit.Before;
import org.junit.Test;

// From library

import static org.junit.Assert.*;


// From library


public class ServerProxyTest {

    // Variable Declaration
    private ServerProxy proxy;
    private RegisterRequest goodrequest = new RegisterRequest("venuschan","ireallydon'tknow","myemail@gmail.com",
            "Venus","Chan","f");
    private RegisterRequest repeatRequest = new RegisterRequest("tomlee","idon'tknowagain","hisemail@gmail.com",
            "Tom","Lee","m");
    private RegisterRequest goodRequest2 = new RegisterRequest("sallynelson","sleepnow","sally@gmail.com",
            "Sally","Nelson","f");

    private LoginRequest forGoodRequest2 = new LoginRequest("sallynelson","sleepnow");



    @Before
    public void setUp() {
        proxy = new ServerProxy();
        String serverHost = "localhost";
        String serverPort = "8080";
        proxy.setServerHost(serverHost);
        proxy.setServerPort(serverPort);
    }

    @Test
    public void RegisterPass() {
        System.out.println("Register Pass Test");
        RegisterResult goodResult = proxy.register(goodrequest);
        assertNotNull(goodResult);

        assertEquals("venuschan",goodResult.getUsername());
        assertEquals(true, goodResult.isSuccess());
    }

    @Test
    public void RegisterFail() {
        System.out.println("Register Fail Test");
        System.out.println("Test 1: already has this user");
        proxy.register(repeatRequest);
        RegisterResult badResult2 = proxy.register(repeatRequest);
        assertNotNull(badResult2);
        assertEquals("Error: username already exist",badResult2.getMessage());
        assertEquals(false, badResult2.isSuccess());
        assertNull(badResult2.getUsername());

    }

    @Test
    public void LoginPass() {
        System.out.println("Login Pass Test");
        proxy.register(goodRequest2);
        LoginResult goodResult = proxy.login(forGoodRequest2);

        assertNotNull(goodResult);
        assertEquals("sallynelson",goodResult.getUsername());
        assertEquals(true,goodResult.isSuccess());
        assertNotNull(goodResult.getPersonID());


    }

    @Test
    public void LoginFail() {
        System.out.println("Login Fail Test");

        System.out.println("Test 1: user does not exist in database");
        LoginRequest request2 = new LoginRequest("bear","345");
        LoginResult badResult = proxy.login(request2);

        assertNotNull(badResult);
        assertEquals("Error: User does not exist",badResult.getMessage());
        assertEquals(false,badResult.isSuccess());
        assertNull(badResult.getPersonID());
        assertNull(badResult.getAuthtoken());

    }

    @Test
    public void GetPeoplePass() {

    }

    @Test
    public void GetPeopleFail() {

    }

    @Test
    public void GetEventsPass() {

    }

    @Test
    public void GetEventsFail() {

    }
}
