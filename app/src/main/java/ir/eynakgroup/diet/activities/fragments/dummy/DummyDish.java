package ir.eynakgroup.diet.activities.fragments.dummy;

import java.util.ArrayList;
import java.util.List;

import ir.eynakgroup.diet.activities.fragments.DietFragment;

/**
 * Created by Shayan on 8/8/2017.
 */

public class DummyDish {
    private DietFragment.Day day = DietFragment.Day.TODAY;
    private List<DummyFood> foods;

    public DummyDish(){
        foods = new ArrayList<>();
    }

    public void addFood(DummyFood food){
        foods.add(food);
    }

    public List<DummyFood> getDishFoods(){
        return this.foods;
    }


    public DietFragment.Day getDay() {
        return day;
    }

    public void setDay(DietFragment.Day day) {
        this.day = day;
    }


}
