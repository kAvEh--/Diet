package ir.eynakgroup.diet.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.activities.fragments.dummy.DummyDish;
import ir.eynakgroup.diet.activities.fragments.dummy.DummyFood;
import ir.eynakgroup.diet.database.DatabaseHelper;
import ir.eynakgroup.diet.database.tables.Diet;
import ir.eynakgroup.diet.database.tables.Food;
import ir.eynakgroup.diet.database.tables.FoodPackage;
import ir.eynakgroup.diet.database.tables.FoodUnit;
import ir.eynakgroup.diet.database.tables.PackageFood;
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
    private AppPreferences appPreference;
    private DatabaseHelper databaseHelper;

    boolean notToday = false;
    static Day currentDay = Day.TODAY;

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
        loadDishes();
        /**
         * meal tabs part
         */
        viewPager = (ViewPager) view.findViewById(R.id.meals_tab_viewpager);
        setupViewPager();

        tabLayout = (TabLayout) view.findViewById(R.id.meals_tabs);
        tabLayout.setupWithViewPager(viewPager);
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
                List<Fragment> fragmentList = ((ViewPagerAdapter) viewPager.getAdapter()).getFragmentList();
                switch (position) {
                    case TODAY:
                        if (notToday) {
                            ((DietDinnerFragment) fragmentList.get(0)).updateDishes(Day.TODAY);
                            ((DietSnackFragment) fragmentList.get(1)).updateDishes(Day.TODAY);
                            ((DietLunchFragment) fragmentList.get(2)).updateDishes(Day.TODAY);
                            ((DietBreakfastFragment) fragmentList.get(3)).updateDishes(Day.TODAY);
                            notToday = false;
                            currentDay = Day.TODAY;
                        }
                        break;
                    case TOMORROW:
                        ((DietDinnerFragment) fragmentList.get(0)).updateDishes(Day.TOMORROW);
                        ((DietSnackFragment) fragmentList.get(1)).updateDishes(Day.TOMORROW);
                        ((DietLunchFragment) fragmentList.get(2)).updateDishes(Day.TOMORROW);
                        ((DietBreakfastFragment) fragmentList.get(3)).updateDishes(Day.TOMORROW);
                        notToday = true;
                        currentDay = Day.TOMORROW;
                        break;
                    case DAY_AFTER_TOMORROW:
                        ((DietDinnerFragment) fragmentList.get(0)).updateDishes(Day.DAY_AFTER_TOMORROW);
                        ((DietSnackFragment) fragmentList.get(1)).updateDishes(Day.DAY_AFTER_TOMORROW);
                        ((DietLunchFragment) fragmentList.get(2)).updateDishes(Day.DAY_AFTER_TOMORROW);
                        ((DietBreakfastFragment) fragmentList.get(3)).updateDishes(Day.DAY_AFTER_TOMORROW);
                        notToday = true;
                        currentDay = Day.DAY_AFTER_TOMORROW;
                        break;
                }
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

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        Map<Day, List<DummyDish>> dinnerDishes = new HashMap<>();
        dinnerDishes.put(Day.TODAY, todayDinnerDishes);
        dinnerDishes.put(Day.TOMORROW, tomorrowDinnerDishes);
        dinnerDishes.put(Day.DAY_AFTER_TOMORROW, afterDinnerDishes);

        Map<Day, List<DummyDish>> lunchDishes = new HashMap<>();
        lunchDishes.put(Day.TODAY, todayLunchDishes);
        lunchDishes.put(Day.TOMORROW, tomorrowLunchDishes);
        lunchDishes.put(Day.DAY_AFTER_TOMORROW, afterLunchDishes);

        Map<Day, List<DummyDish>> snackDishes = new HashMap<>();
        snackDishes.put(Day.TODAY, todaySnackDishes);
        snackDishes.put(Day.TOMORROW, tomorrowSnackDishes);
        snackDishes.put(Day.DAY_AFTER_TOMORROW, afterSnackDishes);

        Map<Day, List<DummyDish>> breakfastDishes = new HashMap<>();
        breakfastDishes.put(Day.TODAY, todayBreakfastDishes);
        breakfastDishes.put(Day.TOMORROW, tomorrowBreakfastDishes);
        breakfastDishes.put(Day.DAY_AFTER_TOMORROW, afterBreakfastDishes);

        adapter.addFragment(DietDinnerFragment.newInstance(DINNER, dinnerDishes), getString(R.string.dinner));
        adapter.addFragment(DietSnackFragment.newInstance(SNACK, snackDishes), getString(R.string.snack));
        adapter.addFragment(DietLunchFragment.newInstance(LUNCH, lunchDishes), getString(R.string.lunch));
        adapter.addFragment(DietBreakfastFragment.newInstance(BREAKFAST, breakfastDishes), getString(R.string.breakfast));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
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
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

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

        public List<Fragment> getFragmentList() {
            return mFragmentList;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    private List<DummyDish> todayBreakfastDishes = new ArrayList<>();
    private List<DummyDish> todayLunchDishes = new ArrayList<>();
    private List<DummyDish> todayDinnerDishes = new ArrayList<>();
    private List<DummyDish> todaySnackDishes = new ArrayList<>();

    private List<DummyDish> tomorrowBreakfastDishes = new ArrayList<>();
    private List<DummyDish> tomorrowLunchDishes = new ArrayList<>();
    private List<DummyDish> tomorrowDinnerDishes = new ArrayList<>();
    private List<DummyDish> tomorrowSnackDishes = new ArrayList<>();

    private List<DummyDish> afterBreakfastDishes = new ArrayList<>();
    private List<DummyDish> afterLunchDishes = new ArrayList<>();
    private List<DummyDish> afterDinnerDishes = new ArrayList<>();
    private List<DummyDish> afterSnackDishes = new ArrayList<>();

    private QueryBuilder<FoodPackage, Integer> packageQueryBuilder;
    private List<FoodPackage> packageList;
    private QueryBuilder<Diet, Integer> dietQueryBuilder;
    private List<Diet> dietList;
    private List<Diet> dietDayList;
    private QueryBuilder<Food, Integer> foodQueryBuilder;
    private List<Food> foods;
    private String[] foodsId;
    private QueryBuilder<PackageFood, Integer> packageFoodQueryBuilder;
    private List<PackageFood> packageFoods;
    private String[] packagesId;
    private String amount;
    private QueryBuilder<FoodUnit, Integer> foodUnitQueryBuilder;
    private List<FoodUnit> foodUnits;

    private void loadDishes() {
        timeToShow = Calendar.getInstance().getTimeInMillis();
        appPreference = new AppPreferences(getContext());
        databaseHelper = new DatabaseHelper(getContext());
        int dietId = appPreference.getDietNumber();
        try {
            dietQueryBuilder = databaseHelper.getDietDao().queryBuilder();
            dietQueryBuilder.where().eq("_id", dietId);
            dietList = dietQueryBuilder.query();
            packageQueryBuilder = databaseHelper.getFoodPackageDao().queryBuilder();
            foodQueryBuilder = databaseHelper.getFoodDao().queryBuilder();
            packageFoodQueryBuilder = databaseHelper.getPackageFoodDao().queryBuilder();
            foodUnitQueryBuilder = databaseHelper.getFoodUnitDao().queryBuilder();
            if (dietList.size() > 0) {
                int today = (int) ((timeToShow - Long.parseLong(dietList.get(0).getStartDate())) / TimeUnit.DAYS.toMillis(1)) + 1;
                String dietType = dietList.get(0).getDietType().trim();
                Log.d("DAY", today + "");
                for (int day = today - 1; day <= today + 2; day++) {
                    dietQueryBuilder.where().eq("day", day);
                    dietDayList = dietQueryBuilder.query();
                    if (dietDayList.size() > 0) {
                        if (day == today - 1) {
                            packagesId = new String[]{dietDayList.get(0).getSelectedBreakfast(), dietDayList.get(0).getSelectedLunch(), dietDayList.get(0).getSelectedSnack(), dietDayList.get(0).getSelectedDinner()};
                        } else
                            packagesId = new String[]{dietDayList.get(0).getBreakfastPack1(), dietDayList.get(0).getBreakfastPack2(), dietDayList.get(0).getBreakfastPack3(), dietDayList.get(0).getLunchPack1(), dietDayList.get(0).getLunchPack2(), dietDayList.get(0).getLunchPack3(), dietDayList.get(0).getSnackPack1(), dietDayList.get(0).getSnackPack2(), dietDayList.get(0).getSnackPack3(), dietDayList.get(0).getDinnerPack1(), dietDayList.get(0).getDinnerPack2(), dietDayList.get(0).getDinnerPack3()};

                        for (int i = 0; i < packagesId.length; i++) {
                            packageQueryBuilder.where().eq("_id", packagesId[i]);
                            packageList = packageQueryBuilder.query();
                            if (packageList.size() > 0) {
                                DummyDish dish = new DummyDish();
                                foodsId = packageList.get(0).getFoods().split(",");
                                for (String foodId : foodsId) {
                                    packageFoodQueryBuilder.where().eq("_id", packagesId[i]).and().eq("foodId", foodId.trim());
                                    packageFoods = packageFoodQueryBuilder.query();
                                    if (packageFoods.size() > 0) {
                                        amount = "0";
                                        switch (dietType) {
                                            case "1000":
                                                amount = packageFoods.get(0).getAmount1000();
                                                break;
                                            case "1250":
                                                amount = packageFoods.get(0).getAmount1250();
                                                break;
                                            case "1500":
                                                amount = packageFoods.get(0).getAmount1500();
                                                break;
                                            case "1750":
                                                amount = packageFoods.get(0).getAmount1750();
                                                break;
                                            case "2000":
                                                amount = packageFoods.get(0).getAmount2000();
                                                break;
                                            case "2250":
                                                amount = packageFoods.get(0).getAmount2250();
                                                break;
                                        }

                                        if (!amount.equalsIgnoreCase("0")) {
                                            DummyFood food = new DummyFood();
                                            food.setAmount(amount);
                                            foodQueryBuilder.where().eq("foodId", Integer.valueOf(foodId.trim()));
                                            foods = foodQueryBuilder.query();
                                            if (foods.size() > 0) {
                                                food.setFoodName(foods.get(0).getFoodName());
                                                foodUnitQueryBuilder.where().eq("Unit_ID", foods.get(0).getUnitId());
                                                foodUnits = foodUnitQueryBuilder.query();
                                                if (foodUnits.size() > 0)
                                                    food.setUnit(foodUnits.get(0).getUnitName());

                                                foodUnitQueryBuilder.reset();
                                            }
                                            dish.addFood(food);
                                            foodQueryBuilder.reset();
                                        }

                                    }
                                    packageFoodQueryBuilder.reset();
                                }

                                if (day == today - 1) {
                                    dish.setDay(Day.YESTERDAY);
                                    dish.setDishNumber(3);
                                    switch (i) {
                                        case 0:
                                            todayBreakfastDishes.add(dish);
                                            break;
                                        case 1:
                                            todayLunchDishes.add(dish);
                                            break;
                                        case 2:
                                            todaySnackDishes.add(dish);
                                            break;
                                        case 3:
                                            todayDinnerDishes.add(dish);
                                            break;
                                    }

                                } else if (day == today) {
                                    dish.setDay(Day.TODAY);
                                    dish.setDishNumber(i%3);
                                    switch (i / 3) {
                                        case 0:
                                            todayBreakfastDishes.add(dish);
                                            break;
                                        case 1:
                                            todayLunchDishes.add(dish);
                                            break;
                                        case 2:
                                            todaySnackDishes.add(dish);
                                            break;
                                        case 3:
                                            todayDinnerDishes.add(dish);
                                            break;
                                    }
                                } else if (day == today + 1) {
                                    dish.setDay(Day.TOMORROW);
                                    dish.setDishNumber(i%3);
                                    switch (i / 3) {
                                        case 0:
                                            tomorrowBreakfastDishes.add(dish);
                                            break;
                                        case 1:
                                            tomorrowLunchDishes.add(dish);
                                            break;
                                        case 2:
                                            tomorrowSnackDishes.add(dish);
                                            break;
                                        case 3:
                                            tomorrowDinnerDishes.add(dish);
                                            break;
                                    }
                                } else if (day == today + 2) {
                                    dish.setDay(Day.DAY_AFTER_TOMORROW);
                                    dish.setDishNumber(i%3);
                                    switch (i / 3) {
                                        case 0:
                                            afterBreakfastDishes.add(dish);
                                            break;
                                        case 1:
                                            afterLunchDishes.add(dish);
                                            break;
                                        case 2:
                                            afterSnackDishes.add(dish);
                                            break;
                                        case 3:
                                            afterDinnerDishes.add(dish);
                                            break;
                                    }
                                }


                            }
                            packageQueryBuilder.reset();
                        }


                    }
                    dietQueryBuilder.reset();
                }


            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public enum Day {
        YESTERDAY,
        TODAY,
        TOMORROW,
        DAY_AFTER_TOMORROW
    }

}
