package ir.eynakgroup.diet.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.database.DatabaseHelper;
import ir.eynakgroup.diet.network.ClientFactory;
import ir.eynakgroup.diet.network.RequestMethod;
import ir.eynakgroup.diet.utils.AppPreferences;
import ir.eynakgroup.diet.utils.view.CustomTextView;

class BaseActivity extends AppCompatActivity {

    private DisplayMetrics mDisplayMetrics;
    private AppPreferences mAppPreferences;
    private RequestMethod mRequestMethod;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Window window = getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            window.setStatusBarColor(getResources().getColor(R.color.colorWhite, this.getTheme()));
//
//        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            window.setStatusBarColor(getResources().getColor(R.color.colorWhite));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabaseHelper != null) {
//            OpenHelperManager.releaseHelper();
            mDatabaseHelper.close();
            mDatabaseHelper = null;
        }

        if(mDisplayMetrics != null)
            mDisplayMetrics = null;

        if(mAppPreferences != null)
            mAppPreferences = null;

        if(mRequestMethod != null)
            mRequestMethod = null;

    }

    protected DisplayMetrics getDisplayMetrics(){
        if(mDisplayMetrics == null)
            mDisplayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics;
    }


    protected DatabaseHelper getDBHelper() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(this);

        }
        return mDatabaseHelper;
    }

    protected AppPreferences getAppPreferences(){
        if(mAppPreferences == null)
            mAppPreferences = new AppPreferences(this);

        return mAppPreferences;
    }

    protected RequestMethod getRequestMethod(){
        if(mRequestMethod == null)
            mRequestMethod = ClientFactory.getRetrofitInstance().create(RequestMethod.class);

        return mRequestMethod;
    }

    protected Toast getToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_custom,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        CustomTextView text = (CustomTextView) layout.findViewById(R.id.text);
        text.setText(message);
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        return toast;
    }


}
