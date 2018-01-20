package ir.eynakgroup.diet.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shayan on 3/13/2017.
 */
public class AppPreferences {

    private SharedPreferences mPref;
    private static final String FIRST_TIME = "first";
    private static final String HAS_DIET = "already";
    private static final String DIET_NUMBER = "diet";
    private static final String DIET_POINTS = "Points";
    private static final String DIET_DATA = "diet_data";
    private static final String DIET_PREFIX = "P";

    public AppPreferences(Context context) {
        mPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    //    public static AppPreferences getInstance(Context context){
//        return new AppPreferences(context);
//    }
    public void setFirstTime(boolean status) {
        mPref.edit().putBoolean(FIRST_TIME, status).commit();
    }

    public boolean getFirstTime() {
        return mPref.getBoolean(FIRST_TIME, true);
    }


    public void setAlreadyDiet(boolean status) {
        mPref.edit().putBoolean(HAS_DIET, status).commit();
    }

    public boolean hasDiet() {
        return mPref.getBoolean(HAS_DIET, false);
    }

    public void setDietNumber(int number) {
        mPref.edit().putInt(DIET_NUMBER, number).commit();
    }

    public int getDietNumber() {
        return mPref.getInt(DIET_NUMBER, 0);
    }

    public int getTotalPoint() {
        return mPref.getInt(DIET_POINTS, 0);
    }

    public void setTotalPoints(int number) {
        mPref.edit().putInt(DIET_POINTS, number).commit();
    }

    public void setDietPoint(String diet_id, int number) {
        mPref.edit().putInt(diet_id, number).commit();
    }

    public int getDietPoint(String diet_id) {
        return mPref.getInt(diet_id, 0);
    }

    public void setDietData(String d) {
        mPref.edit().putString(DIET_DATA, d).commit();
    }

    public String getDietData() {
        return mPref.getString(DIET_DATA, null);
    }
}