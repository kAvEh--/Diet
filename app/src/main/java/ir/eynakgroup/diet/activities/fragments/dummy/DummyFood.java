package ir.eynakgroup.diet.activities.fragments.dummy;

/**
 * Created by Shayan on 8/8/2017.
 */

public class DummyFood {
    private String foodName;
    private String amount;
    private String unit = "گرم";

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
