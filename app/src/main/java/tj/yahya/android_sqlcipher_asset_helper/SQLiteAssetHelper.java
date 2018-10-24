package tj.yahya.android_sqlcipher_asset_helper;

import android.content.Context;
import android.os.Build;

import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static net.sqlcipher.database.SQLiteDatabase.openDatabase;

/**
 * Created by K_Hayoev on 24.10.2018.
 */

public class SQLiteAssetHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLiteAssetHelper.class.getSimpleName();
    private static final String ASSET_DB_PATH = "databases";

    //The Android's default system path of your application database.
    private final String mDbPath;
    private final String mDbName;
    private final Integer mDbVersion;

    private SQLiteDatabase database;
    private final Context mContext;
    private final String mPassword;


    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public SQLiteAssetHelper(Context context, String name, String password, int version) {

        super(context, name, null, version);
        this.mContext = context;
        this.mPassword = password;
        this.mDbName = name;
        this.mDbVersion = version;


        if (Build.VERSION.SDK_INT > 17) {
            this.mDbPath = mContext.getApplicationInfo().dataDir + "/" + ASSET_DB_PATH + "/";
        } else {
            this.mDbPath = "data/data/" + mContext.getPackageName() + "/" + ASSET_DB_PATH + "/";
        }
    }


    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase(mPassword);

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            //String path = DB_PATH + DB_NAME;
            //checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

            SQLiteDatabase.loadLibs(mContext);
            checkDB = openDatabase(this.mDbPath + this.mDbName, mPassword, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }


    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream inputStream = mContext.getAssets().open("databases/" + this.mDbName);

        // Path to the just created empty db
        String outFileName = new StringBuilder(this.mDbPath).append(this.mDbName).toString();

        //Open the empty db as the output stream
        OutputStream outputStream = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        //Close the streams
        outputStream.flush();
        outputStream.close();
        inputStream.close();

    }


    public void openDataBase() throws SQLException {

        //Open the database

            database = openDatabase(this.mDbPath + this.mDbName, mPassword, null, SQLiteDatabase.OPEN_READWRITE);



    }


    public synchronized SQLiteDatabase getReadableDatabase() {
        if (database != null && database.isOpen()) {
            return database;  // The database is already open for business
        }else {
            try {
                createDataBase();
                openDataBase();
                return database;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
            super.close();
        }
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
