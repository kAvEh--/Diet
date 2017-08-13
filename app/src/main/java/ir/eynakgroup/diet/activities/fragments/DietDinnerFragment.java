package ir.eynakgroup.diet.activities.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.activities.fragments.dummy.DummyDish;
import ir.eynakgroup.diet.activities.fragments.dummy.DummyFood;


public class DietDinnerFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MEAL_ID = "meal_id";


    // TODO: Rename and change types of parameters
    private int mMealId;
    private Map<DietFragment.Day, List<DummyDish>> dinnerDishes;


    private ImageView imgBackGrid;
    private TextView textMealTitle;


    private TextView textOptionSelect;

    private LinearLayout layoutPack12;
    private LinearLayout layoutPack34;
    private CardView cardOpenPack;
    private CardView cardNonPack;
    private CardView yesterdayPack;

    private TextView[] dishItem = new TextView[20];
    private TextView[] foodItem = new TextView[5];
    private TextView[] amountItem = new TextView[5];
    private TextView[] midItem = new TextView[5];

    public DietDinnerFragment() {
        // Required empty public constructor
    }

    public DietDinnerFragment(Map<DietFragment.Day, List<DummyDish>> dinnerDishes){
        this.dinnerDishes = dinnerDishes;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mealId Parameter 1.
     * @return A new instance of fragment DietDishesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DietDinnerFragment newInstance(int mealId, Map<DietFragment.Day, List<DummyDish>> dinnerDishes) {
        DietDinnerFragment fragment = new DietDinnerFragment(dinnerDishes);
        Bundle args = new Bundle();
        args.putInt(MEAL_ID, mealId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMealId = getArguments().getInt(MEAL_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dishes_dinner, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textOptionSelect = (TextView) view.getRootView().findViewById(R.id.txt_option_select);
        layoutPack12 = (LinearLayout) view.findViewById(R.id.pack12);
        layoutPack34 = (LinearLayout) view.findViewById(R.id.pack34);
        cardOpenPack = (CardView) view.findViewById(R.id.pack_open);
        cardNonPack = (CardView) view.findViewById(R.id.non_pack);

        dishItem[0] = (TextView) view.findViewById(R.id.pack1_item1);
        dishItem[1] = (TextView) view.findViewById(R.id.pack1_item2);
        dishItem[2] = (TextView) view.findViewById(R.id.pack1_item3);
        dishItem[3] = (TextView) view.findViewById(R.id.pack1_item4);
        dishItem[4] = (TextView) view.findViewById(R.id.pack1_item5);
        dishItem[5] = (TextView) view.findViewById(R.id.pack2_item1);
        dishItem[6] = (TextView) view.findViewById(R.id.pack2_item2);
        dishItem[7] = (TextView) view.findViewById(R.id.pack2_item3);
        dishItem[8] = (TextView) view.findViewById(R.id.pack2_item4);
        dishItem[9] = (TextView) view.findViewById(R.id.pack2_item5);
        dishItem[10] = (TextView) view.findViewById(R.id.pack3_item1);
        dishItem[11] = (TextView) view.findViewById(R.id.pack3_item2);
        dishItem[12] = (TextView) view.findViewById(R.id.pack3_item3);
        dishItem[13] = (TextView) view.findViewById(R.id.pack3_item4);
        dishItem[14] = (TextView) view.findViewById(R.id.pack3_item5);
        dishItem[15] = (TextView) view.findViewById(R.id.pack4_item1);
        dishItem[16] = (TextView) view.findViewById(R.id.pack4_item2);
        dishItem[17] = (TextView) view.findViewById(R.id.pack4_item3);
        dishItem[18] = (TextView) view.findViewById(R.id.pack4_item4);
        dishItem[19] = (TextView) view.findViewById(R.id.pack4_item5);

        textMealTitle = (TextView) view.findViewById(R.id.meal_title);
        imgBackGrid = (ImageView) view.findViewById(R.id.back_group);
        imgBackGrid.setOnClickListener(this);
        foodItem[0] = (TextView) view.findViewById(R.id.food_item1);
        foodItem[1] = (TextView) view.findViewById(R.id.food_item2);
        foodItem[2] = (TextView) view.findViewById(R.id.food_item3);
        foodItem[3] = (TextView) view.findViewById(R.id.food_item4);
        foodItem[4] = (TextView) view.findViewById(R.id.food_item5);
        amountItem[0] = (TextView) view.findViewById(R.id.amount_item1);
        amountItem[1] = (TextView) view.findViewById(R.id.amount_item2);
        amountItem[2] = (TextView) view.findViewById(R.id.amount_item3);
        amountItem[3] = (TextView) view.findViewById(R.id.amount_item4);
        amountItem[4] = (TextView) view.findViewById(R.id.amount_item5);
        midItem[0] = (TextView) view.findViewById(R.id.mid_item1);
        midItem[1] = (TextView) view.findViewById(R.id.mid_item2);
        midItem[2] = (TextView) view.findViewById(R.id.mid_item3);
        midItem[3] = (TextView) view.findViewById(R.id.mid_item4);
        midItem[4] = (TextView) view.findViewById(R.id.mid_item5);
        view.findViewById(R.id.choose_option_btn_1).setOnClickListener(this);
        view.findViewById(R.id.choose_option_btn_2).setOnClickListener(this);
        view.findViewById(R.id.choose_option_btn_3).setOnClickListener(this);
        view.findViewById(R.id.choose_option_btn_4).setOnClickListener(this);
        view.findViewById(R.id.choose_option_btn_5).setOnClickListener(this);
        view.findViewById(R.id.pack1).setOnClickListener(this);
        view.findViewById(R.id.pack2).setOnClickListener(this);
        view.findViewById(R.id.pack3).setOnClickListener(this);
        view.findViewById(R.id.non_pack).setOnClickListener(this);

        yesterdayPack = (CardView) view.findViewById(R.id.yesterday_pack);
        yesterdayPack.setOnClickListener(this);

        updateDishes(DietFragment.currentDay);
    }

    private List<DummyDish> dishList;
    public void updateDishes(DietFragment.Day day) {
        if(day.equals(DietFragment.Day.TODAY))
            yesterdayPack.setVisibility(View.VISIBLE);
        else
            yesterdayPack.setVisibility(View.INVISIBLE);


        for (int i = 0; i < dishItem.length; i++)
            dishItem[i].setText("");

        dishList = dinnerDishes.get(day);
        for (DummyDish dish : dishList) {
            List<DummyFood> foodList = dish.getDishFoods();
            int position = 0;
            for (DummyFood food : foodList) {
                dishItem[dish.getDishNumber() * 5 + position].setText(food.getFoodName());
                position++;
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_group:
                cardNonPack.setVisibility(View.VISIBLE);
                layoutPack12.setVisibility(View.VISIBLE);
                layoutPack34.setVisibility(View.VISIBLE);
                textOptionSelect.setVisibility(View.VISIBLE);
                cardOpenPack.setVisibility(View.GONE);


                break;
            case R.id.choose_option_btn_1:
            case R.id.pack1:
                textMealTitle.setText("شام یک");
                bindPack(0);

                break;
            case R.id.choose_option_btn_2:
            case R.id.pack2:
                textMealTitle.setText("شام دو");
                bindPack(1);

                break;
            case R.id.choose_option_btn_3:
            case R.id.pack3:
                textMealTitle.setText("شام سه");
                bindPack(2);

                break;
            case R.id.choose_option_btn_4:
            case R.id.yesterday_pack:
                textMealTitle.setText("شام دیروز");
                bindPack(3);


                break;
            case R.id.choose_option_btn_5:
            case R.id.non_pack:
//                bindPack(4);
                break;


        }
    }

    private void bindPack(int packNum) {
        if(DietFragment.currentDay.equals(DietFragment.Day.TODAY)){
            for(int i = 0; i < foodItem.length; i++){
                foodItem[i].setText("");
                amountItem[i].setText("");
                midItem[i].setText("");
            }
            for(DummyDish dish: dishList){
                if(dish.getDishNumber() == packNum){
                    System.out.println(dish.getPackageId()+"------------------- package id");
                    List<DummyFood> foodList = dish.getDishFoods();
                    int position = 0;
                    for (DummyFood food : foodList) {
                        foodItem[position].setText(food.getFoodName());
                        amountItem[position].setText(food.getAmount()+" "+food.getUnit());
                        midItem[position].setText("--------------------------------------------------------");
                        position++;
                    }
                    break;
                }
            }
            cardNonPack.setVisibility(View.GONE);
            layoutPack12.setVisibility(View.GONE);
            layoutPack34.setVisibility(View.GONE);
            textOptionSelect.setVisibility(View.INVISIBLE);
            cardOpenPack.setVisibility(View.VISIBLE);
        }

    }


}
