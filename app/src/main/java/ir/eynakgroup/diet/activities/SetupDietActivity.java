package ir.eynakgroup.diet.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.JsonObject;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.table.TableUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.database.DatabaseHelper;
import ir.eynakgroup.diet.database.tables.Diet;
import ir.eynakgroup.diet.database.tables.FoodPackage;
import ir.eynakgroup.diet.database.tables.PackageFood;
import ir.eynakgroup.diet.database.tables.UserInfo;
import ir.eynakgroup.diet.network.RequestMethod;
import ir.eynakgroup.diet.network.response_models.CommonResponse;
import ir.eynakgroup.diet.network.response_models.CreateDietResponse;
import ir.eynakgroup.diet.network.response_models.LoginResponse;
import ir.eynakgroup.diet.network.response_models.User;
import ir.eynakgroup.diet.util.IabHelper;
import ir.eynakgroup.diet.util.IabResult;
import ir.eynakgroup.diet.util.Purchase;
import ir.eynakgroup.diet.utils.view.CustomTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Shayan on 5/6/2017.
 */

public class SetupDietActivity extends BaseActivity implements View.OnClickListener {

    private List<Chat> chatList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChatAdapter mAdapter;
    private FlexboxLayout flexBox;
    private ImageView imageBack;
    private TextView textTyping;

    private static final int BREAKFAST_ID = 0;
    private static final int LUNCH_ID = 1;
    private static final int SNACK_ID = 2;
    private static final int DINNER_ID = 3;
    private static RequestMethod mRequestMethod;

    private static DatabaseHelper mDatabaseHelper;

    private Level diffLevel = Level.NONE;

    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mActivity = this;
        mRequestMethod = getRequestMethod();
//        writeToFile(createDietJson(generateDiet(1750, 2.0f)), this);
        mDatabaseHelper = getDBHelper();
        textTyping = (TextView) findViewById(R.id.txt_typing);
        imageBack = (ImageView) findViewById(R.id.img_back);
        final View backParent = (View) imageBack.getParent();  // button: the view you want to enlarge hit area
        backParent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                imageBack.getHitRect(rect);
                rect.top -= 50;    // increase top hit area
                rect.left -= 50;   // increase left hit area
                rect.bottom += 50; // increase bottom hit area
                rect.right += 50;  // increase right hit area
                backParent.setTouchDelegate(new TouchDelegate(rect, imageBack));
            }
        });

        imageBack.setOnClickListener(this);
        flexBox = (FlexboxLayout) findViewById(R.id.flexbox_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ChatAdapter(this, chatList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.activity_orientation_margin_2x)));
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        showExitDialog();
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(R.string.exit_alert)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                })
                .setNegativeButton(R.string.exit_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/iran_sans.ttf");
        ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(typeface);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(typeface);
    }

    private class Chat {
        private String chatText;
        private Type chatType;

        public Chat(String chatText, Type chatType) {
            setChatText(chatText);
            setChatType(chatType);
        }

        public Chat(Type chatType) {
            setChatType(chatType);
        }

        public Type getChatType() {
            return chatType;
        }

        public void setChatType(Type chatType) {
            this.chatType = chatType;
        }

        public String getChatText() {
            return chatText;
        }

        public void setChatText(String chatText) {
            this.chatText = chatText;
        }
    }

    private enum Type {
        APP, USER, ALLERGY
    }

    boolean hardDiet = false;
    int lineNumber = 0;

    private Map<Level, Difficulty> difficulty;
    private void prepareChatData() {
        BufferedReader reader;
        try {
            UserInfo user = getDBHelper().getUserDao().queryForAll().get(0);
            reader = new BufferedReader(new InputStreamReader(getAssets().open("chat.txt")));
            // do reading, usually loop until end of file reading
            int tmp = lineNumber;
            while (tmp > 0) {
                reader.readLine();
                tmp--;
            }
            String line = reader.readLine();
            lineNumber++;
            float weightLoss;
            while (line != null) {
                String[] splits = line.split(":");
                if (line.startsWith("app")) {
                    if (line.contains("[first_time]") && lineNumber == 1) {
                        addListItem(splits[1].trim().replace("[name]", user.getName()), Type.APP);
                    } else if (line.contains("[ideal]") && lineNumber == 3) {
                        addListItem(splits[1].trim().replace("[ideal_weight]", round(calculateIdealWeight(user), 1) + ""), Type.APP);
                    } else if (line.contains("[weight]") && lineNumber == 4) {
                        if((weightLoss = round(user.getWeight() - calculateIdealWeight(user), 1)) > 0.0f)
                            addListItem(splits[1].trim().replace("[weight_loss]",  weightLoss + ""), Type.APP);

                        reader.readLine();
                        lineNumber++;
                        addResponseView("باشه");

                        return;
                    } else if (line.contains("[step]")) {
                        addListItem(splits[1].trim().replace("[diet_step]", Math.round(round(user.getWeight() - calculateIdealWeight(user), 1)) + ""), Type.APP);
                    } else if (line.contains("[type]") && lineNumber == 6) {
                        difficulty = calculateDietTypes(user);
                        if (difficulty.containsKey(Level.DIFFICULT)) {
                            addListItem(splits[1].trim().replace("[diet_type]", "3"), Type.APP);
                        } else if (difficulty.containsKey(Level.NORMAL)) {
                            addListItem(splits[1].trim().replace("[diet_type]", "2"), Type.APP);

                        } else if (difficulty.containsKey(Level.EASY)) {
                            addListItem(splits[1].trim().replace("[diet_type]", "1"), Type.APP);

                        } else if (difficulty.containsKey(Level.NONE)) {
                            addListItem(getString(R.string.no_diet), Type.APP);
                            return;
                        }

                    } else if (line.contains("[prefer]")) {
//                        if(!calculateDietTypes(user).containsKey(Integer.valueOf(-1)))
//                            addListItem(splits[1].trim(), Type.APP);
                        if (lineNumber == 7) {
                            addListItem(splits[1].trim(), Type.APP);
                            difficulty = calculateDietTypes(user);
                            if (difficulty.containsKey(Level.DIFFICULT)) {
                                reader.readLine();
                                lineNumber++;
                                addResponseView("ساده" + " " + difficulty.get(Level.EASY).getAmount() + " کیلویی");
                                addResponseView("متوسط" + " " + difficulty.get(Level.NORMAL).getAmount() + " کیلویی");
                                addResponseView("سخت" + " " + difficulty.get(Level.DIFFICULT).getAmount() + " کیلویی با ورزش");
                                return;
                            } else if (difficulty.containsKey(Level.NORMAL)) {
                                reader.readLine();
                                lineNumber++;
                                addResponseView("ساده" + " " + difficulty.get(Level.EASY).getAmount() + " کیلویی");
                                addResponseView("متوسط" + " " + difficulty.get(Level.NORMAL).getAmount() + " کیلویی");
                                return;
                            } else if (difficulty.containsKey(Level.EASY)) {
                                reader.readLine();
                                lineNumber++;
                                addResponseView("ساده" + " " + difficulty.get(Level.EASY).getAmount() + " کیلویی");
                                return;
                            }

                        } else if (lineNumber == 11 && hardDiet) {
                            addListItem(splits[1].trim(), Type.APP);
                            difficulty = calculateDietTypes(user);
                            if (difficulty.containsKey(Level.NORMAL)) {
                                reader.readLine();
                                lineNumber++;
                                addResponseView("ساده" + " " + difficulty.get(Integer.valueOf(1)).getAmount() + " کیلویی");
                                addResponseView("متوسط" + " " + difficulty.get(Integer.valueOf(2)).getAmount() + " کیلویی");
                                return;
                            } else if (difficulty.containsKey(Level.EASY)) {
                                reader.readLine();
                                lineNumber++;
                                addResponseView("ساده" + " " + difficulty.get(Integer.valueOf(1)).getAmount() + " کیلویی");
                                return;
                            }
                        }


                    } else if (line.contains("[allergy]") && lineNumber == 13) {
                        addListItem(splits[1].trim(), Type.APP);
                        addAllergyItem();
                        addResponseView(reader.readLine().split(":")[1].trim());
                        lineNumber++;
                        return;

                    } else if (line.contains("[hard]") && lineNumber == 9 && hardDiet) {
                        String[] response = reader.readLine().split(":");
                        lineNumber++;
                        addResponseView(response[1].split(",")[0].trim());
                        addResponseView(response[1].split(",")[1].trim());
                        return;
                    } else if (lineNumber == 2) {
                        addListItem(splits[1].trim(), Type.APP);
                    }


                }
//                else if(line.contains("user")){
//                    String[] responses;
//                    if(splits[1].contains(","))
//                        responses = splits[1].trim().split(",");
//                    else
//                        responses = new String[]{splits[1].trim()};
//
//                    for(String response: responses)
//                        addResponseView(response.trim());
//
//                }

                line = reader.readLine();
                lineNumber++;
            }
            reader.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


//        addListItem("من دستیار کرفس هستم.", Type.APP);
//        addListItem("سلام شایان، خوشحالم که تصمیم گرفتی رژیمت رو شروع کنی.", Type.APP);
//        addListItem("سلام شایان، خوشحالم که تصمیم گرفتی رژیمت رو شروع کنی.", Type.APP);
//        addListItem("سلام شایان، خوشحالم که تصمیم گرفتی رژیمت رو شروع کنی.", Type.APP);
//        addAllergyItem();
//
//        addResponseView("باشه");
//        addResponseView("حمید پسر خوبی است.");
//        addResponseView("باشه");
//        addResponseView("حمید پسر خوبی است.");

    }

    public float round(float value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (float) tmp / factor;
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareChatData();
    }

    private void addAllergyItem() {
        textTyping.setVisibility(View.VISIBLE);
        chatList.add(new WeakReference<>(new Chat(Type.ALLERGY)).get());
        mAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
        textTyping.setVisibility(View.GONE);
    }

    private void addListItem(final String chatText, final Type chatType) {
        if (!chatType.equals(Type.USER)) {
            textTyping.setVisibility(View.VISIBLE);
            chatList.add(new WeakReference<>(new Chat(chatText, chatType)).get());
            mAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            textTyping.setVisibility(View.GONE);

        } else {
            chatList.add(new WeakReference<>(new Chat(chatText, chatType)).get());
            mAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
        }

    }

    private void addResponseView(String response) {
        final CustomTextView textView = new CustomTextView(this);
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getResources().getDimensionPixelSize(R.dimen.activity_orientation_margin_2x), 0, 0, getResources().getDimensionPixelSize(R.dimen.activity_orientation_margin));
        textView.setLayoutParams(params);
        int padding = getResources().getDimensionPixelSize(R.dimen.activity_orientation_margin);
        textView.setPadding(padding, padding, padding, padding);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        textView.setText(response);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        textView.setBackgroundResource(R.drawable.background_text_solid);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v;
                addListItem(tv.getText().toString().trim(), Type.USER);
                flexBox.removeAllViewsInLayout();
                flexBox.requestLayout();
                hardDiet = tv.getText().toString().trim().contains("سخت") ? true : false;
                String text = tv.getText().toString().trim();
                if (text.contains("دریافت رژیم")) {
                    try {
                        showPurchaseDialog();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return;
                }else if(text.contains("سخت"))
                    diffLevel = Level.DIFFICULT;

                else if(text.contains("متوسط"))
                    diffLevel = Level.NORMAL;

                else if(text.contains("ساده"))
                    diffLevel = Level.EASY;


                prepareChatData();
            }
        });
        flexBox.addView(textView);
    }

    private class PurchaseDialog extends Dialog implements
            android.view.View.OnClickListener {

        private UserInfo user;
        private Activity activity;

        public PurchaseDialog(@NonNull Activity activity, UserInfo user) {
            super(activity);
            this.user = user;
            this.activity = activity;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_purchase);
            setCancelable(false);
            findViewById(R.id.diet_card_1).setOnClickListener(this);
            findViewById(R.id.diet_card_2).setOnClickListener(this);
            findViewById(R.id.diet_card_3).setOnClickListener(this);
            findViewById(R.id.diet_card_4).setOnClickListener(this);
            findViewById(R.id.diet_card_5).setOnClickListener(this);
            findViewById(R.id.diet_card_6).setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int credit = 0;
            mPayLoad = user.getUserId();
            System.out.println(user.getUserId() + "**********************");
            switch (v.getId()) {
                case R.id.diet_card_1:
                    mHelper.launchPurchaseFlow(this.activity, getString(R.string.sku_diet_1), RC_REQUEST, mPurchaseFinishedListener, mPayLoad);
                    break;
                case R.id.diet_card_2:
                    mHelper.launchPurchaseFlow(this.activity, getString(R.string.sku_diet_2), RC_REQUEST, mPurchaseFinishedListener, mPayLoad);
                    break;
                case R.id.diet_card_3:
                    mHelper.launchPurchaseFlow(this.activity, getString(R.string.sku_diet_3), RC_REQUEST, mPurchaseFinishedListener, mPayLoad);
                    break;
                case R.id.diet_card_4:
                    mHelper.launchPurchaseFlow(this.activity, getString(R.string.sku_diet_4), RC_REQUEST, mPurchaseFinishedListener, mPayLoad);
                    break;
                case R.id.diet_card_5:
                    mHelper.launchPurchaseFlow(this.activity, getString(R.string.sku_diet_5), RC_REQUEST, mPurchaseFinishedListener, mPayLoad);
                    break;
                case R.id.diet_card_6:
                    mHelper.launchPurchaseFlow(this.activity, getString(R.string.sku_diet_6), RC_REQUEST, mPurchaseFinishedListener, mPayLoad);
                    break;
                default:
                    break;
            }
            dismiss();
//            try {
//                UserInfo user = getDBHelper().getUserDao().queryForAll().get(0);
//                UpdateBuilder<UserInfo, Integer> updateBuilder = getDBHelper().getUserDao().updateBuilder();
            // set the criteria like you would a QueryBuilder
//                updateBuilder.where().eq("User_ID", user.getUserId());
            // update the value of your field(s)
//                updateBuilder.updateColumnValue("Credit" /* column */, credit /* value */);
//                updateBuilder.update();
//                System.out.println("------------ user credit updated");

//                showPurchaseDialog();
//            } catch (SQLException e) {
//                e.printStackTrace();
//                dismiss();
//            }
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                Log.d(TAG, "Error purchasing: " + result);
                return;
                //TODO Clean up code....
            } else {
                System.out.println("consuming .................................................." + purchase.getPurchaseState());
                mHelper.consumeAsync(purchase,
                        mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    if (result.isSuccess()) {
                        if (purchase.getSku().equals(getString(R.string.sku_diet_1))) {
                            try {
                                sendPurchasetoServer(purchase);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else if (purchase.getSku().equals(getString(R.string.sku_diet_2))) {
                            try {
                                sendPurchasetoServer(purchase);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else if (purchase.getSku().equals(getString(R.string.sku_diet_3))) {
                            try {
                                sendPurchasetoServer(purchase);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else if (purchase.getSku().equals(getString(R.string.sku_diet_4))) {
                            try {
                                sendPurchasetoServer(purchase);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else if (purchase.getSku().equals(getString(R.string.sku_diet_5))) {
                            try {
                                sendPurchasetoServer(purchase);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else if (purchase.getSku().equals(getString(R.string.sku_diet_6))) {
                            try {
                                sendPurchasetoServer(purchase);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        // handle error
                    }
                }
            };

    private void sendPurchasetoServer(final Purchase purchase) throws SQLException {
        final UserInfo user = getDBHelper().getUserDao().queryForAll().get(0);
        Call<CommonResponse> call = mRequestMethod.purchaseSend(user.getSessionId(), user.getApiKey(), user.getUserId(),
                purchase.getPackageName(), purchase.getSku(), purchase.getToken());
        final int[] values = getResources().getIntArray(R.array.sku_values);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                System.out.println(response.body().getError() + ">>>>>>>>" + response.body().getStatus());
                try {
                    if (response.body().getStatus().equals("success")) {
                        UpdateBuilder<UserInfo, Integer> updateBuilder = getDBHelper().getUserDao().updateBuilder();
                        // set the criteria like you would a QueryBuilder
                        updateBuilder.where().eq("User_ID", user.getUserId());
                        // update the value of your field(s)
                        if (purchase.getSku().equals(getString(R.string.sku_diet_1))) {
                            updateBuilder.updateColumnValue("Credit" /* column */, values[0] /* value */);
                            updateBuilder.update();
                        } else if (purchase.getSku().equals(getString(R.string.sku_diet_2))) {
                            updateBuilder.updateColumnValue("Credit" /* column */, values[1] /* value */);
                            updateBuilder.update();
                        } else if (purchase.getSku().equals(getString(R.string.sku_diet_3))) {
                            updateBuilder.updateColumnValue("Credit" /* column */, values[2] /* value */);
                            updateBuilder.update();
                        } else if (purchase.getSku().equals(getString(R.string.sku_diet_4))) {
                            updateBuilder.updateColumnValue("Credit" /* column */, values[3] /* value */);
                            updateBuilder.update();
                        } else if (purchase.getSku().equals(getString(R.string.sku_diet_5))) {
                            updateBuilder.updateColumnValue("Credit" /* column */, values[4] /* value */);
                            updateBuilder.update();
                        } else if (purchase.getSku().equals(getString(R.string.sku_diet_6))) {
                            updateBuilder.updateColumnValue("Credit" /* column */, values[5] /* value */);
                            updateBuilder.update();
                        }
                        showPurchaseDialog();
                    } else {
                        //TODO
                        Toast.makeText(getApplicationContext(), "Something is wrong!!",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    //TODO
                    Toast.makeText(getApplicationContext(), "Something is wrong in DB!!",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    private void showPurchaseDialog() throws SQLException {
        final UserInfo user = getDBHelper().getUserDao().queryForAll().get(0);
        final int credit = user.getCredit();
        if (credit > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage(R.string.dialog_diet_message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Difficulty diff = difficulty.get(diffLevel);
                            final DietProperty property = generateDiet(diff.getCalorie(), diff.getAmount());

                            //TODO
                            Call<CreateDietResponse> call = getRequestMethod().createDiet(user.getSessionId(), user.getUserId(), user.getApiKey(), property.getType(), property.getStartDate(), createDietJson(property.getId()));

                            call.enqueue(new Callback<CreateDietResponse>() {
                                @Override
                                public void onResponse(Call<CreateDietResponse> call, Response<CreateDietResponse> response) {
                                    System.out.println("----------------Diet gene1rated to server!!");

                                    if (response.body().getError() == null)
                                        try {
                                            UpdateBuilder<Diet, Integer> updateBuilder = mDatabaseHelper.getDietDao().updateBuilder();
                                            updateBuilder.where().eq("_id", property.getId());
                                            updateBuilder.updateColumnValue("serverId", response.body().getId());
                                            updateBuilder.update();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    else
                                        System.out.println(response.body().getError());

                                    setResult(RESULT_OK);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<CreateDietResponse> call, Throwable t) {

                                }
                            });

                            try {
                                UpdateBuilder<UserInfo, Integer> updateBuilder = getDBHelper().getUserDao().updateBuilder();
                                // set the criteria like you would a QueryBuilder
                                updateBuilder.where().eq("User_ID", user.getId());
                                // update the value of your field(s)
                                updateBuilder.updateColumnValue("Credit", credit - 1);
                                updateBuilder.update();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/iran_sans.ttf");
            ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(typeface);
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(typeface);

        } else
            new PurchaseDialog(this, user).show();

    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }

    private class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> implements View.OnClickListener {

        private List<Chat> chatList;
        private Context context;
        private boolean[] checked = new boolean[8];

        public class MyViewHolder extends RecyclerView.ViewHolder {
            CustomTextView chatText;
            //            RelativeLayout chatLayout;
            LinearLayout allergyLayout;


            public MyViewHolder(View view) {
                super(view);
//                chatLayout = (RelativeLayout) view;
                chatText = (CustomTextView) view.findViewById(R.id.text_chat);
                allergyLayout = (LinearLayout) view.findViewById(R.id.layout_allergy);
            }

        }

        public ChatAdapter(Context context, List<Chat> chatList) {
            this.chatList = chatList;
            this.context = context;
            init();
        }

        private void init() {
            for (int i = 0; i < checked.length; i++)
                checked[i] = false;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item, parent, false);

            return new MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            if (chatList.get(position).getChatType().equals(Type.APP)) {
                holder.chatText.setVisibility(View.VISIBLE);
                holder.chatText.setBackgroundResource(R.drawable.background_text_stroke);
                holder.chatText.setTextColor(ContextCompat.getColor(context, R.color.colorDescription));
                params.setMargins(0, 0, context.getResources().getDimensionPixelSize(R.dimen.indicator_padding), 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                holder.chatText.setText(chatList.get(position).getChatText());
                holder.allergyLayout.setVisibility(View.GONE);

            } else if (chatList.get(position).getChatType().equals(Type.USER)) {
                holder.chatText.setVisibility(View.VISIBLE);
                holder.chatText.setBackgroundResource(R.drawable.background_text_solid);
                holder.chatText.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                params.setMargins(context.getResources().getDimensionPixelSize(R.dimen.indicator_padding), 0, 0, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                holder.chatText.setText(chatList.get(position).getChatText());
                holder.allergyLayout.setVisibility(View.GONE);

            } else {
                params.setMargins(0, 0, 0, 0);
                holder.chatText.setVisibility(View.GONE);
                holder.allergyLayout.setVisibility(View.VISIBLE);

                if (checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_egg).getTag().toString())]) {
                    holder.allergyLayout.findViewById(R.id.layout_egg).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_egg).findViewById(R.id.txt_egg)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                } else {
                    holder.allergyLayout.findViewById(R.id.layout_egg).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_egg).findViewById(R.id.txt_egg)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                if (checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_eggplant).getTag().toString())]) {
                    holder.allergyLayout.findViewById(R.id.layout_eggplant).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_eggplant).findViewById(R.id.txt_eggplant)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                } else {
                    holder.allergyLayout.findViewById(R.id.layout_eggplant).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_eggplant).findViewById(R.id.txt_eggplant)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                if (checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_zucchini).getTag().toString())]) {
                    holder.allergyLayout.findViewById(R.id.layout_zucchini).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_zucchini).findViewById(R.id.txt_zucchini)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                } else {
                    holder.allergyLayout.findViewById(R.id.layout_zucchini).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_zucchini).findViewById(R.id.txt_zucchini)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                if (checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_walnut).getTag().toString())]) {
                    holder.allergyLayout.findViewById(R.id.layout_walnut).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_walnut).findViewById(R.id.txt_walnut)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                } else {
                    holder.allergyLayout.findViewById(R.id.layout_walnut).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_walnut).findViewById(R.id.txt_walnut)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                if (checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_fava).getTag().toString())]) {
                    holder.allergyLayout.findViewById(R.id.layout_fava).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_fava).findViewById(R.id.txt_fava)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                } else {
                    holder.allergyLayout.findViewById(R.id.layout_fava).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_fava).findViewById(R.id.txt_fava)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                if (checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_peanut).getTag().toString())]) {
                    holder.allergyLayout.findViewById(R.id.layout_peanut).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_peanut).findViewById(R.id.txt_peanut)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                } else {
                    holder.allergyLayout.findViewById(R.id.layout_peanut).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_peanut).findViewById(R.id.txt_peanut)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                if (checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_shrimp).getTag().toString())]) {
                    holder.allergyLayout.findViewById(R.id.layout_shrimp).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_shrimp).findViewById(R.id.txt_shrimp)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                } else {
                    holder.allergyLayout.findViewById(R.id.layout_shrimp).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_shrimp).findViewById(R.id.txt_shrimp)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                if (checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_soya).getTag().toString())]) {
                    holder.allergyLayout.findViewById(R.id.layout_soya).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_soya).findViewById(R.id.txt_soya)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                } else {
                    holder.allergyLayout.findViewById(R.id.layout_soya).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView) holder.allergyLayout.findViewById(R.id.layout_soya).findViewById(R.id.txt_soya)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                setClickListener(holder);
            }
            holder.chatText.setLayoutParams(params);
        }

        @Override
        public int getItemCount() {
            return chatList.size();
        }

        private void setClickListener(MyViewHolder holder) {
            holder.allergyLayout.findViewById(R.id.layout_egg).setOnClickListener(this);
            holder.allergyLayout.findViewById(R.id.layout_eggplant).setOnClickListener(this);
            holder.allergyLayout.findViewById(R.id.layout_fava).setOnClickListener(this);
            holder.allergyLayout.findViewById(R.id.layout_peanut).setOnClickListener(this);
            holder.allergyLayout.findViewById(R.id.layout_shrimp).setOnClickListener(this);
            holder.allergyLayout.findViewById(R.id.layout_soya).setOnClickListener(this);
            holder.allergyLayout.findViewById(R.id.layout_walnut).setOnClickListener(this);
            holder.allergyLayout.findViewById(R.id.layout_zucchini).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_egg:
                    if (!checked[Integer.valueOf(v.getTag().toString())]) {
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView) v.findViewById(R.id.txt_egg)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    } else {
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView) v.findViewById(R.id.txt_egg)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                case R.id.layout_eggplant:
                    if (!checked[Integer.valueOf(v.getTag().toString())]) {
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView) v.findViewById(R.id.txt_eggplant)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    } else {
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView) v.findViewById(R.id.txt_eggplant)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                case R.id.layout_peanut:
                    if (!checked[Integer.valueOf(v.getTag().toString())]) {
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView) v.findViewById(R.id.txt_peanut)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    } else {
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView) v.findViewById(R.id.txt_peanut)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                case R.id.layout_fava:
                    if (!checked[Integer.valueOf(v.getTag().toString())]) {
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView) v.findViewById(R.id.txt_fava)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    } else {
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView) v.findViewById(R.id.txt_fava)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                case R.id.layout_shrimp:
                    if (!checked[Integer.valueOf(v.getTag().toString())]) {
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView) v.findViewById(R.id.txt_shrimp)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    } else {
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView) v.findViewById(R.id.txt_shrimp)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                case R.id.layout_soya:
                    if (!checked[Integer.valueOf(v.getTag().toString())]) {
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView) v.findViewById(R.id.txt_soya)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    } else {
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView) v.findViewById(R.id.txt_soya)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                case R.id.layout_walnut:
                    if (!checked[Integer.valueOf(v.getTag().toString())]) {
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView) v.findViewById(R.id.txt_walnut)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    } else {
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView) v.findViewById(R.id.txt_walnut)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                case R.id.layout_zucchini:
                    if (!checked[Integer.valueOf(v.getTag().toString())]) {
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView) v.findViewById(R.id.txt_zucchini)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    } else {
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView) v.findViewById(R.id.txt_zucchini)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                default:

            }
        }

    }

    private float calculateDailyRequiredCalorie(UserInfo user) throws SQLException {
//        UserInfo user = getDBHelper().getUserDao().queryForAll().get(0);
        float tmp;
        if (user.getGender() == User.Gender.Female.ordinal())
            tmp = (float) (655 + (9.479866 * user.getWeight()) + (1.8503947 * user.getHeight()) - (4.7 * user.getAge()));
        else
            tmp = (float) (66 + (13.889106 * user.getWeight()) + (5.0787429 * user.getHeight()) - (6.8 * user.getAge()));

        return Math.round(tmp * getActivityProportion(user));
    }

    private float getActivityProportion(UserInfo user) {
        float factor;
        switch (user.getActivityLevel()) {
            case 1:
                factor = 1.2f;
                break;
            case 2:
                factor = 1.3f;
                break;
            case 3:
                factor = 1.4f;
                break;
            case 4:
                factor = 1.6f;
                break;
            case 5:
                factor = 1.8f;
                break;
            default:
                factor = 1.2f;
                break;
        }
        return factor;
    }

    private float calculateBMI(UserInfo user) throws SQLException {
//        UserInfo user = getDBHelper().getUserDao().queryForAll().get(0);
        float height = user.getHeight() / 100;
        return user.getWeight() / (height * height);
    }

    private float calculateIdealWeight(UserInfo user) throws SQLException {
//        UserInfo user = getDBHelper().getUserDao().queryForAll().get(0);
        float height = user.getHeight() / 100;
        if (user.getGender() == User.Gender.Female.ordinal())
            return 22 * height * height;
        else
            return 23 * height * height;

    }

    private class Difficulty {
        private float calorie;
        private float amount;

        public Difficulty(float calorie, float amount) {
            this.calorie = calorie;
            this.amount = amount;
        }

        public float getCalorie() {
            return this.calorie;
        }

        public float getAmount() {
            return round(this.amount, 1);
        }
    }

    private Map<Level, Difficulty> calculateDietTypes(UserInfo user) throws SQLException {
        float bmi = calculateBMI(user);
        float reqCal = calculateDailyRequiredCalorie(user);
        Map<Level, Difficulty> difficulty = new HashMap();

        if (user.getGender() == User.Gender.Female.ordinal()) {
            if (bmi > 21 && bmi <= 23)
                difficulty.put(Level.EASY, new Difficulty(reqCal, user.getWeight() - calculateIdealWeight(user)));
            if (bmi <= 21)
                difficulty.put(Level.NONE, new Difficulty(0.0f, 0.0f));
        } else {
            if (bmi > 22 && bmi <= 24)
                difficulty.put(Level.EASY, new Difficulty(reqCal, user.getWeight() - calculateIdealWeight(user)));
            if (bmi <= 22)
                difficulty.put(Level.NONE, new Difficulty(0.0f, 0.0f));
        }
        if (difficulty.size() == 0) {
            int pivotCal;
            if (user.getGender() == User.Gender.Female.ordinal()) {
                if (bmi <= 25)
                    pivotCal = 7700;
                else
                    pivotCal = 7000;
            } else {
                if (bmi <= 25)
                    pivotCal = 8400;
                else
                    pivotCal = 7700;
            }

            float dietCal;
            if (reqCal - ((pivotCal * 1.2f) / 7) >= 1000) {
                dietCal = reqCal - (pivotCal / 7);
                difficulty.put(Level.DIFFICULT, new Difficulty(dietCal, 4.8f));
            }
            if ((dietCal = reqCal - (pivotCal / 7)) >= 1000)
                difficulty.put(Level.NORMAL, new Difficulty(dietCal, 4f));

            if ((dietCal = reqCal - (pivotCal / 14)) >= 1000)
                difficulty.put(Level.EASY, new Difficulty(dietCal, 2f));

        }

        return difficulty;
    }

    private DietProperty generateDiet(float dietCalorie, float amount) {
        if (dietCalorie < 1000 || distance(dietCalorie, 1000)) {
            return generateMonthlyDietDB(1000, amount);
        }
        if (distance(dietCalorie, 1250)) {
            return generateMonthlyDietDB(1250, amount);
        }

        if (distance(dietCalorie, 1500)) {
            return generateMonthlyDietDB(1500, amount);
        }

        if (distance(dietCalorie, 1750)) {
            return generateMonthlyDietDB(1750, amount);
        }

        if (distance(dietCalorie, 2000)) {
            return generateMonthlyDietDB(2000, amount);
        }

        if (distance(dietCalorie, 2250) || dietCalorie > 2250) {
            return generateMonthlyDietDB(2250, amount);
        }

        return new DietProperty();

    }

    private boolean distance(float dietCalorie, float dailyCalorie) {
        if (dietCalorie > dailyCalorie - 125 && dietCalorie <= dailyCalorie + 125)
            return true;

        return false;
    }

    private class DietProperty {
        private int id = 0;
        private String type = "0";
        private String startDate = "0";

        public int getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }
    }

    private DietProperty generateMonthlyDietDB(int dietCalorie, float weightLossAmount) {
        DietProperty property = new DietProperty();
        try {
            QueryBuilder<FoodPackage, Integer> foodPackageQueryBuilder = getDBHelper().getFoodPackageDao().queryBuilder();

            foodPackageQueryBuilder.where().eq("mealId", BREAKFAST_ID);
            List<FoodPackage> breakfastPackageList = foodPackageQueryBuilder.query();
            ArrayList<String> tempBreakfast = new ArrayList<>();
            Collections.shuffle(breakfastPackageList);
            for (int i = 0; i < 9; i++)
                tempBreakfast.add(breakfastPackageList.get(i).getId());

            foodPackageQueryBuilder.reset();
            foodPackageQueryBuilder.where().eq("mealId", LUNCH_ID);
            List<FoodPackage> lunchPackageList = foodPackageQueryBuilder.query();
            ArrayList<String> tempLunch = new ArrayList<>();
            Collections.shuffle(lunchPackageList);
            for (int i = 0; i < 9; i++)
                tempLunch.add(lunchPackageList.get(i).getId());


            foodPackageQueryBuilder.reset();
            foodPackageQueryBuilder.where().eq("mealId", SNACK_ID);
            List<FoodPackage> snackPackageList = foodPackageQueryBuilder.query();
            ArrayList<String> tempSnack = new ArrayList<>();
            Collections.shuffle(snackPackageList);
            for (int i = 0; i < 9; i++)
                tempSnack.add(snackPackageList.get(i).getId());

            foodPackageQueryBuilder.reset();
            foodPackageQueryBuilder.where().eq("mealId", DINNER_ID);
            List<FoodPackage> dinnerPackageList = foodPackageQueryBuilder.query();
            ArrayList<String> tempDinner = new ArrayList<>();
            Collections.shuffle(dinnerPackageList);
            for (int i = 0; i < 9; i++)
                tempDinner.add(dinnerPackageList.get(i).getId());

            float currentWeight = getDBHelper().getUserDao().queryForAll().get(0).getWeight();
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);
            int second = Calendar.getInstance().get(Calendar.SECOND);
            long startDate = Calendar.getInstance().getTimeInMillis() - ((hour*60*60)+(minute*60)+second)*1000;
            int day = 1;
            int lastDietNumber = getAppPreferences().getDietNumber();
            while (day <= 31) {
                Diet diet = new Diet();
                diet.setDay(day);
                diet.setDietType(dietCalorie + "");
                diet.setStartDate(startDate + "");
                diet.setStartWeight(round(currentWeight, 1) + "");
                diet.setGoalWeight(round(currentWeight - weightLossAmount, 1) + "");
                diet.setId(lastDietNumber + 1);

                diet.setSelectedBreakfast("0");
                diet.setBreakfastPack1(tempBreakfast.get(0));
                diet.setBreakfastPack2(tempBreakfast.get(1));
                diet.setBreakfastPack3(tempBreakfast.get(2));
                Collections.shuffle(breakfastPackageList);
                while (tempBreakfast.size() <= 12) {
                    for (FoodPackage pack : breakfastPackageList) {
                        if (!tempBreakfast.contains(pack.getId()))
                            tempBreakfast.add(pack.getId());
                    }
                }
                tempBreakfast.remove(0);
                tempBreakfast.remove(0);
                tempBreakfast.remove(0);

                diet.setSelectedLunch("0");
                diet.setLunchPack1(tempLunch.get(0));
                diet.setLunchPack2(tempLunch.get(1));
                diet.setLunchPack3(tempLunch.get(2));
                Collections.shuffle(lunchPackageList);
                while (tempLunch.size() <= 12) {
                    for (FoodPackage pack : lunchPackageList) {
                        if (!tempLunch.contains(pack.getId()))
                            tempLunch.add(pack.getId());
                    }
                }
                tempLunch.remove(0);
                tempLunch.remove(0);
                tempLunch.remove(0);

                diet.setSelectedSnack("0");
                diet.setSnackPack1(tempSnack.get(0));
                diet.setSnackPack2(tempSnack.get(1));
                diet.setSnackPack3(tempSnack.get(2));
                Collections.shuffle(snackPackageList);
                while (tempSnack.size() <= 12) {
                    for (FoodPackage pack : snackPackageList) {
                        if (!tempSnack.contains(pack.getId()))
                            tempSnack.add(pack.getId());
                    }
                }
                tempSnack.remove(0);
                tempSnack.remove(0);
                tempSnack.remove(0);

                diet.setSelectedDinner("0");
                diet.setDinnerPack1(tempDinner.get(0));
                diet.setDinnerPack2(tempDinner.get(1));
                diet.setDinnerPack3(tempDinner.get(2));
                Collections.shuffle(dinnerPackageList);
                while (tempDinner.size() <= 12) {
                    for (FoodPackage pack : dinnerPackageList) {
                        if (!tempDinner.contains(pack.getId()))
                            tempDinner.add(pack.getId());
                    }
                }
                tempDinner.remove(0);
                tempDinner.remove(0);
                tempDinner.remove(0);

                getDBHelper().getDietDao().create(diet);
                day++;
            }
            getAppPreferences().setDietNumber(++lastDietNumber);
            property.setId(lastDietNumber);
            property.setStartDate(startDate + "");
            property.setType(dietCalorie + "");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return property;
    }

    private String createDietJson(int id) {
        try {
            QueryBuilder<Diet, Integer> dietQueryBuilder = getDBHelper().getDietDao().queryBuilder();
            dietQueryBuilder.where().eq("_id", id + "");
            List<Diet> dietList = dietQueryBuilder.query();
//            if (dietList.size() > 0) {
//                Collections.sort(dietList, new Comparator<Diet>() {
//
//                    @Override
//                    public int compare(Diet diet1, Diet diet2) {
//                        return ((Integer)diet1.getDay()).compareTo(diet2.getDay());
//                    }
//                });
//            }
            StringBuilder dietJson = new StringBuilder();
            dietJson.append("{");
            for (int i = 0; i < dietList.size(); i++) {
                Diet diet = dietList.get(i);
                String breakfastChoices = "[" + "\"" + diet.getBreakfastPack1() + "\"" + "," + "\"" + diet.getBreakfastPack2() + "\"" + "," + "\"" + diet.getBreakfastPack3() + "\"" + "]";
                String lunchChoices = "[" + "\"" + diet.getLunchPack1() + "\"" + "," + "\"" + diet.getLunchPack2() + "\"" + "," + "\"" + diet.getLunchPack3() + "\"" + "]";
                String snackChoices = "[" + "\"" + diet.getSnackPack1() + "\"" + "," + "\"" + diet.getSnackPack2() + "\"" + "," + "\"" + diet.getSnackPack3() + "\"" + "]";
                String dinnerChoices = "[" + "\"" + diet.getDinnerPack1() + "\"" + "," + "\"" + diet.getDinnerPack2() + "\"" + "," + "\"" + diet.getDinnerPack3() + "\"" + "]";

                dietJson.append("\"" + diet.getDay() + "\"" + ":{" + "\"" + BREAKFAST_ID + "\"" + ":{" + "\"" + "status" + "\"" + ":0," + "\"" + "choices" + "\"" + ":" + breakfastChoices + "," + "\"" + "selected" + "\"" + ":\"" + diet.getSelectedBreakfast() + "\"}");
                dietJson.append("," + "\"" + LUNCH_ID + "\"" + ":{" + "\"" + "status" + "\"" + ":0," + "\"" + "choices" + "\"" + ":" + lunchChoices + "," + "\"" + "selected" + "\"" + ":\"" + diet.getSelectedLunch() + "\"}");
                dietJson.append("," + "\"" + SNACK_ID + "\"" + ":{" + "\"" + "status" + "\"" + ":0," + "\"" + "choices" + "\"" + ":" + snackChoices + "," + "\"" + "selected" + "\"" + ":\"" + diet.getSelectedSnack() + "\"}");
                dietJson.append("," + "\"" + DINNER_ID + "\"" + ":{" + "\"" + "status" + "\"" + ":0," + "\"" + "choices" + "\"" + ":" + dinnerChoices + "," + "\"" + "selected" + "\"" + ":\"" + diet.getSelectedDinner() + "\"}}");

                if (i != dietList.size() - 1)
                    dietJson.append(",");

            }

            return dietJson.append("}").toString();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("diet.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("DIET_FILE", "File write failed: " + e.toString());
        }
    }

//    var dietHistory = function(userId, startDate, diet, type){
//        this.userId = userId;
//        this.startDate = startDate;
//        this.diet = diet;
//        this.type = type; //1000,1250,1500,1750,2000,2250
//  /*
//  diet = {
//    0:{ //first day of diet
//      mealId : {
//        status : 0 \\0 do nothing, 1 done, 2 break
//        choices : [packageIds],
//        selected : packageId
//      }
//    }
//  }
//  */
//        this.createdAt = new Date();
//    }
    private enum Level{
        NONE,
        EASY,
        NORMAL,
        DIFFICULT
    }
}
