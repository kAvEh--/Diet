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


public class MealFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MEAL_ID = "meal_id";


    // TODO: Rename and change types of parameters
    protected Map<DietFragment.Day, List<DummyDish>> dishes;

    protected ImageView imgBackGrid;
    protected TextView textMealTitle;

    protected LinearLayout layoutPack12;
    protected LinearLayout layoutPack34;
    protected CardView cardOpenPack;
    protected CardView cardNonPack;
    protected CardView yesterdayPack;


    protected TextView textOptionSelect;

    protected TextView[] dishItem = new TextView[16];
    protected TextView[] foodItem = new TextView[4];
    protected TextView[] amountItem = new TextView[4];
    protected TextView[] midItem = new TextView[4];

    public MealFragment() {
        // Required empty public constructor
    }


    public MealFragment(Map<DietFragment.Day, List<DummyDish>> dishes) {
        this.dishes = dishes;
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

        yesterdayPack = (CardView) view.findViewById(R.id.yesterday_pack);

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

        dishList = dishes.get(day);
        for (DummyDish dish : dishList) {
            List<DummyFood> foodList = dish.getDishFoods();
            int position = 0;
            for (DummyFood food : foodList) {
                dishItem[dish.getDishNumber() * 4 + position].setText(food.getFoodName());
                position++;
            }
        }

    }


    protected void bindPack(int packNum) {
        if(DietFragment.currentDay.equals(DietFragment.Day.TODAY)){
            for(int i = 0; i < foodItem.length; i++){
                foodItem[i].setText("");
                amountItem[i].setText("");
                midItem[i].setText("");
            }

            for(DummyDish dish: dishList){
                if(dish.getDishNumber() == packNum){


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

