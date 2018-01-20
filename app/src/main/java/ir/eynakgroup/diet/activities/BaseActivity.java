package ir.eynakgroup.diet.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.j256.ormlite.stmt.UpdateBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.ronash.pushe.Pushe;
import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.database.DatabaseHelper;
import ir.eynakgroup.diet.database.tables.UserInfo;
import ir.eynakgroup.diet.network.ClientFactory;
import ir.eynakgroup.diet.network.RequestMethod;
import ir.eynakgroup.diet.network.response_models.CommonResponse;
import ir.eynakgroup.diet.network.response_models.DietListResponse;
import ir.eynakgroup.diet.network.response_models.UpdateResponse;
import ir.eynakgroup.diet.util.IabHelper;
import ir.eynakgroup.diet.util.IabResult;
import ir.eynakgroup.diet.util.Inventory;
import ir.eynakgroup.diet.utils.AppPreferences;
import ir.eynakgroup.diet.utils.JDF;
import ir.eynakgroup.diet.utils.view.CustomTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class BaseActivity extends AppCompatActivity {

    private DisplayMetrics mDisplayMetrics;
    private AppPreferences mAppPreferences;
    private RequestMethod mRequestMethod;
    private DatabaseHelper mDatabaseHelper;

    private FirebaseAnalytics mFirebaseAnalytics;

    // Debug tag, for logging
    static final String TAG = "";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 1008;

    private String base64EncodedPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwC7Qyfd4F3X3XROaY6SOSyD3zG4tDN58LtLRJ0Q5oQIaKG5ll6TuJTj1XbXEPpqS8/VdfXKL1QYPR560aMvlELh84k/Cy9G5KzpW1glJiPHkgOoSBvTOsnpnrz7WkHr3vfjATjm3hKyaDDGyPFIr7wb5i89BlEtNqwa0tOaKgI0LVZ7oLGPQ7nogMfGVJLBh320AsJwzsjpRVc17kRNi6b3UmYWB9VEJNcltrEG0kkCAwEAAQ==";

    // The helper object
    protected IabHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Pushe.initialize(this, true);
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

    protected void updateData() throws SQLException {
        final UserInfo user = getDBHelper().getUserDao().queryForAll().get(0);
        String fbToken = FirebaseInstanceId.getInstance().getToken();
        Call<UpdateResponse> call = getRequestMethod().onLogin(user.getSessionId(), user.getUserId(), user.getApiKey(), getResources().getString(R.string.app_version), getResources().getString(R.string.app_market), fbToken);

        call.enqueue(new Callback<UpdateResponse>() {
            @Override
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                System.out.println("api key updated----------------" + response.body());

                if (response.body().getError() == null) {
                    user.setApiKey(response.body().getApikey());
                    UpdateBuilder<UserInfo, Integer> updateBuilder = null;
                    try {
                        updateBuilder = getDBHelper().getUserDao().updateBuilder();
                        updateBuilder.updateColumnValue("Api_Key", response.body().getApikey());
                        updateBuilder.update();
                        user.setApiKey(response.body().getApikey());
                        getListRequest(user);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
//                    getListRequest(user);
                }
            }

            @Override
            public void onFailure(Call<UpdateResponse> call, Throwable t) {
                System.out.println("Failed------------");
                t.printStackTrace();
            }
        });
    }

    private void getListRequest(UserInfo user) {
        Call<List<DietListResponse>> getData = getRequestMethod().getDietList(user.getSessionId(), user.getUserId(), user.getApiKey());

        getData.enqueue(new Callback<List<DietListResponse>>() {
            @Override
            public void onResponse(Call<List<DietListResponse>> call, Response<List<DietListResponse>> response) {
                List<DietListResponse> list = response.body();
                JSONArray datas = new JSONArray();
                JsonObject tmp2;
                for (int i = 0; i < list.size(); i++) {
                    JsonObject tmp = list.get(i).getDietData();
                    System.out.println(tmp.toString());
                    for (int j = 0; j < tmp.size() - 1; j++) {
                        tmp2 = tmp.get(String.valueOf(j + 1)).getAsJsonObject();
                        tmp2.get("0").getAsJsonObject().remove("choices");
                        tmp2.get("1").getAsJsonObject().remove("choices");
                        tmp2.get("2").getAsJsonObject().remove("choices");
                        tmp2.get("3").getAsJsonObject().remove("choices");
                        tmp2.get("0").getAsJsonObject().remove("selected");
                        tmp2.get("1").getAsJsonObject().remove("selected");
                        tmp2.get("2").getAsJsonObject().remove("selected");
                        tmp2.get("3").getAsJsonObject().remove("selected");
                    }
                    tmp.addProperty("id", list.get(i).getId());
                    tmp.addProperty("point", list.get(i).getPoint());
                    tmp.addProperty("type", list.get(i).getType());
                    tmp.addProperty("startWeight", list.get(i).getStartWeight());
                    tmp.addProperty("goalWeight", list.get(i).getGoalWeight());
                    tmp.addProperty("startDate", parseJavascriptDate(list.get(i).getStartDate()));
                    datas.put(tmp);
                }
                getAppPreferences().setDietNumber(list.size());
                getAppPreferences().setDietData(datas.toString());
                System.out.println(datas.toString());

                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<List<DietListResponse>> call, Throwable t) {
                System.out.println("Failed------------!!!!!" + call.toString());
                t.printStackTrace();
            }
        });
    }

    public void sendDatatoServer(UserInfo user) {
        String email = "";
        int age = getAge(user.getBirthday());
        String deviceModel = getDeviceName();
        String diseases = user.getDisease();
        String version = getResources().getString(R.string.app_version);
        String market = getResources().getString(R.string.app_market);
        String firebasToken = FirebaseInstanceId.getInstance().getToken();;
        Call<CommonResponse> getData = getRequestMethod().editProfile(user.getSessionId(), user.getUserId(), user.getApiKey(),
                email, user.getGender(), jsonifyGeorgianDate(user.getBirthday()), user.getHeight(), user.getWeight(),
                age, user.getActivityLevel(), deviceModel, diseases, version,
                market, firebasToken);

        getData.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                System.out.println(response + ":<:<:<:<:<:<:<:<:<:<:<:<:<:<:<:<:<:<:<:<:<:<:<:");
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                System.out.println("Failed------------!:::" + call.toString());
                t.printStackTrace();
            }
        });
    }

    public static String jsonifyGeorgianDate(String date) {
        JSONObject dateJSON = new JSONObject();
        JDF jdf = new JDF();
        System.out.println(date + ",,,,,,,,,,,,,,,,,");
        String[] tmp = date.split("T");
        String[] dates = tmp[0].split("-");
        jdf.setIranianDate(Integer.parseInt(dates[0]),
                Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
        try {
            dateJSON.put("year", jdf.getGregorianYear());
            dateJSON.put("month", jdf.getGregorianMonth());
            dateJSON.put("day", jdf.getGregorianDay());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dateJSON.toString();
    }

    public int getAge(String bdate) {
        int birthYear = Integer.parseInt(bdate.split("-")[0]);
        JDF jdf = new JDF();
        int currentYear = jdf.getIranianYear();
        return currentYear - birthYear;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model.toUpperCase();
        }
        return manufacturer.toUpperCase() + " " + model;
    }

    private double parseJavascriptDate(String time) {
        if (!(time == null || time.matches("null") || time.matches(""))) {
            String date = time.split("T")[0];
            String yearMonthDay[] = date.split("-");
            int year = Integer.parseInt(yearMonthDay[0]);
            int month = Integer.parseInt(yearMonthDay[1]);
            int day = Integer.parseInt(yearMonthDay[2]);
            JDF j = new JDF(year, month, day);
            Calendar c = Calendar.getInstance();
            c.set(j.getGregorianYear(), j.getGregorianMonth(), j.getGregorianDay());
            Date d = c.getTime();
            return d.getTime();
        } else {
            return 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
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
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        return toast;
    }
}