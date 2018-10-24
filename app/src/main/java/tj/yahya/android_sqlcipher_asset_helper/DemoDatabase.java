package tj.yahya.android_sqlcipher_asset_helper;

import android.content.Context;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by K_Hayoev on 24.10.2018.
 */

public class DemoDatabase extends SQLiteAssetHelper {

    private Context mContext;
    private static String DB_NAME = "demo.db";
    private static String DB_PASSWORD = "test123";
    private static Integer DB_VERSION = 1;

    SQLiteDatabase db;


    public DemoDatabase(Context context) {
        super(context, DB_NAME, DB_PASSWORD, DB_VERSION);
        db = getReadableDatabase();
    }


    public ArrayList getUsers() {
        ArrayList list = new ArrayList();
        Cursor cursor = db.rawQuery("Select * from users", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {

                list.add(cursor.getString(cursor.getColumnIndex("name")));
            } while (cursor.moveToNext());
        }
        return list;
    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return db.query(....)" so it'd be easy
    // to you to create adapters for your views.
}
