package ir.eynakgroup.diet.activities.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.activities.fragments.dummy.DummyDish;
import ir.eynakgroup.diet.activities.fragments.dummy.DummyFood;
import ir.eynakgroup.diet.database.DatabaseHelper;
import ir.eynakgroup.diet.database.tables.Diet;
import ir.eynakgroup.diet.database.tables.UserInfo;
import ir.eynakgroup.diet.network.ClientFactory;
import ir.eynakgroup.diet.network.RequestMethod;
import ir.eynakgroup.diet.network.response_models.CommonResponse;
import ir.eynakgroup.diet.utils.AppPreferences;
import retrofit2.Call;
import retrofit2.Callback;


public class MealFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    protected static final String MEAL_ID = "meal_id";
    protected static final String LAYOUT_RES = "layout_resource";

    private int current_day;

    private int mealId;
    private int layoutRes;
    // TODO: Rename and change types of parameters
    protected Map<DietFragment.Day, List<DummyDish>> dishes;

    protected ImageView imgBackGrid;
    protected TextView textMealTitle;

    protected LinearLayout layoutPack12;
    protected LinearLayout layoutPack34;
    protected CardView cardOpenPack;
    protected CardView cardNoPack;
    protected CardView cardPack1;
    protected CardView cardPack2;
    protected CardView cardPack3;
    protected CardView yesterdayPack;

    protected Button btnNoPack;
    protected Button btnPack1;
    protected Button btnPack2;
    protected Button btnPack3;
    protected Button btnPack4;

    private RequestMethod mRequestMethod;

    protected TextView textOptionSelect;
    protected TextView[] dishItem = new TextView[16];
    protected TextView[] foodItem = new TextView[4];
    protected TextView[] amountItem = new TextView[4];
    protected TextView[] midItem = new TextView[4];

    protected AppPreferences appPreferences;
    protected DatabaseHelper databaseHelper;
    protected QueryBuilder<Diet, Integer> dietQueryBuilder;
    private UpdateBuilder<Diet, Integer> dietUpdateBuilder;

    public MealFragment() {
        // Required empty public constructor
    }


    private MealFragment(Map<DietFragment.Day, List<DummyDish>> dishes, int cday) {
        this.dishes = dishes;
        this.current_day = cday;
    }

    public static MealFragment newInstance(int cday, int mealId, @LayoutRes int layoutRes, Map<DietFragment.Day, List<DummyDish>> dishes) {
        MealFragment fragment = new MealFragment(dishes, cday);
        Bundle args = new Bundle();
        args.putInt(MEAL_ID, mealId);
        args.putInt(LAYOUT_RES, layoutRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layoutRes, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mealId = getArguments().getInt(MEAL_ID);
            layoutRes = getArguments().getInt(LAYOUT_RES);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textOptionSelect = (TextView) view.findViewById(R.id.txt_option_select);
        layoutPack12 = (LinearLayout) view.findViewById(R.id.pack12);
        layoutPack34 = (LinearLayout) view.findViewById(R.id.pack34);
        cardOpenPack = (CardView) view.findViewById(R.id.pack_open);
        cardNoPack = (CardView) view.findViewById(R.id.no_pack);

        dishItem[0] = (TextView) view.findViewById(R.id.pack1_item1);
        dishItem[1] = (TextView) view.findViewById(R.id.pack1_item2);
        dishItem[2] = (TextView) view.findViewById(R.id.pack1_item3);
        dishItem[3] = (TextView) view.findViewById(R.id.pack1_item4);
        dishItem[4] = (TextView) view.findViewById(R.id.pack2_item1);
        dishItem[5] = (TextView) view.findViewById(R.id.pack2_item2);
        dishItem[6] = (TextView) view.findViewById(R.id.pack2_item3);
        dishItem[7] = (TextView) view.findViewById(R.id.pack2_item4);
        dishItem[8] = (TextView) view.findViewById(R.id.pack3_item1);
        dishItem[9] = (TextView) view.findViewById(R.id.pack3_item2);
        dishItem[10] = (TextView) view.findViewById(R.id.pack3_item3);
        dishItem[11] = (TextView) view.findViewById(R.id.pack3_item4);
        dishItem[12] = (TextView) view.findViewById(R.id.pack4_item1);
        dishItem[13] = (TextView) view.findViewById(R.id.pack4_item2);
        dishItem[14] = (TextView) view.findViewById(R.id.pack4_item3);
        dishItem[15] = (TextView) view.findViewById(R.id.pack4_item4);

        textMealTitle = (TextView) view.findViewById(R.id.meal_title);
        imgBackGrid = (ImageView) view.findViewById(R.id.back_group);
        foodItem[0] = (TextView) view.findViewById(R.id.food_item1);
        foodItem[1] = (TextView) view.findViewById(R.id.food_item2);
        foodItem[2] = (TextView) view.findViewById(R.id.food_item3);
        foodItem[3] = (TextView) view.findViewById(R.id.food_item4);
        amountItem[0] = (TextView) view.findViewById(R.id.amount_item1);
        amountItem[1] = (TextView) view.findViewById(R.id.amount_item2);
        amountItem[2] = (TextView) view.findViewById(R.id.amount_item3);
        amountItem[3] = (TextView) view.findViewById(R.id.amount_item4);
        midItem[0] = (TextView) view.findViewById(R.id.mid_item1);
        midItem[1] = (TextView) view.findViewById(R.id.mid_item2);
        midItem[2] = (TextView) view.findViewById(R.id.mid_item3);
        midItem[3] = (TextView) view.findViewById(R.id.mid_item4);

        btnPack1 = (Button) view.findViewById(R.id.choose_option_btn_1);
        btnPack2 = (Button) view.findViewById(R.id.choose_option_btn_2);
        btnPack3 = (Button) view.findViewById(R.id.choose_option_btn_3);
        btnPack4 = (Button) view.findViewById(R.id.choose_option_btn_4);
        btnNoPack = (Button) view.findViewById(R.id.choose_option_btn_5);
        cardPack1 = (CardView) view.findViewById(R.id.pack1);
        cardPack2 = (CardView) view.findViewById(R.id.pack2);
        cardPack3 = (CardView) view.findViewById(R.id.pack3);
        yesterdayPack = (CardView) view.findViewById(R.id.yesterday_pack);

        imgBackGrid.setOnClickListener(this);
        btnPack1.setOnClickListener(this);
        btnPack2.setOnClickListener(this);
        btnPack3.setOnClickListener(this);
        btnPack4.setOnClickListener(this);
        btnNoPack.setOnClickListener(this);

        cardPack1.setOnClickListener(this);
        cardPack2.setOnClickListener(this);
        cardPack3.setOnClickListener(this);
        cardNoPack.setOnClickListener(this);
        yesterdayPack.setOnClickListener(this);

        appPreferences = new AppPreferences(getContext());
        databaseHelper = new DatabaseHelper(getContext());
        try {
            dietQueryBuilder = databaseHelper.getDietDao().queryBuilder();
            dietUpdateBuilder = databaseHelper.getDietDao().updateBuilder();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateDishes(DietFragment.currentDay, false);
    }

    private List<DummyDish> dishList;

    public void updateDishes(DietFragment.Day day, boolean backToOption) {
        if (getView() == null)
            return;
        dishList = dishes.get(day);
        if (!backToOption) {
            for (DummyDish dish : dishList) {
                if (current_day == dish.getDietDay()) {
                    try {
                        switch (mealId) {
                            case 0:
                                dietQueryBuilder.where().eq("_id", appPreferences.getDietNumber()).and().eq("day", dish.getDietDay()).and().eq("selectedBreakfast", dish.getPackageId());
                                break;
                            case 1:
                                dietQueryBuilder.where().eq("_id", appPreferences.getDietNumber()).and().eq("day", dish.getDietDay()).and().eq("selectedLunch", dish.getPackageId());
                                break;
                            case 2:
                                dietQueryBuilder.where().eq("_id", appPreferences.getDietNumber()).and().eq("day", dish.getDietDay()).and().eq("selectedSnack", dish.getPackageId());
                                break;
                            case 3:
                                dietQueryBuilder.where().eq("_id", appPreferences.getDietNumber()).and().eq("day", dish.getDietDay()).and().eq("selectedDinner", dish.getPackageId());
                                break;
                        }
                        if (dietQueryBuilder.query().size() > 0) {
                            bindPack(dish.getDishNumber());
                            return;

                        }
                        dietQueryBuilder.reset();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        backToOptions();
        if (day.equals(DietFragment.Day.TODAY)) {
            yesterdayPack.setVisibility(View.VISIBLE);
            btnPack1.setVisibility(View.VISIBLE);
            btnPack2.setVisibility(View.VISIBLE);
            btnPack3.setVisibility(View.VISIBLE);
            btnPack4.setVisibility(View.VISIBLE);
            btnNoPack.setVisibility(View.VISIBLE);
            cardNoPack.setVisibility(View.VISIBLE);
            textOptionSelect.setVisibility(View.VISIBLE);
        } else {
            yesterdayPack.setVisibility(View.INVISIBLE);
            btnPack1.setVisibility(View.GONE);
            btnPack2.setVisibility(View.GONE);
            btnPack3.setVisibility(View.GONE);
            btnPack4.setVisibility(View.GONE);
            btnNoPack.setVisibility(View.GONE);
            cardNoPack.setVisibility(View.INVISIBLE);
            textOptionSelect.setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < dishItem.length; i++)
            dishItem[i].setText("");


        for (DummyDish dish : dishList) {
            if (!dish.getPackageId().equals("0")) {
                List<DummyFood> foodList = dish.getDishFoods();
                int position = 0;
                for (DummyFood food : foodList) {
                    dishItem[dish.getDishNumber() * 4 + position].setText(food.getFoodName());
                    position++;
                }
            }
        }
    }

    protected void bindPack(int packNum) {
        if (DietFragment.currentDay.equals(DietFragment.Day.TODAY)) {
            for (int i = 0; i < foodItem.length; i++) {
                foodItem[i].setText("");
                amountItem[i].setText("");
                midItem[i].setText("");
            }
            for (DummyDish dish : dishList) {
                if (dish.getDishNumber() == packNum) {
                    try {
                        switch (mealId) {
                            case 0:
                                dietQueryBuilder.where().eq("_id", appPreferences.getDietNumber()).and().eq("day", dish.getDietDay()).and().eq("selectedBreakfast", dish.getPackageId());
                                break;
                            case 1:
                                dietQueryBuilder.where().eq("_id", appPreferences.getDietNumber()).and().eq("day", dish.getDietDay()).and().eq("selectedLunch", dish.getPackageId());
                                break;
                            case 2:
                                dietQueryBuilder.where().eq("_id", appPreferences.getDietNumber()).and().eq("day", dish.getDietDay()).and().eq("selectedSnack", dish.getPackageId());
                                break;
                            case 3:
                                dietQueryBuilder.where().eq("_id", appPreferences.getDietNumber()).and().eq("day", dish.getDietDay()).and().eq("selectedDinner", dish.getPackageId());
                                break;
                        }
                        if (dietQueryBuilder.query().size() == 0) {
                            dietUpdateBuilder.where().eq("_id", appPreferences.getDietNumber()).and().eq("day", dish.getDietDay());
                            System.out.println("~~~~~~>> select dishes " + dish.getDietDay() + ":" + dish.getPackageId());
                            boolean flag = false;
                            switch (mealId) {
                                case 0:
                                    dietUpdateBuilder.updateColumnValue("selectedBreakfast", dish.getPackageId());
                                    flag = true;
                                    break;
                                case 1:
                                    dietUpdateBuilder.updateColumnValue("selectedLunch", dish.getPackageId());
                                    flag = true;
                                    break;
                                case 2:
                                    dietUpdateBuilder.updateColumnValue("selectedSnack", dish.getPackageId());
                                    flag = true;
                                    break;
                                case 3:
                                    dietUpdateBuilder.updateColumnValue("selectedDinner", dish.getPackageId());
                                    flag = true;
                                    break;
                            }
                            dietUpdateBuilder.update();
                            dietUpdateBuilder.reset();
                            QueryBuilder<Diet, Integer> dietQueryBuilder = databaseHelper.getDietDao().queryBuilder();
                            dietQueryBuilder.where().eq("_id", appPreferences.getDietNumber());
                            List<Diet> dietList = dietQueryBuilder.query();
                            String serverId = dietList.get(0).getServerId();
                            if (flag) {
                                int curr_diet = appPreferences.getDietNumber();
                                appPreferences.setTotalPoints(appPreferences.getTotalPoint() + 1);
                                appPreferences.setDietPoint("P" + curr_diet, appPreferences.getDietPoint("P" + curr_diet) + 1);

                                final UserInfo user = databaseHelper.getUserDao().queryForAll().get(0);
                                Call<CommonResponse> call = getRequestMethod().changeStatus(user.getSessionId(), user.getApiKey(), user.getUserId(),
                                        serverId, dish.getDietDay(), mealId, 1, dish.getPackageId());
                                call.enqueue(new Callback<CommonResponse>() {
                                    @Override
                                    public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                                        System.out.println(response.body().getError() + ">>>>>>>>" + response.body().getStatus());
                                        try {
                                            if (response.body().getStatus().equals("success")) {
                                                // set the criteria like you would a QueryBuilder
//                        updateBuilder.where().eq("User_ID", user.getUserId());
                                                System.out.println("seccess--------------------------------------->>");
                                            } else {
                                                //TODO
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            //TODO
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                                    }
                                });
                            }
                        }
                        dietQueryBuilder.reset();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                    setMealTitles(packNum);
                    List<DummyFood> foodList = dish.getDishFoods();
                    int position = 0;
                    for (DummyFood food : foodList) {
                        foodItem[position].setText(food.getFoodName());
                        amountItem[position].setText(food.getAmount() + " " + food.getUnit());
                        midItem[position].setText("--------------------------------------------------------");
                        position++;
                    }
                    break;
                }
            }
            cardNoPack.setVisibility(View.GONE);
            layoutPack12.setVisibility(View.GONE);
            layoutPack34.setVisibility(View.GONE);
            textOptionSelect.setVisibility(View.INVISIBLE);
            cardOpenPack.setVisibility(View.VISIBLE);
        }
    }

    protected RequestMethod getRequestMethod() {
        if (mRequestMethod == null)
            mRequestMethod = ClientFactory.getRetrofitInstance().create(RequestMethod.class);

        return mRequestMethod;
    }

    private void setMealTitles(int packNum) {
        String mealType = "";
        switch (packNum) {
            case 0:
                mealType = "یک";
                break;
            case 1:
                mealType = "دو";
                break;
            case 2:
                mealType = "سه";
                break;
            case 3:
                mealType = "دیروز";
                break;
        }
        switch (mealId) {
            case 0:
                textMealTitle.setText("صبحانه " + mealType);
                break;
            case 1:
                textMealTitle.setText("ناهار " + mealType);
                break;
            case 2:
                textMealTitle.setText("میان‌وعده " + mealType);
                break;
            case 3:
                textMealTitle.setText("شام " + mealType);
                break;
        }

    }

    protected void backToOptions() {
        cardNoPack.setVisibility(View.VISIBLE);
        layoutPack12.setVisibility(View.VISIBLE);
        layoutPack34.setVisibility(View.VISIBLE);
        textOptionSelect.setVisibility(View.VISIBLE);
        cardOpenPack.setVisibility(View.GONE);
    }

    protected void deselectFood(DietFragment.Day day) {
        try {
            System.out.println("~~~~~~>> deselect selected " + current_day);
            dietUpdateBuilder.where().eq("_id", appPreferences.getDietNumber()).and().eq("day", current_day);
            boolean flag = false;
            switch (mealId) {
                case 0:
                    flag = true;
                    dietUpdateBuilder.updateColumnValue("selectedBreakfast", 0);
                    break;
                case 1:
                    flag = true;
                    dietUpdateBuilder.updateColumnValue("selectedLunch", 0);
                    break;
                case 2:
                    flag = true;
                    dietUpdateBuilder.updateColumnValue("selectedSnack", 0);
                    break;
                case 3:
                    flag = true;
                    dietUpdateBuilder.updateColumnValue("selectedDinner", 0);
                    break;
            }
            dietUpdateBuilder.update();
            dietUpdateBuilder.reset();
            if (flag) {
                int curr_diet = appPreferences.getDietNumber();
                appPreferences.setTotalPoints(appPreferences.getTotalPoint() - 1);
                System.out.println("current __________ P" + curr_diet);
                appPreferences.setDietPoint("P" + curr_diet, appPreferences.getDietPoint("P" + curr_diet) - 1);

                QueryBuilder<Diet, Integer> dietQueryBuilder = databaseHelper.getDietDao().queryBuilder();
                dietQueryBuilder.where().eq("_id", appPreferences.getDietNumber());
                List<Diet> dietList = dietQueryBuilder.query();
                String serverId = dietList.get(0).getServerId();

                final UserInfo user = databaseHelper.getUserDao().queryForAll().get(0);
                Call<CommonResponse> call = getRequestMethod().changeStatus(user.getSessionId(), user.getApiKey(), user.getUserId(),
                        serverId, current_day, mealId, 0, "0");
                call.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                        System.out.println(response.body().getError() + ">>>>>>>>" + response.body().getStatus());
                        try {
                            if (response.body().getStatus().equals("success")) {
                                // set the criteria like you would a QueryBuilder
//                        updateBuilder.where().eq("User_ID", user.getUserId());
                                System.out.println("seccess--------------------------------------->>");
                            } else {
                                //TODO
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            //TODO
                        }

                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                    }
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_group:
                deselectFood(DietFragment.currentDay);
                updateDishes(DietFragment.currentDay, true);
                backToOptions();

                break;
            case R.id.choose_option_btn_1:
            case R.id.pack1:
                bindPack(0);

                break;
            case R.id.choose_option_btn_2:
            case R.id.pack2:
                bindPack(1);

                break;
            case R.id.choose_option_btn_3:
            case R.id.pack3:
                bindPack(2);

                break;
            case R.id.choose_option_btn_4:
            case R.id.yesterday_pack:
                bindPack(3);


                break;
            case R.id.choose_option_btn_5:
            case R.id.no_pack:
//                bindPack(4);
                break;


        }
    }
}

