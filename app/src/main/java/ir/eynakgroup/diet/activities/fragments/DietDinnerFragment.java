package ir.eynakgroup.diet.activities.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private TextView textFood1;
    private TextView textFood2;
    private TextView textFood3;
    private TextView textFood4;
    private TextView textFood5;
    private TextView textAmount1;
    private TextView textAmount2;
    private TextView textAmount3;
    private TextView textAmount4;
    private TextView textAmount5;

    private TextView[] dishItem = new TextView[20];

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
        textFood1 = (TextView) view.findViewById(R.id.food_item1);
        textFood2 = (TextView) view.findViewById(R.id.food_item2);
        textFood3 = (TextView) view.findViewById(R.id.food_item3);
        textFood4 = (TextView) view.findViewById(R.id.food_item4);
        textFood5 = (TextView) view.findViewById(R.id.food_item5);
        textAmount1 = (TextView) view.findViewById(R.id.amount_item1);
        textAmount2 = (TextView) view.findViewById(R.id.amount_item2);
        textAmount3 = (TextView) view.findViewById(R.id.amount_item3);
        textAmount4 = (TextView) view.findViewById(R.id.amount_item4);
        textAmount5 = (TextView) view.findViewById(R.id.amount_item5);
        view.findViewById(R.id.choose_option_btn_1).setOnClickListener(this);
        view.findViewById(R.id.choose_option_btn_2).setOnClickListener(this);
        view.findViewById(R.id.choose_option_btn_3).setOnClickListener(this);
        view.findViewById(R.id.choose_option_btn_4).setOnClickListener(this);
        view.findViewById(R.id.choose_option_btn_5).setOnClickListener(this);
        view.findViewById(R.id.pack1).setOnClickListener(this);
        view.findViewById(R.id.pack2).setOnClickListener(this);
        view.findViewById(R.id.pack3).setOnClickListener(this);
        view.findViewById(R.id.yesterday_pack).setOnClickListener(this);
        view.findViewById(R.id.non_pack).setOnClickListener(this);

        updateDishes(DietFragment.currentDay);
    }

    public void updateDishes(DietFragment.Day day){
        for(int i = 0; i < dishItem.length; i++)
            dishItem[i].setText("");

        List<DummyDish> dishList = dinnerDishes.get(day);
        int pack = 0;
        for(DummyDish dish: dishList){
            List<DummyFood> foodList = dish.getDishFoods();

            if(!day.equals(dish.getDay())){
                int position = 0;
                for(DummyFood food: foodList){
                    dishItem[15+position].setText(food.getFoodName());
                    position++;
                }

            }else{
                int position = 0;
                for(DummyFood food: foodList){
                    dishItem[pack*5+position].setText(food.getFoodName());
                    position++;
                }

                pack++;
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_group:

                break;
            case R.id.choose_option_btn_1:
            case R.id.pack1:

                break;
            case R.id.choose_option_btn_2:
            case R.id.pack2:

                break;
            case R.id.choose_option_btn_3:
            case R.id.pack3:

                break;
            case R.id.choose_option_btn_4:
            case R.id.yesterday_pack:

                break;
            case R.id.choose_option_btn_5:
            case R.id.non_pack:

                break;


        }
    }


}
