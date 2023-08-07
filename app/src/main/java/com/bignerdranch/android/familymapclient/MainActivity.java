package com.bignerdranch.android.familymapclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import Fragment.LoginFragment;
import Fragment.MapFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


// main activity initially wants to embed the first fragment (login)
public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {

    // Variable Declarations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // sets the content view to inflate the view tree

        // gets a pointer to the fragment manager
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentFrameLayout); // give a pointer to the fragment object that's inside the FrameLayout

        /*
         * Initially, there is no fragment inside the main layout, it's empty; so the fragment is going to come back null
         */

        if(fragment == null) {
            fragment = createLoginFragment(); // // create an instance of the first instance (login)
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentFrameLayout, fragment)
                    .commit();
        } else {
            // If the fragment is not null, the MainActivity was destroyed and recreated
            // so we need to reset the listener to the new instance of the fragment
            if(fragment instanceof LoginFragment) {
                ((LoginFragment) fragment).registerListener(this);
            }
        }
    }


    private Fragment createLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        /*
         * the login fragment can notify the main activity when the button gets clicked
         */
        fragment.registerListener(this); // attach the main activity to the fragment as a listener
        return fragment;
    }

    /*
     * Class 'MainActivity' must either be declared abstract or implement abstract method 'notifyDone()' in 'Listener'
     */
    // implements the listener interface defined by the login fragment
    @Override
    public void notifyDone() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentFrameLayout,fragment)
                .commit();

    }
}