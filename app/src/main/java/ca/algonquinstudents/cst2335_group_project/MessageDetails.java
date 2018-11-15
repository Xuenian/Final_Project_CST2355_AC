package ca.algonquinstudents.cst2335_group_project;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MessageDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Bundle infoToPass =  getIntent().getExtras();
        //repeat from tablet section:

        MessageFragment newFragment = new MessageFragment();
        newFragment.iAmTablet = false;
        newFragment.setArguments( infoToPass ); //give information to bundle

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ftrans = fm.beginTransaction();
        ftrans.replace(R.id.framelayoutstationm4, newFragment); //load a fragment into the framelayout
//        ftrans.addToBackStack("name doesn't matter"); //changes the back button behaviour
        ftrans.commit(); //actually load it

    }
}
