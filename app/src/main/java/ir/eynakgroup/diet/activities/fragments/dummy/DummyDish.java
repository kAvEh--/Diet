package ir.eynakgroup.diet.activities.fragments.dummy;

import java.util.ArrayList;
import java.util.List;

import ir.eynakgroup.diet.activities.fragments.DietFragment;

/**
 * Created by Shayan on 8/8/2017.
 */

public class DummyDish {
    private List<DummyFood> foods;
    private String packageId;
    private int dietDay;
    private int dishNumber;

    public DummyDish(){
        foods = new ArrayList<>();
    }

    public void addFood(DummyFood food){
        foods.add(food);
    }

    public List<DummyFood> getDishFoods(){
        return this.foods;
    }

    public int getDishNumber() {
        return dishNumber;
    }

    public void setDishNumber(int dishNumber) {
        this.dishNumber = dishNumber;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public int getDietDay() {
        return dietDay;
    }

    public void setDietDay(int dietDay) {
        this.dietDay = dietDay;
    }
}
