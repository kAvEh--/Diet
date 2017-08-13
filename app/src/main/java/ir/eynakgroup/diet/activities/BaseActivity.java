package ir.eynakgroup.diet.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.database.DatabaseHelper;
import ir.eynakgroup.diet.network.ClientFactory;
import ir.eynakgroup.diet.network.RequestMethod;
import ir.eynakgroup.diet.util.IabHelper;
import ir.eynakgroup.diet.util.IabResult;
import ir.eynakgroup.diet.util.Inventory;
import ir.eynakgroup.diet.util.Purchase;
import ir.eynakgroup.diet.utils.AppPreferences;
import ir.eynakgroup.diet.utils.view.CustomTextView;

class BaseActivity extends AppCompatActivity {

    private DisplayMetrics mDisplayMetrics;
    private AppPreferences mAppPreferences;
    private RequestMethod mRequestMethod;
    private DatabaseHelper mDatabaseHelper;

    // Debug tag, for logging
    static final String TAG = "";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 1008;

    private String base64EncodedPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwC7Qyfd4F3X3XROaY6SOSyD3zG4tDN58LtLRJ0Q5oQIaKG5ll6TuJTj1XbXEPpqS8/VdfXKL1QYPR560aMvlELh84k/Cy9G5KzpW1glJiPHkgOoSBvTOsnpnrz7WkHr3vfjATjm3hKyaDDGyPFIr7wb5i89BlEtNqwa0tOaKgI0LVZ7oLGPQ7nogMfGVJLBh320AsJwzsjpRVc17kRNi6b3UmYWB9VEJNcltrEG0kkCAwEAAQ==";

    // The helper object
    IabHelper mHelper;

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

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                }
                // Hooray, IAB is fully set up!
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                Log.d(TAG, "Failed to query inventory: " + result);
                return;
            } else {
                Log.d(TAG, "Query inventory was successful.");
            }

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
        if (mDatabaseHelper != null) {
//            OpenHelperManager.releaseHelper();
            mDatabaseHelper.close();
            mDatabaseHelper = null;
        }

        if (mDisplayMetrics != null)
            mDisplayMetrics = null;

        if (mAppPreferences != null)
            mAppPreferences = null;

        if (mRequestMethod != null)
            mRequestMethod = null;

    }

    protected DisplayMetrics getDisplayMetrics() {
        if (mDisplayMetrics == null)
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

    protected AppPreferences getAppPreferences() {
        if (mAppPreferences == null)
            mAppPreferences = new AppPreferences(this);

        return mAppPreferences;
    }

    protected RequestMethod getRequestMethod() {
        if (mRequestMethod == null)
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