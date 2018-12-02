package ca.algonquinstudents.cst2335_group_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class M4OCDataBaseHelper extends SQLiteOpenHelper {
    protected static String OC_DATABASE_NAME = "OCRouteStop.db";
    protected static int OC_VERSION_NUM = 1;
    protected static String OC_TABLE_NAME = "RouteStopTable";
    protected static String ML_TABLE_NAME = "UserBusStopList";

    public final static String KEY_ID = "List_id";
    public final static String ROUTE = "Route";
    public final static String STOP_CODE = "StopCode";
    public final static String STOP_NAME = "StopName";

    private Context ctx;

    private boolean createDb = false, upgradeDb = false;

    public M4OCDataBaseHelper(Context ctx){
        super(ctx, OC_DATABASE_NAME, null, OC_VERSION_NUM);
        this.ctx = ctx;
    }

    private void copyDatabaseFromAssets(SQLiteDatabase db) {
        Log.i("M4OCDataBaseHelper", "copy OC Database");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = ctx.getAssets().open(OC_DATABASE_NAME);
            outputStream = new FileOutputStream(db.getPath());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            SQLiteDatabase copiedDb = ctx.openOrCreateDatabase(OC_DATABASE_NAME, 0, null);
            copiedDb.execSQL("PRAGMA user_version = " + OC_VERSION_NUM);
            copiedDb.close();
        } catch (IOException e) {
            Log.i("M4OCDataBaseHelper", "OC Database load failure.");
            e.printStackTrace();
            throw new Error("M4OCDataBaseHelper" + " Error copying database");
        } finally {
            // Close the streams
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("M4OCDataBaseHelper" + " Error closing streams");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.i("M4OCDataBaseHelper", "onCreate db");
        createDb = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        Log.i("M4OCDataBaseHelper", "onUpgrade db "+oldVer+" to "+newVer);
        upgradeDb = true;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.i("M4OCDataBaseHelper", "onOpen db");
        if (createDb) {
            createDb = false;
            copyDatabaseFromAssets(db);

        }
        if (upgradeDb) {
            upgradeDb = false;
            copyDatabaseFromAssets(db);
        }
    }
}
