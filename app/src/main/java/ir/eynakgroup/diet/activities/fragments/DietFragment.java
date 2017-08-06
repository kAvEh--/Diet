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
        appPreference = new AppPreferences(getContext());

        databaseHelper = new DatabaseHelper(getContext());
        /**
         * meal tabs part
         */
        viewPager = (ViewPager) view.findViewById(R.id.meals_tab_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.meals_tabs);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

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

    private QueryBuilder<FoodPackage, Integer> packageQueryBuilder;
    private List<FoodPackage> packageList;
    private ViewPagerAdapter pagerAdapter;
    private View mealView;
    private QueryBuilder<Diet, Integer> dietQueryBuilder;
    private List<Diet> dietList;
    private List<Diet> dietDayList;
    private QueryBuilder<Food, Integer> foodQueryBuilder;
    private List<Food> foods;
    private String[] foodsId;
    private int position;
    private QueryBuilder<PackageFood, Integer> packageFoodQueryBuilder;
    private List<PackageFood> packageFoods;
    private String[] packagesId;
    private String amount;

    private void inflateDiet() {
        pagerAdapter = (ViewPagerAdapter) viewPager.getAdapter();
        mealView = pagerAdapter.getItem(viewPager.getCurrentItem()).getView();
        if (mealView != null) {
            try {
                int dietId = appPreference.getDietNumber();
                dietQueryBuilder = databaseHelper.getDietDao().queryBuilder();
                dietQueryBuilder.where().eq("_id", dietId);
                dietList = dietQueryBuilder.query();
//                int day = getDietDay(timeToShow - Long.parseLong(dietList.get(0).getStartDate()));
                if (dietList.size() > 0) {
                    int day = (int) ((timeToShow - Long.parseLong(dietList.get(0).getStartDate())) / TimeUnit.DAYS.toMillis(1)) + 1;
                    String dietType = dietList.get(0).getDietType().trim();
                    Log.d("DAY", day + "");
                    dietQueryBuilder.reset();
                    dietQueryBuilder.where().eq("day", day);
                    dietDayList = dietQueryBuilder.query();
                    if (dietDayList.size() > 0) {
//                        Diet diet = dietDayList.get(0);
                        packagesId = new String[4];
                        switch (viewPager.getCurrentItem()){
                            case 0:
                                packagesId[0] = dietDayList.get(0).getDinnerPack1();
                                packagesId[1] = dietDayList.get(0).getDinnerPack2();
                                packagesId[2] = dietDayList.get(0).getDinnerPack3();
                                if(day > 1){
                                    dietQueryBuilder.reset();
                                    dietQueryBuilder.where().eq("day", day - 1);
                                    dietDayList = dietQueryBuilder.query();
                                    if (dietDayList.size() > 0) {
                                        mealView.findViewById(R.id.yesterday_pack).setVisibility(View.VISIBLE);
                                        packagesId[3] = dietDayList.get(0).getSelectedDinner();
                                    }

                                }else if(day == 1)
                                    mealView.findViewById(R.id.yesterday_pack).setVisibility(View.INVISIBLE);

                                break;
                            case 1:
                                packagesId[0] = dietDayList.get(0).getSnackPack1();
                                packagesId[1] = dietDayList.get(0).getSnackPack2();
                                packagesId[2] = dietDayList.get(0).getSnackPack3();
                                if(day > 1){
                                    dietQueryBuilder.reset();
                                    dietQueryBuilder.where().eq("day", day - 1);
                                    dietDayList = dietQueryBuilder.query();
                                    if (dietDayList.size() > 0) {
                                        mealView.findViewById(R.id.yesterday_pack).setVisibility(View.VISIBLE);
                                        packagesId[3] = dietDayList.get(0).getSelectedSnack();
                                    }

                                }else if(day == 1)
                                    mealView.findViewById(R.id.yesterday_pack).setVisibility(View.INVISIBLE);

                                break;
                            case 2:
                                packagesId[0] = dietDayList.get(0).getLunchPack1();
                                packagesId[1] = dietDayList.get(0).getLunchPack2();
                                packagesId[2] = dietDayList.get(0).getLunchPack3();
                                if(day > 1){
                                    dietQueryBuilder.reset();
                                    dietQueryBuilder.where().eq("day", day - 1);
                                    dietDayList = dietQueryBuilder.query();
                                    if (dietDayList.size() > 0) {
                                        mealView.findViewById(R.id.yesterday_pack).setVisibility(View.VISIBLE);
                                        packagesId[3] = dietDayList.get(0).getSelectedLunch();
                                    }

                                }else if(day == 1)
                                    mealView.findViewById(R.id.yesterday_pack).setVisibility(View.INVISIBLE);

                                break;
                            case 3:
                                packagesId[0] = dietDayList.get(0).getBreakfastPack1();
                                packagesId[1] = dietDayList.get(0).getBreakfastPack2();
                                packagesId[2] = dietDayList.get(0).getBreakfastPack3();
                                if(day > 1){
                                    dietQueryBuilder.reset();
                                    dietQueryBuilder.where().eq("day", day - 1);
                                    dietDayList = dietQueryBuilder.query();
                                    if (dietDayList.size() > 0) {
                                        mealView.findViewById(R.id.yesterday_pack).setVisibility(View.VISIBLE);
                                        packagesId[3] = dietDayList.get(0).getSelectedBreakfast();
                                    }

                                }else if(day == 1)
                                    mealView.findViewById(R.id.yesterday_pack).setVisibility(View.INVISIBLE);

                                break;
                        }
                        packageQueryBuilder = databaseHelper.getFoodPackageDao().queryBuilder();
                        packageQueryBuilder.where().eq("_id", packagesId[0]);
                        packageList = packageQueryBuilder.query();
                        if (packageList.size() > 0) {
//                                    FoodPackage pack1 = packageList1.get(0);
                            foodsId = packageList.get(0).getFoods().split(",");
                            foodQueryBuilder = databaseHelper.getFoodDao().queryBuilder();
                            packageFoodQueryBuilder = databaseHelper.getPackageFoodDao().queryBuilder();
                            position = 1;
                            for (String foodId : foodsId) {
                                packageFoodQueryBuilder.where().eq("_id", packagesId[0]).and().eq("foodId", foodId.trim());
                                packageFoods = packageFoodQueryBuilder.query();
                                if(packageFoods.size() > 0){
                                    amount = "0";
                                    switch (dietType){
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

                                    if(!amount.equalsIgnoreCase("0")){
                                        foodQueryBuilder.where().eq("foodId", Integer.valueOf(foodId.trim()));
                                        foods = foodQueryBuilder.query();
                                        if (foods.size() > 0) {
//                                            Food food = foods.get(0);
                                            System.out.println("food ------------------------------ " + foods.get(0).getFoodName());
                                            switch (position) {
                                                case 1:
                                                    ((TextView) mealView.findViewById(R.id.pack1_item1)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 2:
                                                    ((TextView) mealView.findViewById(R.id.pack1_item2)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 3:
                                                    ((TextView) mealView.findViewById(R.id.pack1_item3)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 4:
                                                    ((TextView) mealView.findViewById(R.id.pack1_item4)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 5:
                                                    ((TextView) mealView.findViewById(R.id.pack1_item5)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                            }
                                            position++;
                                        }
                                        foodQueryBuilder.reset();
                                    }

                                }
                                packageFoodQueryBuilder.reset();
                            }
                        }
                        packageQueryBuilder.reset();
                        packageList.clear();
                        packageQueryBuilder.where().eq("_id", packagesId[1]);
                        packageList = packageQueryBuilder.query();
                        if (packageList.size() > 0) {
//                                    FoodPackage pack1 = packageList1.get(0);
                            foodsId = packageList.get(0).getFoods().split(",");
                            position = 1;
                            for (String foodId : foodsId) {
                                packageFoodQueryBuilder.where().eq("_id", packagesId[1]).and().eq("foodId", foodId.trim());
                                packageFoods.clear();
                                packageFoods = packageFoodQueryBuilder.query();
                                if(packageFoods.size() > 0){
                                    amount = "0";
                                    switch (dietType){
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

                                    if(!amount.equalsIgnoreCase("0")){
                                        foodQueryBuilder.where().eq("foodId", Integer.valueOf(foodId.trim()));
                                        foods.clear();
                                        foods = foodQueryBuilder.query();
                                        if (foods.size() > 0) {
//                                            Food food = foods.get(0);
                                            System.out.println("food ------------------------------ " + foods.get(0).getFoodName());
                                            switch (position) {
                                                case 1:
                                                    ((TextView) mealView.findViewById(R.id.pack2_item1)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 2:
                                                    ((TextView) mealView.findViewById(R.id.pack2_item2)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 3:
                                                    ((TextView) mealView.findViewById(R.id.pack2_item3)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 4:
                                                    ((TextView) mealView.findViewById(R.id.pack2_item4)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 5:
                                                    ((TextView) mealView.findViewById(R.id.pack2_item5)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                            }
                                            position++;
                                        }
                                        foodQueryBuilder.reset();
                                    }

                                }
                                packageFoodQueryBuilder.reset();
                            }
                        }

                        packageQueryBuilder.reset();
                        packageList.clear();
                        packageQueryBuilder.where().eq("_id", packagesId[2]);
                        packageList = packageQueryBuilder.query();
                        if (packageList.size() > 0) {
//                                    FoodPackage pack1 = packageList1.get(0);
                            foodsId = packageList.get(0).getFoods().split(",");
                            position = 1;
                            for (String foodId : foodsId) {
                                packageFoodQueryBuilder.where().eq("_id", packagesId[2]).and().eq("foodId", foodId.trim());
                                packageFoods.clear();
                                packageFoods = packageFoodQueryBuilder.query();
                                if(packageFoods.size() > 0){
                                    amount = "0";
                                    switch (dietType){
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

                                    if(!amount.equalsIgnoreCase("0")){
                                        foodQueryBuilder.where().eq("foodId", Integer.valueOf(foodId.trim()));
                                        foods.clear();
                                        foods = foodQueryBuilder.query();
                                        if (foods.size() > 0) {
//                                            Food food = foods.get(0);
                                            System.out.println("food ------------------------------ " + foods.get(0).getFoodName());
                                            switch (position) {
                                                case 1:
                                                    ((TextView) mealView.findViewById(R.id.pack3_item1)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 2:
                                                    ((TextView) mealView.findViewById(R.id.pack3_item2)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 3:
                                                    ((TextView) mealView.findViewById(R.id.pack3_item3)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 4:
                                                    ((TextView) mealView.findViewById(R.id.pack3_item4)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 5:
                                                    ((TextView) mealView.findViewById(R.id.pack3_item5)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                            }
                                            position++;
                                        }
                                        foodQueryBuilder.reset();
                                    }

                                }
                                packageFoodQueryBuilder.reset();
                            }
                        }

                        packageQueryBuilder.reset();
                        packageList.clear();
                        packageQueryBuilder.where().eq("_id", packagesId[3]);
                        packageList = packageQueryBuilder.query();
                        if (packageList.size() > 0) {
//                                    FoodPackage pack1 = packageList1.get(0);
                            foodsId = packageList.get(0).getFoods().split(",");
                            position = 1;
                            for (String foodId : foodsId) {
                                packageFoodQueryBuilder.where().eq("_id", packagesId[3]).and().eq("foodId", foodId.trim());
                                packageFoods.clear();
                                packageFoods = packageFoodQueryBuilder.query();
                                if(packageFoods.size() > 0){
                                    amount = "0";
                                    switch (dietType){
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

                                    if(!amount.equalsIgnoreCase("0")){
                                        foodQueryBuilder.where().eq("foodId", Integer.valueOf(foodId.trim()));
                                        foods.clear();
                                        foods = foodQueryBuilder.query();
                                        if (foods.size() > 0) {
//                                            Food food = foods.get(0);
                                            System.out.println("food ------------------------------ " + foods.get(0).getFoodName());
                                            switch (position) {
                                                case 1:
                                                    ((TextView) mealView.findViewById(R.id.pack4_item1)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 2:
                                                    ((TextView) mealView.findViewById(R.id.pack4_item2)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 3:
                                                    ((TextView) mealView.findViewById(R.id.pack4_item3)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 4:
                                                    ((TextView) mealView.findViewById(R.id.pack4_item4)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                                case 5:
                                                    ((TextView) mealView.findViewById(R.id.pack4_item5)).setText(foods.get(0).getFoodName().trim());
                                                    break;
                                            }
                                            position++;
                                        }
                                        foodQueryBuilder.reset();
                                    }

                                }
                                packageFoodQueryBuilder.reset();
                            }
                        }

                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

//    private int getDietDay(long diff) {
//        if (diff >= 0 && diff < TimeUnit.DAYS.toMillis(1))
//            return 1;
//        else if (diff >= TimeUnit.DAYS.toMillis(1) && diff < TimeUnit.DAYS.toMillis(2))
//            return 2;
//        else if (diff >= TimeUnit.DAYS.toMillis(2) && diff < TimeUnit.DAYS.toMillis(3))
//            return 3;
//        else if (diff >= TimeUnit.DAYS.toMillis(3) && diff < TimeUnit.DAYS.toMillis(4))
//            return 4;
//        else if (diff >= TimeUnit.DAYS.toMillis(4) && diff < TimeUnit.DAYS.toMillis(5))
//            return 5;
//        else if (diff >= TimeUnit.DAYS.toMillis(5) && diff < TimeUnit.DAYS.toMillis(6))
//            return 6;
//        else if (diff >= TimeUnit.DAYS.toMillis(6) && diff < TimeUnit.DAYS.toMillis(7))
//            return 7;
//        else if (diff >= TimeUnit.DAYS.toMillis(7) && diff < TimeUnit.DAYS.toMillis(8))
//            return 8;
//        else if (diff >= TimeUnit.DAYS.toMillis(8) && diff < TimeUnit.DAYS.toMillis(9))
//            return 9;
//        else if (diff >= TimeUnit.DAYS.toMillis(9) && diff < TimeUnit.DAYS.toMillis(10))
//            return 10;
//        else if (diff >= TimeUnit.DAYS.toMillis(10) && diff < TimeUnit.DAYS.toMillis(11))
//            return 11;
//        else if (diff >= TimeUnit.DAYS.toMillis(11) && diff < TimeUnit.DAYS.toMillis(12))
//            return 12;
//        else if (diff >= TimeUnit.DAYS.toMillis(12) && diff < TimeUnit.DAYS.toMillis(13))
//            return 13;
//        else if (diff >= TimeUnit.DAYS.toMillis(13) && diff < TimeUnit.DAYS.toMillis(14))
//            return 14;
//        else if (diff >= TimeUnit.DAYS.toMillis(14) && diff < TimeUnit.DAYS.toMillis(15))
//            return 15;
//        else if (diff >= TimeUnit.DAYS.toMillis(15) && diff < TimeUnit.DAYS.toMillis(16))
//            return 16;
//        else if (diff >= TimeUnit.DAYS.toMillis(16) && diff < TimeUnit.DAYS.toMillis(17))
//            return 17;
//        else if (diff >= TimeUnit.DAYS.toMillis(17) && diff < TimeUnit.DAYS.toMillis(18))
//            return 18;
//        else if (diff >= TimeUnit.DAYS.toMillis(18) && diff < TimeUnit.DAYS.toMillis(19))
//            return 19;
//        else if (diff >= TimeUnit.DAYS.toMillis(19) && diff < TimeUnit.DAYS.toMillis(20))
//            return 20;
//        else if (diff >= TimeUnit.DAYS.toMillis(20) && diff < TimeUnit.DAYS.toMillis(21))
//            return 21;
//        else if (diff >= TimeUnit.DAYS.toMillis(21) && diff < TimeUnit.DAYS.toMillis(22))
//            return 22;
//        else if (diff >= TimeUnit.DAYS.toMillis(22) && diff < TimeUnit.DAYS.toMillis(23))
//            return 23;
//        else if (diff >= TimeUnit.DAYS.toMillis(23) && diff < TimeUnit.DAYS.toMillis(24))
//            return 24;
//        else if (diff >= TimeUnit.DAYS.toMillis(24) && diff < TimeUnit.DAYS.toMillis(25))
//            return 25;
//        else if (diff >= TimeUnit.DAYS.toMillis(25) && diff < TimeUnit.DAYS.toMillis(26))
//            return 26;
//        else if (diff >= TimeUnit.DAYS.toMillis(26) && diff < TimeUnit.DAYS.toMillis(27))
//            return 27;
//        else if (diff >= TimeUnit.DAYS.toMillis(27) && diff < TimeUnit.DAYS.toMillis(28))
//            return 28;
//        else if (diff >= TimeUnit.DAYS.toMillis(28) && diff < TimeUnit.DAYS.toMillis(29))
//            return 29;
//        else if (diff >= TimeUnit.DAYS.toMillis(29) && diff < TimeUnit.DAYS.toMillis(30))
//            return 30;
//        else if (diff >= TimeUnit.DAYS.toMillis(30) && diff < TimeUnit.DAYS.toMillis(31))
//            return 31;
//        else
//            return 0;
//    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(DietDishesFragment.newInstance(DINNER, R.layout.fragment_dishes_dinner), getString(R.string.dinner));
        adapter.addFragment(DietDishesFragment.newInstance(SNACK, R.layout.fragment_dishes_snack), getString(R.string.snack));
        adapter.addFragment(DietDishesFragment.newInstance(LUNCH, R.layout.fragment_dishes_lunch), getString(R.string.lunch));
        adapter.addFragment(DietDishesFragment.newInstance(BREAKFAST, R.layout.fragment_dishes_breakfast), getString(R.string.breakfast));
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
                inflateDiet();
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


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
