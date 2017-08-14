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


public class DietSnackFragment extends MealFragment implements View.OnClickListener {

    public DietSnackFragment() {
        // Required empty public constructor
    }

    public DietSnackFragment(Map<DietFragment.Day, List<DummyDish>> dinnerDishes){
        super(dinnerDishes);
//        this.dinnerDishes = dinnerDishes;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mealId Parameter 1.
     * @return A new instance of fragment DietDishesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DietSnackFragment newInstance(int mealId, Map<DietFragment.Day, List<DummyDish>> dinnerDishes) {
        DietSnackFragment fragment = new DietSnackFragment(dinnerDishes);
        Bundle args = new Bundle();
        args.putInt(MEAL_ID, mealId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dishes_snack, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    }

//    private List<DummyDish> dishList;
//    public void updateDishes(DietFragment.Day day) {
//        if(day.equals(DietFragment.Day.TODAY))
//            yesterdayPack.setVisibility(View.VISIBLE);
//        else
//            yesterdayPack.setVisibility(View.INVISIBLE);
//
//
//        for (int i = 0; i < dishItem.length; i++)
//            dishItem[i].setText("");
//
//        dishList = dinnerDishes.get(day);
//        for (DummyDish dish : dishList) {
//            List<DummyFood> foodList = dish.getDishFoods();
//            int position = 0;
//            for (DummyFood food : foodList) {
//                dishItem[dish.getDishNumber() * 5 + position].setText(food.getFoodName());
//                position++;
//            }
//        }
//
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_group:
                updateDishes(DietFragment.currentDay, true);
                backToOptions();


                break;
            case R.id.choose_option_btn_1:
            case R.id.pack1:
                textMealTitle.setText("میان‌وعده یک");
                bindPack(0);

                break;
            case R.id.choose_option_btn_2:
            case R.id.pack2:
                textMealTitle.setText("میان‌وعده دو");
                bindPack(1);

                break;
            case R.id.choose_option_btn_3:
            case R.id.pack3:
                textMealTitle.setText("میان‌وعده سه");
                bindPack(2);

                break;
            case R.id.choose_option_btn_4:
            case R.id.yesterday_pack:
                textMealTitle.setText("میان‌وعده دیروز");
                bindPack(3);


                break;
            case R.id.choose_option_btn_5:
            case R.id.no_pack:
//                bindPack(4);
                break;


        }
    }

//    private void bindPack(int packNum) {
//        if(DietFragment.currentDay.equals(DietFragment.Day.TODAY)){
//            for(int i = 0; i < foodItem.length; i++){
//                foodItem[i].setText("");
//                amountItem[i].setText("");
//                midItem[i].setText("");
//            }
//            for(DummyDish dish: dishList){
//                if(dish.getDishNumber() == packNum){
//                    System.out.println(dish.getPackageId()+"------------------- package id");
//                    List<DummyFood> foodList = dish.getDishFoods();
//                    int position = 0;
//                    for (DummyFood food : foodList) {
//                        foodItem[position].setText(food.getFoodName());
//                        amountItem[position].setText(food.getAmount()+" "+food.getUnit());
//                        midItem[position].setText("--------------------------------------------------------");
//                        position++;
//                    }
//                    break;
//                }
//            }
//            cardNonPack.setVisibility(View.GONE);
//            layoutPack12.setVisibility(View.GONE);
//            layoutPack34.setVisibility(View.GONE);
//            textOptionSelect.setVisibility(View.INVISIBLE);
//            cardOpenPack.setVisibility(View.VISIBLE);
//        }
//
//    }


}
