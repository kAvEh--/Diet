package ir.eynakgroup.diet.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.activities.fragments.DietFragment;
import ir.eynakgroup.diet.activities.fragments.PreDietFragment;
import ir.eynakgroup.diet.activities.fragments.ProfileFragment;
import ir.eynakgroup.diet.database.DatabaseHelper;
import ir.eynakgroup.diet.database.tables.Diet;
import ir.eynakgroup.diet.database.tables.UserInfo;
import ir.eynakgroup.diet.utils.AppPreferences;
import ir.eynakgroup.diet.utils.view.CustomTextView;
import ir.eynakgroup.diet.utils.view.CustomViewPager;

/**
 * Created by Shayan on 5/2/2017.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private AppCompatImageView imageProfile;
    private AppCompatImageView imageDiet;
    private View barProfile;
    private View barDiet;
    private CustomViewPager viewPager;

    public static final int SETUP_REQUEST_CODE = 698;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageProfile = (AppCompatImageView) findViewById(R.id.img_profile);
        imageDiet = (AppCompatImageView) findViewById(R.id.img_diet);
        barProfile = findViewById(R.id.bar_tab_profile);
        barDiet = findViewById(R.id.bar_tab_diet);
        viewPager = (CustomViewPager) findViewById(R.id.container_fragment);

        if(getAppPreferences().hasDiet()){
            try {
                QueryBuilder<Diet, Integer> dietQueryBuilder = getDBHelper().getDietDao().queryBuilder();
                dietQueryBuilder.where().eq("_id", getAppPreferences().getDietNumber());
                List<Diet> dietList = dietQueryBuilder.query();
                if(dietList.size() > 0){
                    long diff = Calendar.getInstance().getTimeInMillis() - Long.parseLong(dietList.get(0).getStartDate());
                    int day = (int) (diff/TimeUnit.DAYS.toMillis(1));
                    if(day > 30)
                        getAppPreferences().setAlreadyDiet(false);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            try {
                QueryBuilder<Diet, Integer> dietQueryBuilder = getDBHelper().getDietDao().queryBuilder();
                dietQueryBuilder.where().eq("_id", getAppPreferences().getDietNumber());
                List<Diet> dietList = dietQueryBuilder.query();
                if(dietList.size() > 0){
                    long diff = Calendar.getInstance().getTimeInMillis() - Long.parseLong(dietList.get(0).getStartDate());
                    int day = (int) (diff/TimeUnit.DAYS.toMillis(1));
                    if(day > 0 && day <= 30)
                        getAppPreferences().setAlreadyDiet(true);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), this));

        findViewById(R.id.tab_profile).setOnClickListener(this);
        findViewById(R.id.tab_diet).setOnClickListener(this);

//        Window window = getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            window.setStatusBarColor(getResources().getColor(R.color.colorStatusBar, this.getTheme()));
//
//        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            window.setStatusBarColor(getResources().getColor(R.color.colorStatusBar));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
//                    setLightStatusBar();
                    decorateStatusBar();


                } else {
                    clearLightStatusBar();
                    Fragment profile = ((FragmentStatePagerAdapter) viewPager.getAdapter()).getItem(position);
                    if (profile != null) {
//                        float goalWeight = 0.0f;
//                        if(goalWeight != getAppPreferences().getGoalWeight())
//                            ((CustomTextView)profile.getView().findViewById(R.id.txt_goal_weight)).setText(round(goalWeight, 1)+"");

                        try {
                            UserInfo user = getDBHelper().getUserDao().queryForAll().get(0);
                            ((CustomTextView) profile.getView().findViewById(R.id.txt_credit)).setText(user.getCredit() + "");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        decorateStatusBar();

    }

    public float round(float value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (float) tmp / factor;
    }

    private void decorateStatusBar() {
        if (getAppPreferences().hasDiet()) {
            clearLightStatusBar();
        } else {
            setLightStatusBar();
        }
    }

    private void setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = getWindow().getDecorView();
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
    }

    private void clearLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = getWindow().getDecorView();
            int flags = view.getSystemUiVisibility();
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_diet:
                if (viewPager.getCurrentItem() != 0) {
                    imageProfile.setImageResource(R.drawable.icn_tab_profile);
                    imageDiet.setImageResource(R.drawable.icn_tab_diet_selected);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        barDiet.setBackgroundColor(getResources().getColor(R.color.colorPrimary, getTheme()));
                        barProfile.setBackgroundColor(getResources().getColor(R.color.colorGrey, getTheme()));
                    } else {
                        barDiet.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        barProfile.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                    }

                    viewPager.setCurrentItem(0);
                }
                break;

            case R.id.tab_profile:

                if (viewPager.getCurrentItem() != 1) {
                    imageProfile.setImageResource(R.drawable.icn_tab_profile_selected);
                    imageDiet.setImageResource(R.drawable.icn_tab_diet);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        barProfile.setBackgroundColor(getResources().getColor(R.color.colorPrimary, getTheme()));
                        barDiet.setBackgroundColor(getResources().getColor(R.color.colorGrey, getTheme()));
                    } else {
                        barProfile.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        barDiet.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                    }
                    viewPager.setCurrentItem(1);
                }

                break;
            default:

        }
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        private Context mContext;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    if (getAppPreferences().hasDiet())
                        return DietFragment.newInstance(mContext);
                    else
                        return PreDietFragment.newInstance(mContext);


                case 1:
                    return ProfileFragment.newInstance(mContext);

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    if (getAppPreferences().hasDiet())
                        return DietFragment.TAG;
                    else
                        return PreDietFragment.TAG;

                case 1:
                    return ProfileFragment.TAG;

                default:
                    return null;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETUP_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (requestCode == SETUP_REQUEST_CODE) {
                    System.out.println("------------------- result");
                    getAppPreferences().setAlreadyDiet(true);
                    decorateStatusBar();
                    viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), this));

                }
            } else if (resultCode == RESULT_CANCELED) {
                if (requestCode == SETUP_REQUEST_CODE) {

                }
            }
        }
    }

}
