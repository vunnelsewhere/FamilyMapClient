package Task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import Fragment.LoginFragment;
import Request.LoginRequest;
import Result.LoginResult;
import ServerProxy.ServerProxy;

public class LoginTask implements Runnable{

    // Variable Declarations
    private final Handler messageHandler;
    private LoginRequest request;
    private String serverHost;
    private String serverPort;

    private final ServerProxy proxy;

    private String firstName = "";
    private String lastName = "";



    public LoginTask(Handler messageHandler, LoginRequest request, String serverHost, String serverPort) {
        this.messageHandler = messageHandler;
        this.request = request;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        proxy = new ServerProxy();
        proxy.setServerHost(serverHost);
        proxy.setServerPort(serverPort);


    }

    @Override
    public void run() {
        LoginResult result = proxy.login(request);

        if(result.isSuccess()) {
            DataTask task = new DataTask(result.getAuthtoken(),serverHost,serverPort);
            task.setData(result.getPersonID());
            firstName = task.getFirstName();
            lastName = task.getLastName();

        }

        sendMessage(result);

    }

    // Send message (background thread to UI thread)
    private void sendMessage(LoginResult result) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        Boolean resultIsSuccess = result.isSuccess();

        if(resultIsSuccess) {
            messageBundle.putString(LoginFragment.FIRST_NAME, firstName);
            messageBundle.putString(LoginFragment.LAST_NAME,lastName);
        }
        messageBundle.putBoolean(LoginFragment.SUCCESS, result.isSuccess());

        message.setData(messageBundle);
        messageHandler.sendMessage(message);

    }
}