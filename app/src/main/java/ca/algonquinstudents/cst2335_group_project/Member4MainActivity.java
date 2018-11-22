package ca.algonquinstudents.cst2335_group_project;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static ca.algonquinstudents.cst2335_group_project.OCDatabaseHelper.BUS_LINE;
import static ca.algonquinstudents.cst2335_group_project.OCDatabaseHelper.KEY_ID;
import static ca.algonquinstudents.cst2335_group_project.OCDatabaseHelper.STATION_NAME;
import static ca.algonquinstudents.cst2335_group_project.OCDatabaseHelper.STATION_NUMBER;
import static ca.algonquinstudents.cst2335_group_project.OCDatabaseHelper.TABLE_NAME;


public class Member4MainActivity extends AppCompatActivity {

    private ListView stationView;
    private EditText searchText;
    private Button myListBtn, searchBtn;
    private TextView listTitle;
    private ProgressBar progressBar;
    private ArrayList<String[]> statNameList = new ArrayList<>();

    private OCDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    protected static final String ACTIVITY_NAME = "Activity_Member4_main";

    private Cursor c;
    private StationAdapter messageAdapter;

    private boolean frameExists;
    private boolean isFirstClick = true;
    private boolean isMyList = true;

    private ToolbarMenu toolitem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member4_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarm4);
        setSupportActionBar(toolbar);

        toolitem = new ToolbarMenu(Member4MainActivity.this);

        RadioButton rBtn1 = findViewById(R.id.radioSearchStationNumber);
        RadioButton rBtn2 = findViewById(R.id.radioSearchStationName);
        RadioButton rBtn3 = findViewById(R.id.radioSearchBusNumber);

        searchText = (EditText)findViewById(R.id.SearchTextM4);

        if (!(rBtn1.isChecked()||rBtn2.isChecked()||rBtn3.isChecked())) {
            rBtn3.setChecked(true);
            searchText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        rBtn1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                searchText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        rBtn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                searchText.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });

        rBtn3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                searchText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        frameExists = (findViewById(R.id.frameLayout)!=null);

        myListBtn = findViewById(R.id.ShowMyListButtonM4);
        listTitle = findViewById(R.id.ListViewNameM4);
        stationView = findViewById(R.id.ListViewM4);
        searchBtn = findViewById(R.id.SearchButtonM4);
        progressBar = findViewById(R.id.ProgressBarM4);

        messageAdapter = new StationAdapter(this);
        stationView.setAdapter(messageAdapter);

        dbHelper = new OCDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        refreshMessageCursorAndListView();

        myListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMyList = true;
                refreshMessageCursorAndListView();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cVals = new ContentValues(  );
                String[] msg = searchText.getText().toString().split(";");
                cVals.put(STATION_NUMBER, msg[0]);
                cVals.put(BUS_LINE, msg[1]);
                cVals.put(STATION_NAME, msg[2]);
                db.insert(TABLE_NAME,"NullColumnName", cVals);
                searchText.setText("");
                isMyList = false;
                refreshMessageCursorAndListView();
            }
        });

        stationView.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> adpV, View v, int i, long l) {

                String[] msg = statNameList.get( i );
                long id = messageAdapter.getItemId(i);

                Bundle infoToPass = new Bundle();
                infoToPass.putString("StationNumber", msg[0]);
                infoToPass.putString("BusLine", msg[1]);
                infoToPass.putString("StationName", msg[2]);
                infoToPass.putLong("ID", id);
                infoToPass.putLong("Position", i);

                if(frameExists){
                    if (isFirstClick)
                        isFirstClick=false;
                    else
                        getFragmentManager().popBackStack();

                    M4MessageFragment newFragment = new M4MessageFragment();
                    newFragment.iAmTablet = true;

                    newFragment.setArguments( infoToPass ); //give information to bundle

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ftrans = fm.beginTransaction();
                    ftrans.replace(R.id.frameLayout, newFragment); //load a fragment into the framelayout
                    ftrans.addToBackStack("name doesn't matter"); //changes the back button behaviour
                    ftrans.commit(); //actually load it

                }
                else{
                    Intent intent = new Intent(Member4MainActivity.this, M4MessageDetails.class);
                    intent.putExtras(infoToPass); //send info
                    startActivityForResult(intent, 67);
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.getItem(3).setVisible(false);
        toolitem.setHelpTitle(getString(R.string.m4_help_title));
        toolitem.setHelpMessage(getString(R.string.m4_help_message));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = toolitem.onToolbarItemSelected(item);
        if( intent != null) {
            startActivity(intent);
            Member4MainActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy(){
        dbHelper.close();
        super.onDestroy();
    }

    private void refreshMessageCursorAndListView() {
        ArrayList<String[]> list = new ArrayList<>();
        if(isMyList) {
            c = db.rawQuery("SELECT * from " + TABLE_NAME + " where " + KEY_ID + " > ?", new String[]{"0"});
            Log.i(ACTIVITY_NAME, "Cursor's column count = " + c.getColumnCount());
            for (int i = 0; i < c.getColumnCount(); i++)
                Log.i(ACTIVITY_NAME, "Cursor's column name: " + c.getColumnName(i));

            String stationNumber, busline, stationName;
            c.moveToFirst();
            while (!c.isAfterLast()) {
                stationNumber = c.getString(c.getColumnIndex(STATION_NUMBER));
                busline = c.getString(c.getColumnIndex(BUS_LINE));
                stationName = c.getString(c.getColumnIndex(STATION_NAME));
                Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + stationNumber + "; " + busline + "; " + stationName);
                String[] viewRow = {stationNumber, busline, stationName};
                list.add(viewRow);
                c.moveToNext();
            }
            myListBtn.setVisibility(View.INVISIBLE);
            listTitle.setText(R.string.m4_lv_mylist);
        }
        else{
            myListBtn.setVisibility(View.VISIBLE);
            listTitle.setText(R.string.m4_lv_search_result);
        }
        statNameList = list;
        messageAdapter.notifyDataSetChanged();
    }

    public void deleteMessage(long id, String[] msg){
        db.delete(TABLE_NAME, KEY_ID+"=?", new String[]{Long.toString(id)});
        refreshMessageCursorAndListView();
    }

    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (requestCode == 67) {
            Log.i(ACTIVITY_NAME, "Returned to ChatWindow.onActivityResult");
        }

        String msgPassed = "ListID: ";
        String[] aMsg;
        long idPassed = 0;
        int duration;
        if (responseCode == Activity.RESULT_OK) {
            idPassed = data.getLongExtra("ID", -1);
            aMsg = data.getStringArrayExtra("Messages");
            msgPassed += idPassed + "; Station#: ";
            msgPassed += aMsg[0]+"; Bus: ";
            msgPassed += aMsg[1]+"; Station Name: ";
            msgPassed += aMsg[2];

            if (idPassed > 0) {
                deleteMessage(idPassed, aMsg);
                msgPassed += " removed from list.";
                duration = Toast.LENGTH_SHORT;
            }
            else {
                msgPassed += " can't be removed.";
                duration = Toast.LENGTH_SHORT;
            }
        }
        else{
            msgPassed = "Return back, Select new one.";
            duration = Toast.LENGTH_SHORT;
        }
        Toast toast = Toast.makeText(Member4MainActivity.this, msgPassed, duration);
        toast.show();
    }

    private class StationAdapter extends ArrayAdapter<String> {
        public StationAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return statNameList.size();
        }

        public String getItem(int position) {
            String[] items = statNameList.get(position);
            return items[0]+";"+items[1]+";"+items[2];
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = Member4MainActivity.this.getLayoutInflater();
            View result = null;
            result = inflater.inflate(R.layout.list_row_content_m4, null);
            TextView stationNumber = (TextView) result.findViewById(R.id.StationNumberM4);
            TextView busline = (TextView) result.findViewById(R.id.RouteNumberM4);
            TextView stationName = (TextView) result.findViewById(R.id.StationNameM4);
            String[] items = getItem(position).split(";");
            stationNumber.setText(items[0]);
            busline.setText(items[1]);
            stationName.setText(items[2]);
            return result;
        }

        public long getItemId(int position){
            c.moveToPosition(position);
            return c.getLong(c.getColumnIndex(KEY_ID));
        }
    }
}
