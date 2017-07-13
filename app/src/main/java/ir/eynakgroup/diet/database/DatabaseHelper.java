package ir.eynakgroup.diet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import ir.eynakgroup.diet.BuildConfig;
import ir.eynakgroup.diet.database.tables.Diet;
import ir.eynakgroup.diet.database.tables.Dish;
import ir.eynakgroup.diet.database.tables.Exercise;
import ir.eynakgroup.diet.database.tables.Food;
import ir.eynakgroup.diet.database.tables.FoodUnit;
import ir.eynakgroup.diet.database.tables.HatedFood;
import ir.eynakgroup.diet.database.tables.History;
import ir.eynakgroup.diet.database.tables.UserInfo;
import ir.eynakgroup.diet.utils.AppPreferences;

/**
 * Created by Shayan on 5/15/2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "diet.db";
    private static final String DATABASE_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/";

    public DatabaseHelper(Context context) {
        this(context, DATABASE_NAME, null, BuildConfig.DATABASE_VERSION);
    }

    private DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
        if(new AppPreferences(context).getFirstTime() || !existDB())
            copyDB(context);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
//        try {
//            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
//            TableUtils.createTableIfNotExists(connectionSource, UserInfo.class);
//            TableUtils.createTableIfNotExists(connectionSource, Exercise.class);
//            TableUtils.createTableIfNotExists(connectionSource, History.class);
//
//        } catch (SQLException e) {
//            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    private void copyDB(Context context) {
        System.out.println("coping pre-made database...");
        try {
            File dir = new File(DATABASE_PATH);
            if(dir.exists())
                dir.delete();
            dir.mkdirs();
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
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

    private boolean existDB(){
        return new File(DATABASE_PATH + DATABASE_NAME).exists();
    }

    private Dao<Dish, Integer> dishDao;
    public Dao<Dish, Integer> getDishDao() throws SQLException {
        if (dishDao == null) {
            dishDao = getDao(Dish.class);
        }
        return dishDao;
    }

    private Dao<Food, Integer> foodDao;
    public Dao<Food, Integer> getFoodDao() throws SQLException {
        if (foodDao == null) {
            foodDao = getDao(Food.class);
        }
        return foodDao;
    }

    private Dao<Diet, Integer> dietDao;
    public Dao<Diet, Integer> getDietDao() throws SQLException {
        if (dietDao == null) {
            dietDao = getDao(Diet.class);
        }
        return dietDao;
    }

    private Dao<Exercise, Integer> exerciseDao;
    public Dao<Exercise, Integer> getExerciseDao() throws SQLException {
        if (exerciseDao == null) {
            exerciseDao = getDao(Exercise.class);
        }
        return exerciseDao;
    }

    private Dao<FoodUnit, Integer> unitDao;
    public Dao<FoodUnit, Integer> getFoodUnitDao() throws SQLException {
        if (unitDao == null) {
            unitDao = getDao(FoodUnit.class);
        }
        return unitDao;
    }

    private Dao<HatedFood, Integer> hatedDao;
    public Dao<HatedFood, Integer> getHatedFoodDao() throws SQLException {
        if (hatedDao == null) {
            hatedDao = getDao(HatedFood.class);
        }
        return hatedDao;
    }

    private Dao<History, Integer> historyDao;
    public Dao<History, Integer> getHistoryDao() throws SQLException {
        if (historyDao == null) {
            historyDao = getDao(History.class);
        }
        return historyDao;
    }

    private Dao<UserInfo, Integer> userDao;
    public Dao<UserInfo, Integer> getUserDao() throws SQLException {
        if (userDao == null) {
            userDao = getDao(UserInfo.class);
        }
        return userDao;
    }
}
