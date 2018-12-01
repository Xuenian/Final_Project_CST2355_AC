package ca.algonquinstudents.cst2335_group_project;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class M4MessageDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details_m4);

        Bundle infoToPass =  getIntent().getExtras();
        //repeat from tablet section:

        M4MessageFragment newFragment = new M4MessageFragment();
        newFragment.iAmTablet = false;
        newFragment.setArguments( infoToPass ); //give information to bundle

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ftrans = fm.beginTransaction();
        ftrans.replace(R.id.framelayoutstationm4, newFragment); //load a fragment into the framelayout
        ftrans.commit(); //actually load it
    }
}
