package ca.algonquinstudents.cst2335_group_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OCDatabaseHelper extends SQLiteOpenHelper {
    protected static String DATABASE_NAME = "OCRoute.db";
    protected static int VERSION_NUM = 2;
    protected static String TABLE_NAME = "UserStationList_Table";

    public final static String KEY_ID = "List_id";
    public final static String STATION_NUMBER = "StationNumber";
    public final static String BUS_LINE = "BusLine";
    public final static String STATION_NAME = "StationName";

    public OCDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " +TABLE_NAME+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+STATION_NUMBER+" text, "+BUS_LINE+" text, "+STATION_NAME+" text);");
        Log.i("OCDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        Log.i("OCDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
    }
}