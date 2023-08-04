package Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
// import android.widget.CompoundButton;
import android.widget.Toast;

import com.bignerdranch.android.familymapclient.R;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Request.LoginRequest;
import Request.RegisterRequest;
import Task.LoginTask;
import Task.RegisterTask;




/*
 * Use of fragment: the need to have a part of code to be used in different places
 * such that you can take that fragment and embed it inside of whatever activity needed (avoid code duplication)
 * Another reason: implementing dynamic user interfaces (pieces of user interface appear and disappear
 */

/*
 * Two files for a fragment: 1 for the layout, the java class would have all the event-handling logic for the fragment
 */

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment { // Beginning of class

    // Variable Declarations
    private Listener listener; // store pointer to the current listener
    private View view;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String SUCCESS = "success";

    // Declare: Editable text fields, radio buttons, login/register button
    private EditText serverHost;
    private EditText serverPort;
    private EditText userName;
    private EditText passWord;
    private EditText firstName;
    private EditText lastName;
    private EditText email;

    private RadioGroup gender;

    // private RadioButton maleButton;
    // private RadioButton femaleButton;

    private Button signinButton;
    private Button registerButton;

    private String definedUserGender = null;

    public LoginFragment() {
        // Required empty public constructor
    }


    public interface Listener {
        void notifyDone(); // it's going to notify the main activity when the Done button gets clicked
    }

    public void registerListener(Listener listener) {
        this.listener = listener;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_login,container,false);

        // locate specific view for different parameters
        serverHost = view.findViewById(R.id.serverHostField);
        serverPort = view.findViewById(R.id.serverPort);
        userName = view.findViewById(R.id.userName);
        passWord = view.findViewById(R.id.password);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.emailAddress);

        //maleButton = view.findViewById(R.id.male);
        //femaleButton = view.findViewById(R.id.female);
        gender = view.findViewById(R.id.gender);

        signinButton = view.findViewById(R.id.signinButton);
        registerButton = view.findViewById(R.id.registerButton);

        // have the buttons enabled dynamically
        signinButton.setEnabled(false);
        registerButton.setEnabled(false);

        // Add listeners to editText fields
        addListenerForTextFields(serverHost);
        addListenerForTextFields(serverPort);
        addListenerForTextFields(userName);
        addListenerForTextFields(passWord);
        addListenerForTextFields(firstName);
        addListenerForTextFields(lastName);
        addListenerForTextFields(email);

        // Add listeners to radio buttons
        //addListenerForRadioButtons(maleButton);
        //addListenerForRadioButtons(femaleButton);

        // Add listeners to radio groups
        addListenerForRadioGroups(gender);

        // SetOnClickListener for Login
        addListenerForLogin();

        // SetOnClickListener for Register
        addListenerForRegister();


        // Wire up the views
        return view;
    }

    private void addListenerForTextFields(EditText userInput) {
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { // differentiate whether textfields are filled in for login or register
                // check fields with empty value
                validateLogin();
                // validateRegister(); // maybe no need here(?)


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }



    private void addListenerForRadioGroups(RadioGroup gender) {
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int idOfRadioButtons) {
                // find the radiobutton that corresponds to the id and gets its text
                RadioButton correspondButton = view.findViewById(idOfRadioButtons);
                definedUserGender = correspondButton.getText().toString();

                if(definedUserGender.equalsIgnoreCase("Male")) {
                    definedUserGender = "m";
                } else {
                    definedUserGender = "f";
                }

                validateRegister(); // this has to be inside of onCheckedChanged to enable button
            }
        });



    }





/* At first doesn't work, now find out that it is just the matter of the placement of validateRegister() in the code


    private void addListenerForRadioButtons(RadioButton button) {


        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    definedUserGender = button.getText().toString();
                    if (definedUserGender.equalsIgnoreCase("Male")) {
                        definedUserGender = "male";
                    } else {
                        definedUserGender = "female";
                    }
                    validateRegister();
                }
            }
        });



    }

 */


    private void validateLogin() {
        String serverHostText = serverHost.getText().toString();
        String serverPortText = serverPort.getText().toString();
        String userNameText = userName.getText().toString();
        String passWordText = passWord.getText().toString();

        if(serverHostText.equals("") || serverPortText.equals("") ||
                userNameText.equals("") || passWordText.equals("")) {
            signinButton.setEnabled(false);
        } else {
            signinButton.setEnabled(true);
        }
    }

    private void validateRegister() {
        String serverHostText = serverHost.getText().toString();
        String serverPortText = serverPort.getText().toString();
        String userNameText = userName.getText().toString();
        String passWordText = passWord.getText().toString();
        String firstNameText = firstName.getText().toString();
        String lastNameText = lastName.getText().toString();
        String emailText = email.getText().toString();

        if(serverHostText.equals("") || serverPortText.equals("") || userNameText.equals("") ||
                passWordText.equals("") || firstNameText.equals("") || lastNameText.equals("") ||
                emailText.equals("") || definedUserGender == null)  {
            registerButton.setEnabled(false);
        } else {
            registerButton.setEnabled(true);
        }


    }

    private void addListenerForLogin() { // Beginning of listener for login
        signinButton.setOnClickListener(v -> {
          LoginRequest realrequest = new LoginRequest(
                  userName.getText().toString(),
                  passWord.getText().toString());

          // Set up a handler that will process messages from the task and make updates on the UI thread
            // This `Handler` class should be static or leaks might occur (anonymous android.os.Handler)
            @SuppressLint("HandlerLeak")Handler uiThreadMessageHandler = new Handler() { // Beginning of Handler
              @Override
              public void handleMessage(@NonNull Message msg) { // Beginning of handleMessage
                  Bundle bundle = msg.getData();
                  Boolean isLoginSuccess = bundle.getBoolean(SUCCESS);


                  if(isLoginSuccess) {
                      Toast.makeText(view.getContext(),bundle.getString(FIRST_NAME)
                      + " " + bundle.getString(LAST_NAME),Toast.LENGTH_SHORT).show();
                      listener.notifyDone();
                  } else {
                      Toast.makeText(view.getContext(),"Sign-in Failed",Toast.LENGTH_SHORT).show();
                  }
              } // End of handleMessage
          }; // End of Handler


            // Connect to Login Background task
            String serverHostText = serverHost.getText().toString();
            String serverPortText = serverPort.getText().toString();
            LoginTask task = new LoginTask(uiThreadMessageHandler,realrequest,
                    serverHostText,serverPortText);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(task); // submit the login background task

        });

    } // End of listener for login

    private void addListenerForRegister() { // Beginning of listener for register
        registerButton.setOnClickListener(v -> {
            RegisterRequest realrequest = new RegisterRequest(
                    userName.getText().toString(),
                    passWord.getText().toString(),
                    email.getText().toString(),
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    definedUserGender);

            // Set up a handler that will process messages from the task and make updates on the UI thread
            // This `Handler` class should be static or leaks might occur (anonymous android.os.Handler)
            @SuppressLint("HandlerLeak") Handler uiThreadMessageHandler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    Bundle bundle = msg.getData();
                    Boolean isRegisterSuccess = bundle.getBoolean(SUCCESS);




                    if(isRegisterSuccess) {
                        Toast.makeText(view.getContext(),bundle.getString(FIRST_NAME)
                                + " " + bundle.getString(LAST_NAME),Toast.LENGTH_SHORT).show();
                        listener.notifyDone();
                    } else {
                        Toast.makeText(view.getContext(),"Register Failed",Toast.LENGTH_SHORT).show();
                    }




                } // End of handleMessage
            }; // End of Handler

            // Connect to Register Background task
            String serverHostText = serverHost.getText().toString();
            String serverPortText = serverPort.getText().toString();
            RegisterTask task = new RegisterTask(uiThreadMessageHandler,realrequest,
                    serverHostText,serverPortText);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(task); // submit the register background task

        });

    } // End of listener for register






} // End of Class


/*
 * OnCreate method is actually divided into two parts in a fragment
 */

/*
 * Background task
 * 1. Write a class that implements the Runnable interface
 * You code goes in the public void run() method
 * Preferred when you don't need to return a result
 */