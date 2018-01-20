package ir.eynakgroup.diet.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.activities.fragments.ActivityDialog;
import ir.eynakgroup.diet.activities.fragments.GenderDialog;
import ir.eynakgroup.diet.activities.fragments.WeightDialog;
import ir.eynakgroup.diet.activities.fragments.HeightDialog;
import ir.eynakgroup.diet.database.tables.UserInfo;
import ir.eynakgroup.diet.utils.JDF;
import ir.eynakgroup.diet.utils.persiandatepicker.Listener;
import ir.eynakgroup.diet.utils.persiandatepicker.PersianDatePickerDialog;
import ir.eynakgroup.diet.utils.persiandatepicker.util.PersianCalendar;

/**
 * Created by Eynak_PC2 on 11/11/2017.
 */

public class SettingActivity extends BaseActivity {

    TextView weight;
    TextView height;
    TextView bdate;
    TextView active;
    TextView sex;
    TextView logout;

    UserInfo user;
    private SettingActivity mThis = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("تنظیمات");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        weight = (TextView) findViewById(R.id.setting_weight);
        height = (TextView) findViewById(R.id.setting_height);
        bdate = (TextView) findViewById(R.id.setting_bdate);
        active = (TextView) findViewById(R.id.setting_activity);
        sex = (TextView) findViewById(R.id.setting_sex);
        logout = (TextView) findViewById(R.id.setting_logout);

        try {
            user = getDBHelper().getUserDao().queryForAll().get(0);
            weight.setText(user.getWeight() + " kg");
            height.setText(user.getHeight() + " cm");
            bdate.setText(parseJavascriptDate(user.getBirthday()));
            active.setText(convertactivity(user.getActivityLevel()));
            sex.setText(convertgender(user.getGender()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        bdate.setOnClickListener(new View.OnClickListener() {
            @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                PersianDatePickerDialog picker = new PersianDatePickerDialog(mThis)
                        .setPositiveButtonString("باشه")
                        .setNegativeButton("بی‌خیال")
                        .setTodayButton("امروز")
                        .setTodayButtonVisible(true)
                        .setInitDate(parseJavascriptDate2(user.getBirthday()))
                        .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                        .setMinYear(1300)
                        .setActionTextColor(getResources().getColor(R.color.colorBreakfast))
                        .setListener(new Listener() {
                            @Override
                            public void onDateSelected(PersianCalendar persianCalendar) {
                                JDF jdf = new JDF();
                                jdf.setIranianDate(persianCalendar.getPersianYear(), persianCalendar.getPersianMonth(), persianCalendar.getPersianDay());
                                try {
                                    UpdateBuilder<UserInfo, Integer> updateBuilder = getDBHelper().getUserDao().updateBuilder();
                                    updateBuilder.updateColumnValue("Birthday", jdf.getGregorianYear() + "-" + jdf.getGregorianMonth() + "-" + jdf.getGregorianDay() + "T00:00:00Z");
                                    updateBuilder.update();
                                    user.setBirthday(jdf.getGregorianYear() + "-" + jdf.getGregorianMonth() + "-" + jdf.getGregorianDay() + "T00:00:00Z");
                                    bdate.setText(jdf.getIranianYear() + "/" + jdf.getIranianMonth() + "/" + jdf.getIranianDay());
                                    sendDatatoServer(user);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onDismissed() {

                            }
                        });

                picker.show();
            }
        });

        weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeightDialog dialog = new WeightDialog();
                dialog.set_w((float) user.getWeight());
                dialog.setUser(user);
                dialog.show(getSupportFragmentManager(), "missiles");
            }
        });

        height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeightDialog dialog = new HeightDialog();
                dialog.setHeight((int) user.getHeight());
                dialog.setUser(user);
                dialog.show(getSupportFragmentManager(), "missiles");
            }
        });

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDialog dialog = new ActivityDialog();
                dialog.setActLevel(user.getActivityLevel());
                dialog.setUser(user);
                dialog.show(getSupportFragmentManager(), "missiles");
            }
        });

        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenderDialog dialog = new GenderDialog();
                dialog.setGender(user.getGender());
                dialog.setUser(user);
                dialog.show(getSupportFragmentManager(), "missiles");
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this).create();
                alertDialog.setMessage(getResources().getString(R.string.logout_message));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "بله",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    DeleteBuilder updateBuilder = getDBHelper().getUserDao().deleteBuilder();
                                    updateBuilder.delete();
                                    startActivityForResult(new Intent(SettingActivity.this, RegisterActivity.class), 123);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "لغو",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    public void changeWeight(String newWeight) {
        try {
            UpdateBuilder<UserInfo, Integer> updateBuilder = getDBHelper().getUserDao().updateBuilder();
            updateBuilder.updateColumnValue("Weight", newWeight);
            updateBuilder.update();
            user.setWeight(newWeight);
            weight.setText(newWeight + " kg");
            sendDatatoServer(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeActivity(int level) {
        try {
            UpdateBuilder<UserInfo, Integer> updateBuilder = getDBHelper().getUserDao().updateBuilder();
            updateBuilder.updateColumnValue("Activity_Level", level);
            updateBuilder.update();
            user.setActivityLevel(level);
            active.setText(convertactivity(level));
            sendDatatoServer(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeHight(String newHeight) {
        try {
            UpdateBuilder<UserInfo, Integer> updateBuilder = getDBHelper().getUserDao().updateBuilder();
            updateBuilder.updateColumnValue("Height", newHeight);
            updateBuilder.update();
            user.setHeight(newHeight);
            height.setText(newHeight + " cm");
            sendDatatoServer(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeGender(int g) {
        try {
            UpdateBuilder<UserInfo, Integer> updateBuilder = getDBHelper().getUserDao().updateBuilder();
            updateBuilder.updateColumnValue("Gender", g);
            updateBuilder.update();
            user.setGender(g);
            sex.setText(convertgender(g));
            sendDatatoServer(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String convertactivity(int a) {
        switch (a) {
            case 1:
                return "خیلی کم";
            case 2:
                return "کم";
            case 3:
                return "متوسط";
            case 4:
                return "زیاد";
            default:
                return "خیلی زیاد";
        }
    }

    private String convertgender(int gender) {
        switch (gender) {
            case 0:
                return "مرد";
            default:
                return "زن";
        }
    }

    private Date convertStringtoDate(String dtStart) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = format.parse(dtStart);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    private String parseJavascriptDate(String time) {
        if (!(time == null || time.matches("null") || time.matches(""))) {
            String date = time.split("T")[0];
            String yearMonthDay[] = date.split("-");
            int year = Integer.parseInt(yearMonthDay[0]);
            int month = Integer.parseInt(yearMonthDay[1]);
            int day = Integer.parseInt(yearMonthDay[2]);
            return (new JDF(year, month, day)).getIranianDate();

        } else {
            return "";
        }
    }

    private PersianCalendar parseJavascriptDate2(String time) {
        if (!(time == null || time.matches("null") || time.matches(""))) {
            System.out.println(time + "------------------------------------");
            String date = time.split("T")[0];
            String yearMonthDay[] = date.split("-");
            int year = Integer.parseInt(yearMonthDay[0]);
            int month = Integer.parseInt(yearMonthDay[1]);
            int day = Integer.parseInt(yearMonthDay[2]);
            PersianCalendar initDate = new PersianCalendar();
            JDF j = new JDF();
            j.setGregorianDate(year, month, day);
            System.out.println(j.getIranianYear() + "....." + j.getIranianMonth());
            initDate.setPersianDate(j.getIranianYear(), j.getIranianMonth(), j.getIranianDay());
            return initDate;

        } else {
            return null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
