package ir.eynakgroup.diet.activities.fragments.dummy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shayan on 8/8/2017.
 */

public class DummyDish {
    private Day day = Day.TODAY;
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


    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public enum Day{
        YESTERDAY,
        TODAY,
        TOMORROW,
        DAY_AFTER_TOMORROW
    }
}
