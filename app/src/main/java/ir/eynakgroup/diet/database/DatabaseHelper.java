package ir.eynakgroup.diet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ir.eynakgroup.diet.BuildConfig;

/**
 * Created by Shayan on 5/15/2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "db_diet.db";
    private static final String DATABASE_PATH = "/data/data/"+ BuildConfig.APPLICATION_ID+ "/databases/";

    public DatabaseHelper(Context context) {
        this(context, DATABASE_PATH+DATABASE_NAME, null, BuildConfig.DATABASE_VERSION);
    }

    private DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
        copyDB(context);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    private void copyDB(Context context){
        System.out.println(existDB()+"---------------------");
        if (!existDB()) {
            try {
                File dir = new File(DATABASE_PATH);
                dir.mkdirs();
                InputStream inputStream = context.getAssets().open("database/"+DATABASE_NAME);
                String dbFileName = DATABASE_PATH + DATABASE_NAME;
                Log.i(DatabaseHelper.class.getName(), "DB Path : " + dbFileName);

                OutputStream outputStream = new FileOutputStream(dbFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean existDB() {
        String dbPath = DATABASE_PATH + DATABASE_NAME;
        return new File(dbPath).exists();
    }


}
