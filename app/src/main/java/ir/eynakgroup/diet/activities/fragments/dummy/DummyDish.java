package ir.eynakgroup.diet.activities.fragments.dummy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shayan on 8/8/2017.
 */

public class DummyDish {
    private String dishId;
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


    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }
}
