package ir.eynakgroup.diet.utils;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by Shayan on 3/13/2017.
 */
public class AppPreferences {

    private SharedPreferences mPref;
    public AppPreferences(Context context){
        mPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }
//    public static AppPreferences getInstance(Context context){
//        return new AppPreferences(context);
//    }
    public void setFirstTime(boolean status){
        mPref.edit().putBoolean("first", status).commit();
    }
    public boolean getFirstTime(){
        return mPref.getBoolean("first", true);
    }

}