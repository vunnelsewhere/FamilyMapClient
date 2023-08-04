package Task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.RegisterResult;
import ServerProxy.ServerProxy;

public class RegisterTask implements Runnable{

    // Variable Declarations
    private final Handler messageHandler;
    private RegisterRequest request;
    private String serverHost;
    private String serverPort;

    private final ServerProxy proxy;

    private String firstName;
    private String lastName;
    private boolean isSuccess;

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String SUCCESS = "success";

    public RegisterTask(Handler messageHandler, RegisterRequest request, String serverHost, String serverPort) {
        this.messageHandler = messageHandler;
        this.request = request;
        proxy = new ServerProxy();
        proxy.setServerHost(serverHost);
        proxy.setServerPort(serverPort);
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        RegisterResult result = proxy.register(request);

        if(result.isSuccess()) {
            DataTask task = new DataTask(result.getAuthtoken(),serverHost,serverPort);
            task.setData(result.getPersonID());
            firstName = task.getFirstName();
            lastName = task.getLastName();
            isSuccess = true;
        } else {
            isSuccess = false;
        }

        sendMessage();
    }


    // Send message (background thread to UI thread)
    private void sendMessage() {
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();

        if(isSuccess) {
            messageBundle.putString(FIRST_NAME, firstName);
            messageBundle.putString(LAST_NAME,lastName);
        }
        messageBundle.putBoolean(SUCCESS, isSuccess);

        message.setData(messageBundle);
        messageHandler.sendMessage(message);

    }


}
