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

    public AppPreferences(Context context){
        mPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }
//    public static AppPreferences getInstance(Context context){
//        return new AppPreferences(context);
//    }
    public void setFirstTime(boolean status){
        mPref.edit().putBoolean(FIRST_TIME, status).commit();
    }
    public boolean getFirstTime(){
        return mPref.getBoolean(FIRST_TIME, true);
    }


    public void setHasDiet(boolean status){
        mPref.edit().putBoolean(HAS_DIET, status).commit();
    }
    public boolean getHasDiet(){
        return mPref.getBoolean(HAS_DIET, false);
    }


    public void setGoalWeight(float weight){
        mPref.edit().putFloat("goal", weight).commit();
    }
    public float getGoalWeight(){
        return mPref.getFloat("goal", 0.0f);
    }


}