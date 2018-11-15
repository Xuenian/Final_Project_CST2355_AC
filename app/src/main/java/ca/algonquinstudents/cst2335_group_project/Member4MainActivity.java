package ca.algonquinstudents.cst2335_group_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static ca.algonquinstudents.cst2335_group_project.OCDatabaseHelper.KEY_ID;
import static ca.algonquinstudents.cst2335_group_project.OCDatabaseHelper.KEY_MESSAGE;
import static ca.algonquinstudents.cst2335_group_project.OCDatabaseHelper.TABLE_NAME;


public class Member4MainActivity extends Activity {

    private ListView stationView;
    private EditText searchMsg;
    private Button searchBtn;
    private ProgressBar progressBar;
    private ArrayList<String> statNameList = new ArrayList<>();

    private OCDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    protected static final String ACTIVITY_NAME = "Activity_Member4_main";

    Cursor c;
    private ChatAdapter messageAdapter;

    private boolean frameExists;
    private boolean isFirstClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member4_main);

        Button Btn1 = findViewById(R.id.member4Btn1);
        Button Btn2 = findViewById(R.id.member4Btn2);
        Button Btn3 = findViewById(R.id.member4Btn3);

        Btn1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Member4MainActivity.this, Member1MainActivity.class);
                startActivity(intent);
                Member4MainActivity.this.finish();
            }
        });

        Btn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Member4MainActivity.this, Member2MainActivity.class);
                startActivity(intent);
                Member4MainActivity.this.finish();
            }
        });

        Btn3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Member4MainActivity.this, Member3MainActivity.class);
                startActivity(intent);
                Member4MainActivity.this.finish();
            }
        });

//        frameExists = (findViewById(R.id.frameLayout)!=null);

        stationView = findViewById(R.id.ListViewM4);
        searchMsg = findViewById(R.id.SearchStationM4);
        searchBtn = findViewById(R.id.SearchButtonM4);
        progressBar = findViewById(R.id.ProgressBarM4);

        messageAdapter = new ChatAdapter(this);
        stationView.setAdapter(messageAdapter);

        dbHelper = new OCDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        getMessageCursorFromDatabase();
        Log.i(ACTIVITY_NAME, "Cursor's column count = " + c.getColumnCount());
        for (int i = 0; i < c.getColumnCount(); i++)
            Log.i(ACTIVITY_NAME, "Cursor's column name: " + c.getColumnName(i));

        int colIndex = c.getColumnIndex(KEY_MESSAGE);
        c.moveToFirst();
        statNameList.clear();
        while (!c.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + c.getString(colIndex));
            statNameList.add(c.getString(colIndex));
            messageAdapter.notifyDataSetChanged();
            c.moveToNext();
        }

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cVals = new ContentValues(  );
                String msg = searchMsg.getText().toString();
                statNameList.add(msg);
                messageAdapter.notifyDataSetChanged();
                cVals.put(KEY_MESSAGE, msg);
                db.insert(TABLE_NAME,"NullColumnName", cVals);
                searchMsg.setText("");
                getMessageCursorFromDatabase();
            }
        });

        stationView.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> adpV, View v, int i, long l) {

                String msg = statNameList.get( i );
                long id = messageAdapter.getItemId(i);

                Bundle infoToPass = new Bundle();
                infoToPass.putString("Message", msg);
                infoToPass.putLong("ID", id);
                infoToPass.putLong("Position", i);
/*
                if(frameExists){
                    if (isFirstClick)
                        isFirstClick=false;
                    else
                        getFragmentManager().popBackStack();

                    MessageFragment newFragment = new MessageFragment();
                    newFragment.iAmTablet = true;

                    newFragment.setArguments( infoToPass ); //give information to bundle

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ftrans = fm.beginTransaction();
                    ftrans.replace(R.id.frameLayout, newFragment); //load a fragment into the framelayout
                    ftrans.addToBackStack("name doesn't matter"); //changes the back button behaviour
                    ftrans.commit(); //actually load it

                }
                else{
*/
                    Intent intent = new Intent(Member4MainActivity.this, MessageDetails.class);
                    intent.putExtras(infoToPass); //send info
                    startActivityForResult(intent, 67);
//                }

            }
        });
    }

    @Override
    protected void onDestroy(){
        dbHelper.close();
        super.onDestroy();
    }

    private void getMessageCursorFromDatabase() {
        c = db.rawQuery("SELECT * from " + TABLE_NAME + " where " + KEY_ID + " > ?", new String[]{"0"});
    }

    public void deleteMessage(long id, String msg){
        statNameList.remove(msg);
        messageAdapter.notifyDataSetChanged();
        db.delete(TABLE_NAME, KEY_ID+"=?", new String[]{Long.toString(id)});
        getMessageCursorFromDatabase();
    }

    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (requestCode == 67) {
            Log.i(ACTIVITY_NAME, "Returned to ChatWindow.onActivityResult");
        }

        String msgPassed = "Station:  ";
        long idPassed = 0;
        int duration;
        if (responseCode == Activity.RESULT_OK) {
            idPassed = data.getLongExtra("ID", -1);
            msgPassed += idPassed + "; ";
            msgPassed += data.getStringExtra("Message");

            if (idPassed > 0) {
                deleteMessage(idPassed, msgPassed);
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

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return statNameList.size();
        }

        public String getItem(int position) {
            return statNameList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = Member4MainActivity.this.getLayoutInflater();
            View result = null;
            result = inflater.inflate(R.layout.list_row_content_m4, null);
            TextView message = (TextView) result.findViewById(R.id.StationNameM4);
            message.setText(getItem(position));
            return result;
        }

        public long getItemId(int position){
            c.moveToPosition(position);
            return c.getLong(c.getColumnIndex(KEY_ID));
        }
    }
}
