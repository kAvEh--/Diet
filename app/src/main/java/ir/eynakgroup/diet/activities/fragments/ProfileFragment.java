package ir.eynakgroup.diet.activities.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.j256.ormlite.stmt.QueryBuilder;
import com.shawnlin.numberpicker.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.activities.SettingActivity;
import ir.eynakgroup.diet.database.DatabaseHelper;
import ir.eynakgroup.diet.database.tables.Diet;
import ir.eynakgroup.diet.database.tables.UserInfo;
import ir.eynakgroup.diet.utils.AppPreferences;
import ir.eynakgroup.diet.utils.view.CircularImageView;
import ir.eynakgroup.diet.utils.view.CustomTextView;

public class ProfileFragment extends Fragment {

    private Context mContext;
    private AppPreferences appPreferences;
    private static ProfileFragment mProfileFragmentInstance = null;
    public final static String TAG = "FRAGMENT_PROFILE";
    private static TextView textTotalPoint = null;
    JSONArray data = new JSONArray();

    private ProfileFragment(Context context) {
        mContext = context;
    }

    public static ProfileFragment newInstance(Context context) {
        if (mProfileFragmentInstance == null)
            mProfileFragmentInstance = new ProfileFragment(context);

        if (textTotalPoint != null) {
            SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            int points = preferences.getInt("Points", 0);
            textTotalPoint.setText(points + "");
        }

        return mProfileFragmentInstance;
    }

    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_about:

                return true;
            case R.id.action_insta:

                return true;
            case R.id.action_telegram:

                return true;
            case R.id.action_setting:
                Intent myIntent = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (appPreferences == null)
            appPreferences = new AppPreferences(getContext());

        android.support.v7.widget.Toolbar tb = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
//        tb.setNavigationIcon(R.drawable.common_google_signin_btn_icon_dark );
        tb.setTitle("");
//        tb.setNavigationIcon(R.drawable.ic_shopping_cart_white_24dp);
        ((AppCompatActivity) getActivity()).setSupportActionBar(tb);
//        tb.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new SetupDietActivity.PurchaseDialog(this, user).show();
//            }
//        });

        final CircularImageView imageProfile = (CircularImageView) view.findViewById(R.id.img_profile);
        final TextView textName = (CustomTextView) view.findViewById(R.id.txt_name);
        final TextView textList = (CustomTextView) view.findViewById(R.id.txt_list);
        final TextView textCredit = (CustomTextView) view.findViewById(R.id.txt_credit);
        final TextView textBMI = (CustomTextView) view.findViewById(R.id.txt_bmi);
        final TextView dietDay = (CustomTextView) view.findViewById(R.id.profile_diet_day);
        final TextView dietPoint = (CustomTextView) view.findViewById(R.id.profile_diet_point);
        final TextView beginW = (CustomTextView) view.findViewById(R.id.begin_weight);
        final TextView currentW = (CustomTextView) view.findViewById(R.id.current_weight);
        final TextView bLabel = (CustomTextView) view.findViewById(R.id.begin_weight_label);
        final TextView eLabel = (CustomTextView) view.findViewById(R.id.current_weight_label);
        final View secondBar = view.findViewById(R.id.second_bar);
        final View barLabel = view.findViewById(R.id.second_bar_label);
        final RoundCornerProgressBar pb = (RoundCornerProgressBar) view.findViewById(R.id.pb);
        textTotalPoint = (CustomTextView) view.findViewById(R.id.txt_total_point);
        final ImageView level = (ImageView) view.findViewById(R.id.diet_level_img);
        final LinearLayout profile_holder = (LinearLayout) view.findViewById(R.id.profile_holder);
        final CustomTextView nothing = (CustomTextView) view.findViewById(R.id.profile_nothing);
        final ImageView[] icons = new ImageView[30];

        icons[0] = (ImageView) view.findViewById(R.id.c_1);
        icons[1] = (ImageView) view.findViewById(R.id.c_2);
        icons[2] = (ImageView) view.findViewById(R.id.c_3);
        icons[3] = (ImageView) view.findViewById(R.id.c_4);
        icons[4] = (ImageView) view.findViewById(R.id.c_5);
        icons[5] = (ImageView) view.findViewById(R.id.c_6);
        icons[6] = (ImageView) view.findViewById(R.id.c_7);
        icons[7] = (ImageView) view.findViewById(R.id.c_8);
        icons[8] = (ImageView) view.findViewById(R.id.c_9);
        icons[9] = (ImageView) view.findViewById(R.id.c_10);
        icons[10] = (ImageView) view.findViewById(R.id.c_11);
        icons[11] = (ImageView) view.findViewById(R.id.c_12);
        icons[12] = (ImageView) view.findViewById(R.id.c_13);
        icons[13] = (ImageView) view.findViewById(R.id.c_14);
        icons[14] = (ImageView) view.findViewById(R.id.c_15);
        icons[15] = (ImageView) view.findViewById(R.id.c_16);
        icons[16] = (ImageView) view.findViewById(R.id.c_17);
        icons[17] = (ImageView) view.findViewById(R.id.c_18);
        icons[18] = (ImageView) view.findViewById(R.id.c_19);
        icons[19] = (ImageView) view.findViewById(R.id.c_20);
        icons[20] = (ImageView) view.findViewById(R.id.c_21);
        icons[21] = (ImageView) view.findViewById(R.id.c_22);
        icons[22] = (ImageView) view.findViewById(R.id.c_23);
        icons[23] = (ImageView) view.findViewById(R.id.c_24);
        icons[24] = (ImageView) view.findViewById(R.id.c_25);
        icons[25] = (ImageView) view.findViewById(R.id.c_26);
        icons[26] = (ImageView) view.findViewById(R.id.c_27);
        icons[27] = (ImageView) view.findViewById(R.id.c_28);
        icons[28] = (ImageView) view.findViewById(R.id.c_29);
        icons[29] = (ImageView) view.findViewById(R.id.c_30);

        final NumberPicker picker = (NumberPicker) view.findViewById(R.id.diet_picker);
        picker.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/iran_sans_light.ttf"));
        picker.setMaxValue(appPreferences.getDietNumber());
        final DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        final String[] temp = getResources().getStringArray(R.array.days);
        picker.setValue(picker.getMaxValue());
        String[] diet_list = getResources().getStringArray(R.array.diet_list);
        if (appPreferences.getDietNumber() > 0) {
            diet_list[appPreferences.getDietNumber() - 1] = "رژیم فعلی";
            picker.setDisplayedValues(diet_list);
        }

        try {
            if (appPreferences.getDietData() != null) {
                data = new JSONArray(appPreferences.getDietData());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        QueryBuilder<Diet, Integer> dietQueryBuilder = null;
        List<Diet> dietList = null;
        try {
            dietQueryBuilder = databaseHelper.getDietDao().queryBuilder();
            dietQueryBuilder.where().eq("_id", appPreferences.getDietNumber());
            dietList = dietQueryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int day = 30;
        if (dietList != null && dietList.size() > 0) {
            long diff = Calendar.getInstance().getTimeInMillis() - Long.parseLong(dietList.get(0).getStartDate());
            day = (int) (diff / TimeUnit.DAYS.toMillis(1));
        }
        final int finalDay = day;
        final int dietNum = appPreferences.getDietNumber();
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                try {
                    JSONObject tmp = new JSONObject((String) data.get(newVal - 1));
                    dietPoint.setText("امتیاز شما از این رژیم : " + tmp.getInt("point"));

                    double s_w = tmp.getDouble("startWeight");
                    double g_w = tmp.getDouble("goalWeight");

                    beginW.setText(String.valueOf(g_w) + " کیلوگرم");
                    currentW.setText(String.valueOf(s_w) + " کیلوگرم");

                    int s1, s2, s3, s4;
                    for (int i = 0; i < 30; i++) {
                        if (i <= finalDay || newVal != dietNum) {
                            JSONObject tt = tmp.getJSONObject(String.valueOf(i + 1));
                            s1 = tt.getJSONObject("0").getInt("status");
                            s2 = tt.getJSONObject("1").getInt("status");
                            s3 = tt.getJSONObject("2").getInt("status");
                            s4 = tt.getJSONObject("3").getInt("status");
                            if (s1 == 0 & s2 == 0 && s3 == 0 && s4 == 0) {
                                icons[i].setBackground(getResources().getDrawable(R.drawable.none));
                                icons[i].setImageResource(0);
                            } else if (s1 == 1 & s2 == 0 && s3 == 0 && s4 == 0) {
                                icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                                icons[i].setImageResource(R.drawable.b_1);
                            } else if (s1 == 0 & s2 == 1 && s3 == 0 && s4 == 0) {
                                icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                                icons[i].setImageResource(R.drawable.b_2);
                            } else if (s1 == 0 & s2 == 0 && s3 == 1 && s4 == 0) {
                                icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                                icons[i].setImageResource(R.drawable.b_3);
                            } else if (s1 == 0 & s2 == 0 && s3 == 0 && s4 == 1) {
                                icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                                icons[i].setImageResource(R.drawable.b_4);
                            } else if (s1 == 1 & s2 == 1 && s3 == 0 && s4 == 0) {
                                icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                                icons[i].setImageResource(R.drawable.b_5);
                            } else if (s1 == 1 & s2 == 0 && s3 == 1 && s4 == 0) {
                                icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                                icons[i].setImageResource(R.drawable.b_6);
                            } else if (s1 == 1 & s2 == 0 && s3 == 0 && s4 == 1) {
                                icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                                icons[i].setImageResource(R.drawable.b_7);
                            } else if (s1 == 0 & s2 == 1 && s3 == 1 && s4 == 0) {
                                icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                                icons[i].setImageResource(R.drawable.b_8);
                            } else if (s1 == 0 & s2 == 1 && s3 == 0 && s4 == 1) {
                                icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                                icons[i].setImageResource(R.drawable.b_9);
                            } else if (s1 == 0 & s2 == 0 && s3 == 1 && s4 == 1) {
                                icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                                icons[i].setImageResource(R.drawable.b_10);
                            } else if (s1 == 1 & s2 == 1 && s3 == 1 && s4 == 0) {
                                icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                                icons[i].setImageResource(R.drawable.b_11);
                            } else if (s1 == 1 & s2 == 1 && s3 == 0 && s4 == 1) {
                                icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                                icons[i].setImageResource(R.drawable.b_12);
                            } else if (s1 == 0 & s2 == 1 && s3 == 1 && s4 == 1) {
                                icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                                icons[i].setImageResource(R.drawable.b_13);
                            } else if (s1 == 1 & s2 == 1 && s3 == 1 && s4 == 1) {
                                icons[i].setBackground(getResources().getDrawable(R.drawable.full));
                            }
                        } else {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.bg_none));
                            icons[i].setImageResource(0);
                        }
                    }

                    if (finalDay >= 0 && finalDay < 31 && newVal == dietNum) {
                        bLabel.setText("وزن هدف");
                        eLabel.setText("وزن فعلی");
                        dietDay.setText(temp[finalDay]);
                        UserInfo user = databaseHelper.getUserDao().queryForAll().get(0);
                        currentW.setText(String.valueOf(user.getWeight()) + " کیلوگرم");
                        if (user.getWeight() >= s_w) {
                            pb.setProgress(0);
                            secondBar.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10f));
                            barLabel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10f));
                        } else if (user.getWeight() <= g_w) {
                            pb.setProgress(100);
                            secondBar.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0f));
                            barLabel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0f));
                        } else {
                            pb.setProgress((float) ((s_w - user.getWeight()) / (s_w - g_w)) * 100f);
                            secondBar.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, (float) (10 - ((s_w - user.getWeight()) / (s_w - g_w)) * 10)));
                            barLabel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, (float) (10 - ((s_w - user.getWeight()) / (s_w - g_w)) * 10)));
                        }
                    } else {
                        bLabel.setText("انتهای رژیم");
                        eLabel.setText("ابتدای رژیم");
                        dietDay.setText("وزن کم شده با این رژیم : " + round((float) (s_w - g_w), 1) + " کیلوگرم");
                        pb.setProgress(100f);
                        secondBar.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10f));
                        barLabel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10f));
                    }
                    switch (tmp.getString("type")) {
                        case "1000":
                            level.setImageResource(R.drawable.level_3);
                            break;
                        case "1250":
                            level.setImageResource(R.drawable.level_3);
                            break;
                        case "1500":
                            level.setImageResource(R.drawable.level_2);
                            break;
                        case "1750":
                            level.setImageResource(R.drawable.level_2);
                            break;
                        case "2000":
                            level.setImageResource(R.drawable.level_1);
                            break;
                        case "2250":
                            level.setImageResource(R.drawable.level_1);
                            break;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            UserInfo user = databaseHelper.getUserDao().queryForAll().get(0);
            textName.setText(user.getName());
            textBMI.setText(round(calculateBMI(user), 1) + "");
            textCredit.setText(user.getCredit() + "");
            textTotalPoint.setText(appPreferences.getTotalPoint() + "");
            //------
            if (appPreferences.getDietNumber() > 0) {
                profile_holder.setVisibility(View.VISIBLE);
                nothing.setVisibility(View.GONE);
                JSONArray data = new JSONArray(appPreferences.getDietData());
                System.out.println("-------------------------------------------------------");
                System.out.println(data.toString());
                JSONObject tmp = new JSONObject((String) data.get(data.length() - 1));
                dietPoint.setText("امتیاز شما از این رژیم : " + tmp.getInt("point"));

                double s_w = tmp.getDouble("startWeight");
                double g_w = tmp.getDouble("goalWeight");

                beginW.setText(String.valueOf(g_w) + " کیلوگرم");
                currentW.setText(String.valueOf(s_w) + " کیلوگرم");

                int s1, s2, s3, s4;
                for (int i = 0; i < 30; i++) {
                    if (i <= finalDay) {
                        JSONObject tt = tmp.getJSONObject(String.valueOf(i + 1));
                        s1 = tt.getJSONObject("0").getInt("status");
                        s2 = tt.getJSONObject("1").getInt("status");
                        s3 = tt.getJSONObject("2").getInt("status");
                        s4 = tt.getJSONObject("3").getInt("status");
                        if (s1 == 0 & s2 == 0 && s3 == 0 && s4 == 0) {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.none));
                            icons[i].setImageResource(0);
                        } else if (s1 == 1 & s2 == 0 && s3 == 0 && s4 == 0) {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                            icons[i].setImageResource(R.drawable.b_1);
                        } else if (s1 == 0 & s2 == 1 && s3 == 0 && s4 == 0) {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                            icons[i].setImageResource(R.drawable.b_2);
                        } else if (s1 == 0 & s2 == 0 && s3 == 1 && s4 == 0) {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                            icons[i].setImageResource(R.drawable.b_3);
                        } else if (s1 == 0 & s2 == 0 && s3 == 0 && s4 == 1) {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                            icons[i].setImageResource(R.drawable.b_4);
                        } else if (s1 == 1 & s2 == 1 && s3 == 0 && s4 == 0) {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                            icons[i].setImageResource(R.drawable.b_5);
                        } else if (s1 == 1 & s2 == 0 && s3 == 1 && s4 == 0) {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                            icons[i].setImageResource(R.drawable.b_6);
                        } else if (s1 == 1 & s2 == 0 && s3 == 0 && s4 == 1) {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                            icons[i].setImageResource(R.drawable.b_7);
                        } else if (s1 == 0 & s2 == 1 && s3 == 1 && s4 == 0) {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                            icons[i].setImageResource(R.drawable.b_8);
                        } else if (s1 == 0 & s2 == 1 && s3 == 0 && s4 == 1) {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                            icons[i].setImageResource(R.drawable.b_9);
                        } else if (s1 == 0 & s2 == 0 && s3 == 1 && s4 == 1) {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                            icons[i].setImageResource(R.drawable.b_10);
                        } else if (s1 == 1 & s2 == 1 && s3 == 1 && s4 == 0) {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                            icons[i].setImageResource(R.drawable.b_11);
                        } else if (s1 == 1 & s2 == 1 && s3 == 0 && s4 == 1) {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                            icons[i].setImageResource(R.drawable.b_12);
                        } else if (s1 == 0 & s2 == 1 && s3 == 1 && s4 == 1) {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.bg));
                            icons[i].setImageResource(R.drawable.b_13);
                        } else if (s1 == 1 & s2 == 1 && s3 == 1 && s4 == 1) {
                            icons[i].setBackground(getResources().getDrawable(R.drawable.full));
                        }
                    } else {
                        icons[i].setBackground(getResources().getDrawable(R.drawable.bg_none));
                        icons[i].setImageResource(0);
                    }
                }

                if (finalDay >= 0 && finalDay < 31) {
                    bLabel.setText("وزن هدف");
                    eLabel.setText("وزن فعلی");
                    dietDay.setText(temp[finalDay]);
                    currentW.setText(String.valueOf(user.getWeight()) + " کیلوگرم");
                    if (user.getWeight() >= s_w) {
                        pb.setProgress(0);
                        secondBar.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10f));
                        barLabel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10f));
                    } else if (user.getWeight() <= g_w) {
                        pb.setProgress(100);
                        secondBar.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0f));
                        barLabel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0f));
                    } else {
                        pb.setProgress((float) ((s_w - user.getWeight()) / (s_w - g_w)) * 100f);
                        secondBar.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, (float) (10 - ((s_w - user.getWeight()) / (s_w - g_w)) * 10)));
                        barLabel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, (float) (10 - ((s_w - user.getWeight()) / (s_w - g_w)) * 10)));
                    }
                } else {
                    bLabel.setText("انتهای رژیم");
                    eLabel.setText("ابتدای رژیم");
                    dietDay.setText("وزن کم شده با این رژیم : " + round((float) (s_w - g_w), 1) + " کیلوگرم");
                    pb.setProgress(100f);
                    secondBar.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10f));
                    barLabel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10f));
                }
                switch (tmp.getString("type")) {
                    case "1000":
                        level.setImageResource(R.drawable.level_3);
                        break;
                    case "1250":
                        level.setImageResource(R.drawable.level_3);
                        break;
                    case "1500":
                        level.setImageResource(R.drawable.level_2);
                        break;
                    case "1750":
                        level.setImageResource(R.drawable.level_2);
                        break;
                    case "2000":
                        level.setImageResource(R.drawable.level_1);
                        break;
                    case "2250":
                        level.setImageResource(R.drawable.level_1);
                        break;
                }
            } else {
                profile_holder.setVisibility(View.GONE);
                nothing.setVisibility(View.VISIBLE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
//                    // Collapsed
//
//
//                } else if (verticalOffset == 0) {
//                    // Expanded
//
//
//                }
//
//            }
//        });

//        ColorFilter filter = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            filter = new LightingColorFilter(getResources().getColor(R.color.colorWhite, getActivity().getTheme()), getResources().getColor(R.color.colorWhite, getActivity().getTheme()));
//
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            filter = new LightingColorFilter(getResources().getColor(R.color.colorWhite), getResources().getColor(R.color.colorWhite));
//        }
//        textList.getCompoundDrawables()[0].setColorFilter(filter);


    }

    public long getDiffDays(Date startDate, Date endDate) {
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;

        return elapsedDays;
    }

    private float calculateBMI(UserInfo user) throws SQLException {
        float height = user.getHeight() / 100;
        return user.getWeight() / (height * height);
    }

    public float round(float value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (float) tmp / factor;
    }
}
