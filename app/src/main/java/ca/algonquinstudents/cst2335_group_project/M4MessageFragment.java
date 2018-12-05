package ca.algonquinstudents.cst2335_group_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class M4MessageFragment extends Fragment {

    private Member4MainActivity parent = null;
    private View screen;
    private ProgressBar pBar;
    private String[] msgs;
    private long idPassed;

    public boolean iAmTablet;

    public M4MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle infoToPass = getArguments(); //returns the arguments set before

        msgs = new String[]{infoToPass.getString("StationNumber"), infoToPass.getString("BusLine"), infoToPass.getString("StationName")};
        idPassed = infoToPass.getLong("ID");

        screen = inflater.inflate(R.layout.activity_message_details_m4, container, false);
        TextView msgTV = screen.findViewById(R.id.textViewStationDetails01M4);

        String passedMessage = "Station#: "+msgs[0]+"  Station Name: "+msgs[2]+"  Bus: "+msgs[1]+"  (ID = "+idPassed+")";
        msgTV.setText(passedMessage);

        pBar = (ProgressBar) screen.findViewById(R.id.ProgressBarM4);

        Button btnRemove = (Button)screen.findViewById(R.id.member4Btn1);
        Button btnAdd = (Button)screen.findViewById(R.id.member4Btn2);
        Button btnRefresh = (Button)screen.findViewById(R.id.member4Btn3);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OCBusQuery fQuery = new OCBusQuery();
                fQuery.execute("https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="+msgs[0]+"&routeNo="+msgs[1]);
            }
        });

        btnRefresh.callOnClick();

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iAmTablet) {
                    parent.deleteMessage(idPassed, msgs); //call function from parent
                    getActivity().getFragmentManager().popBackStack();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.m4_dialog_message);
                    builder.setTitle(R.string.m4_dialog_title);
                    builder.setPositiveButton(R.string.positive_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("ID", idPassed);
                            resultIntent.putExtra("Messages", msgs);
                            getActivity().setResult(Activity.RESULT_OK, resultIntent);
                            getActivity().finish();// go to previous activity
                        }
                    });
                    builder.setNegativeButton(R.string.negative_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent resultIntent = new Intent();
                            getActivity().setResult(Activity.RESULT_CANCELED, resultIntent);
                            getActivity().finish();// go to previous activity
                        }
                    });
                    builder.show();
                }
            }
        });

        Snackbar sb = Snackbar.make(screen, "Delete the Station and return to the list", Snackbar.LENGTH_LONG);
        sb.show();

        return screen;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        if(iAmTablet)
            parent = (Member4MainActivity)  context; //find out which activity has the fragment
    }

    private class OCBusQuery extends AsyncTask<String, Integer, String[]> {

        private Bitmap picWeather;

        @Override
        protected String[] doInBackground(String... urls) {

            String[] aVs = new String[20];

            int iTrip = -6, iDir = -6;
            boolean isRoute = false;
            String tagName;

            publishProgress(0);
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xPP = factory.newPullParser();
                xPP.setInput(response, "UTF-8");

                publishProgress(25);
                while (xPP.getEventType() != XmlPullParser.END_DOCUMENT) {
                    switch (xPP.getEventType()) {
                        case XmlPullParser.START_TAG:
                            tagName = xPP.getName();
                            if (tagName.equals("StopNo")) {
                                aVs[0] = xPP.nextText();
                                isRoute = aVs[0].equals(msgs[0]);
                                if(isRoute)
                                    iDir = -1;
                            }
                            if (tagName.equals("StopLabel"))
                                aVs[1] = xPP.nextText();
                            if (tagName.equals("RouteDirection"))
                                iDir++;
                            if (tagName.equals("RouteNo")) {
                                isRoute &= xPP.nextText().equals(msgs[1]);
                                if(isRoute && iDir>=0)
                                    iTrip = 0;
                                publishProgress((iDir+2)*25);
                            }
                            if(isRoute){
                                if (tagName.equals("RouteLabel"))
                                    aVs[iDir+2] = xPP.nextText();
                                if (tagName.equals("Direction"))
                                    aVs[iDir+4] = xPP.nextText();
                                if (tagName.equals("RequestProcessingTime"))
                                    aVs[iDir+6] = xPP.nextText();
                                if (tagName.equals("TripDestination"))
                                    aVs[iDir*3+iTrip+8] = xPP.nextText();
                                if (tagName.equals("TripStartTime")&&(iTrip<3)){
                                    aVs[iDir*3+iTrip+14] = xPP.nextText();
                                    iTrip++;
                                }
                            }
                            Log.i("read XML tag:", tagName);
                            break;
                    }
                    xPP.next();
                }
            } catch (Exception e) {
                Log.i("Exception", e.getMessage());
            }
            publishProgress(100);
            return aVs;
        }

        @Override
        protected void onProgressUpdate(Integer... args) {
            pBar.setVisibility(View.VISIBLE);
            pBar.setProgress(args[0]);
            Log.i("Progress:", args[0].toString());
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);

            TextView QDetails01 = screen.findViewById(R.id.textViewStationDetails02M4);
            TextView QDetails02 = screen.findViewById(R.id.textViewStationDetails03M4);
            TextView QDetails03 = screen.findViewById(R.id.textViewStationDetails04M4);
            TextView QDetails04 = screen.findViewById(R.id.textViewStationDetails05M4);
            TextView QDetails05 = screen.findViewById(R.id.textViewStationDetails06M4);
            TextView QDetails06 = screen.findViewById(R.id.textViewStationDetails07M4);
            TextView QDetails07 = screen.findViewById(R.id.textViewStationDetails08M4);
            TextView QDetails08 = screen.findViewById(R.id.textViewStationDetails09M4);
            TextView QDetails09 = screen.findViewById(R.id.textViewStationDetails10M4);
            TextView QDetails10 = screen.findViewById(R.id.textViewStationDetails11M4);
            TextView QDetails11 = screen.findViewById(R.id.textViewStationDetails12M4);
            TextView QDetails12 = screen.findViewById(R.id.textViewStationDetails13M4);
            TextView QDetails13 = screen.findViewById(R.id.textViewStationDetails14M4);

            QDetails01.setText("StopNo: "+s[0]+"    StopLabel: "+s[1]);
            QDetails02.setText("RouteNo: "+msgs[1]+"    RouteLabel: "+s[2]);
            QDetails03.setText("Direction: "+s[4]);
            QDetails04.setText("RequestProcessingTime: "+s[6]);
            QDetails05.setText("Trip1 StartTime: "+s[14]+"    Destination: "+s[8]);
            QDetails06.setText("Trip2 StartTime: "+s[15]+"    Destination: "+s[9]);
            QDetails07.setText("Trip3 StartTime: "+s[16]+"    Destination: "+s[10]);
            QDetails08.setText("RouteNo: "+msgs[1]+"    RouteLabel: "+s[3]);
            QDetails09.setText("Direction: "+s[5]);
            QDetails10.setText("RequestProcessingTime: "+s[7]);
            QDetails11.setText("Trip1 StartTime: "+s[17]+"    Destination: "+s[11]);
            QDetails12.setText("Trip2 StartTime: "+s[18]+"    Destination: "+s[12]);
            QDetails13.setText("Trip3 StartTime: "+s[19]+"    Destination: "+s[13]);

            pBar.setVisibility(View.INVISIBLE);
        }
    }
}
