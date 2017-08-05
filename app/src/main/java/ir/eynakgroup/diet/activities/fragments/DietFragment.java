package ir.eynakgroup.diet.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.TimeUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.TextView;

import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.database.DatabaseHelper;
import ir.eynakgroup.diet.database.tables.Diet;
import ir.eynakgroup.diet.database.tables.Food;
import ir.eynakgroup.diet.database.tables.FoodPackage;
import ir.eynakgroup.diet.utils.AppPreferences;


public class DietFragment extends Fragment {

    private static final int BREAKFAST = 0;
    private static final int LUNCH = 1;
    private static final int SNACK = 2;
    private static final int DINNER = 3;

    private static final int TODAY = 0;
    private static final int TOMORROW = 1;
    private static final int DAY_AFTER_TOMORROW = 2;

    private long timeToShow;

    private static DietFragment mDietFragmentInstance = null;
    public static final String TAG = "FRAGMENT_DIET";
    private Context mContext;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AppCompatSpinner date_spinner;

    public DietFragment(Context context) {
        mContext = context;
    }

//    public static DietFragment getInstance(Context context) {
//        if (mDietFragmentInstance == null)
//            mDietFragmentInstance = new DietFragment(context);
//
//        return mDietFragmentInstance;
//    }

    public static DietFragment newInstance(Context context) {
        if (mDietFragmentInstance == null)
            mDietFragmentInstance = new DietFragment(context);

        return mDietFragmentInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timeToShow = Calendar.getInstance().getTimeInMillis();
        /**
         * meal tabs part
         */
        viewPager = (ViewPager) view.findViewById(R.id.meals_tab_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.meals_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.colorDinner));
                        break;
                    case 1:
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.colorSnack));
                        break;
                    case 2:
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.colorLunch));
                        break;
                    case 3:
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.colorBreakfast));
                        break;
                    default:
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /**
         * date spinner part
         */
        date_spinner = (AppCompatSpinner) view.findViewById(R.id.date_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                R.array.date_spinner_array, R.layout.date_spinner_view);
        adapter.setDropDownViewResource(R.layout.date_spinner_item);
        date_spinner.setAdapter(adapter);
        date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case TODAY:
                        timeToShow = Calendar.getInstance().getTimeInMillis();
                        break;
                    case TOMORROW:
                        timeToShow = Calendar.getInstance().getTimeInMillis() + TimeUnit.DAYS.toMillis(1);
                        break;
                    case DAY_AFTER_TOMORROW:
                        timeToShow = Calendar.getInstance().getTimeInMillis() + TimeUnit.DAYS.toMillis(2);
                        break;
                }
                inflateDiet();
                //TODO do somt hing, date is changed.

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selectPage(3);
    }

    private void selectPage(int pageIndex) {
        tabLayout.setScrollPosition(pageIndex, 0f, true);
        viewPager.setCurrentItem(pageIndex);
    }

    private void inflateDiet() {
        FragmentStatePagerAdapter pagerAdapter = (FragmentStatePagerAdapter) viewPager.getAdapter();
        View mealView = pagerAdapter.getItem(viewPager.getCurrentItem()).getView();
        if (mealView != null) {
            try {
                int dietId = new AppPreferences(getContext()).getDietNumber();
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                QueryBuilder<Diet, Integer> dietQueryBuilder = databaseHelper.getDietDao().queryBuilder();
                dietQueryBuilder.where().eq("_id", dietId);
                List<Diet> dietList = dietQueryBuilder.query();
//                int day = getDietDay(timeToShow - Long.parseLong(dietList.get(0).getStartDate()));
                if (dietList.size() > 0) {
                    int day = (int) ((timeToShow - Long.parseLong(dietList.get(0).getStartDate())) / TimeUnit.DAYS.toMillis(1)) + 1;
                    Log.d("DAY", day + "");
                    dietQueryBuilder.reset();
                    dietQueryBuilder.where().eq("day", day);
                    List<Diet> dietDayList = dietQueryBuilder.query();
                    if (dietDayList.size() > 0) {
                        Diet diet = dietDayList.get(0);
                        QueryBuilder<FoodPackage, Integer> packageQueryBuilder = databaseHelper.getFoodPackageDao().queryBuilder();
                        switch (viewPager.getCurrentItem()){
                            case 0:
                                packageQueryBuilder.where().eq("_id", diet.getDinnerPack1());
                                break;
                            case 1:
                                packageQueryBuilder.where().eq("_id", diet.getSnackPack1());
                                break;
                            case 2:
                                packageQueryBuilder.where().eq("_id", diet.getLunchPack1());
                                break;
                            case 3:
                                packageQueryBuilder.where().eq("_id", diet.getBreakfastPack1());
                                break;
                        }

                        List<FoodPackage> packageList = packageQueryBuilder.query();
                        if (packageList.size() > 0) {
                            FoodPackage pack1 = packageList.get(0);
                            String[] foodsId = pack1.getFoods().split(",");
                            QueryBuilder<Food, Integer> foodQueryBuilder = databaseHelper.getFoodDao().queryBuilder();
                            int position = 1;
                            for (String foodId : foodsId) {
                                foodQueryBuilder.where().eq("foodId", Integer.valueOf(foodId.trim()));
                                List<Food> foods = foodQueryBuilder.query();
                                if (foods.size() > 0) {
                                    Food food = foods.get(0);
                                    System.out.println("food ------------------------------ " + food.getFoodName());
                                    switch (position) {
                                        case 1:
                                            ((TextView) mealView.findViewById(R.id.pack1_item1)).setText(food.getFoodName().trim());
                                            break;
                                        case 2:
                                            ((TextView) mealView.findViewById(R.id.pack1_item2)).setText(food.getFoodName().trim());
                                            break;
                                        case 3:
                                            ((TextView) mealView.findViewById(R.id.pack1_item3)).setText(food.getFoodName().trim());
                                            break;
                                        case 4:
                                            ((TextView) mealView.findViewById(R.id.pack1_item4)).setText(food.getFoodName().trim());
                                            break;
                                        case 5:
                                            ((TextView) mealView.findViewById(R.id.pack1_item5)).setText(food.getFoodName().trim());
                                            break;
                                    }
                                    position++;
                                }
                                foodQueryBuilder.reset();

                            }
                        }
                    }


                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private int getDietDay(long diff) {
        if (diff >= 0 && diff < TimeUnit.DAYS.toMillis(1))
            return 1;
        else if (diff >= TimeUnit.DAYS.toMillis(1) && diff < TimeUnit.DAYS.toMillis(2))
            return 2;
        else if (diff >= TimeUnit.DAYS.toMillis(2) && diff < TimeUnit.DAYS.toMillis(3))
            return 3;
        else if (diff >= TimeUnit.DAYS.toMillis(3) && diff < TimeUnit.DAYS.toMillis(4))
            return 4;
        else if (diff >= TimeUnit.DAYS.toMillis(4) && diff < TimeUnit.DAYS.toMillis(5))
            return 5;
        else if (diff >= TimeUnit.DAYS.toMillis(5) && diff < TimeUnit.DAYS.toMillis(6))
            return 6;
        else if (diff >= TimeUnit.DAYS.toMillis(6) && diff < TimeUnit.DAYS.toMillis(7))
            return 7;
        else if (diff >= TimeUnit.DAYS.toMillis(7) && diff < TimeUnit.DAYS.toMillis(8))
            return 8;
        else if (diff >= TimeUnit.DAYS.toMillis(8) && diff < TimeUnit.DAYS.toMillis(9))
            return 9;
        else if (diff >= TimeUnit.DAYS.toMillis(9) && diff < TimeUnit.DAYS.toMillis(10))
            return 10;
        else if (diff >= TimeUnit.DAYS.toMillis(10) && diff < TimeUnit.DAYS.toMillis(11))
            return 11;
        else if (diff >= TimeUnit.DAYS.toMillis(11) && diff < TimeUnit.DAYS.toMillis(12))
            return 12;
        else if (diff >= TimeUnit.DAYS.toMillis(12) && diff < TimeUnit.DAYS.toMillis(13))
            return 13;
        else if (diff >= TimeUnit.DAYS.toMillis(13) && diff < TimeUnit.DAYS.toMillis(14))
            return 14;
        else if (diff >= TimeUnit.DAYS.toMillis(14) && diff < TimeUnit.DAYS.toMillis(15))
            return 15;
        else if (diff >= TimeUnit.DAYS.toMillis(15) && diff < TimeUnit.DAYS.toMillis(16))
            return 16;
        else if (diff >= TimeUnit.DAYS.toMillis(16) && diff < TimeUnit.DAYS.toMillis(17))
            return 17;
        else if (diff >= TimeUnit.DAYS.toMillis(17) && diff < TimeUnit.DAYS.toMillis(18))
            return 18;
        else if (diff >= TimeUnit.DAYS.toMillis(18) && diff < TimeUnit.DAYS.toMillis(19))
            return 19;
        else if (diff >= TimeUnit.DAYS.toMillis(19) && diff < TimeUnit.DAYS.toMillis(20))
            return 20;
        else if (diff >= TimeUnit.DAYS.toMillis(20) && diff < TimeUnit.DAYS.toMillis(21))
            return 21;
        else if (diff >= TimeUnit.DAYS.toMillis(21) && diff < TimeUnit.DAYS.toMillis(22))
            return 22;
        else if (diff >= TimeUnit.DAYS.toMillis(22) && diff < TimeUnit.DAYS.toMillis(23))
            return 23;
        else if (diff >= TimeUnit.DAYS.toMillis(23) && diff < TimeUnit.DAYS.toMillis(24))
            return 24;
        else if (diff >= TimeUnit.DAYS.toMillis(24) && diff < TimeUnit.DAYS.toMillis(25))
            return 25;
        else if (diff >= TimeUnit.DAYS.toMillis(25) && diff < TimeUnit.DAYS.toMillis(26))
            return 26;
        else if (diff >= TimeUnit.DAYS.toMillis(26) && diff < TimeUnit.DAYS.toMillis(27))
            return 27;
        else if (diff >= TimeUnit.DAYS.toMillis(27) && diff < TimeUnit.DAYS.toMillis(28))
            return 28;
        else if (diff >= TimeUnit.DAYS.toMillis(28) && diff < TimeUnit.DAYS.toMillis(29))
            return 29;
        else if (diff >= TimeUnit.DAYS.toMillis(29) && diff < TimeUnit.DAYS.toMillis(30))
            return 30;
        else if (diff >= TimeUnit.DAYS.toMillis(30) && diff < TimeUnit.DAYS.toMillis(31))
            return 31;
        else
            return 0;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(DietDishesFragment.newInstance(DINNER, R.layout.fragment_dishes_dinner), getString(R.string.dinner));
        adapter.addFragment(DietDishesFragment.newInstance(SNACK, R.layout.fragment_dishes_snack), getString(R.string.snack));
        adapter.addFragment(DietDishesFragment.newInstance(LUNCH, R.layout.fragment_dishes_lunch), getString(R.string.lunch));
        adapter.addFragment(DietDishesFragment.newInstance(BREAKFAST, R.layout.fragment_dishes_breakfast), getString(R.string.breakfast));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
