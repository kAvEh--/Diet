package ir.eynakgroup.diet.database.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Shayan on 4/30/2017.
 */
@DatabaseTable(tableName = "Dishes")
public class Dish {
    @DatabaseField(columnName = "Food_ID", dataType = DataType.INTEGER)
    private int mealId;

    @DatabaseField(columnName = "Food_ID", dataType = DataType.INTEGER)
    private int packageId;

    @DatabaseField(columnName = "Hated_Foods", dataType = DataType.STRING)
    private String hatedFoods;

    @DatabaseField(columnName = "Food_ID", dataType = DataType.INTEGER)
    private int foodId;

    @DatabaseField(columnName = "Category", dataType = DataType.INTEGER)
    private int category;

    @DatabaseField(columnName = "Is_Standard_Unit", dataType = DataType.INTEGER)
    private int isStandard;

    @DatabaseField(columnName = "Amount_1000", dataType = DataType.BIG_DECIMAL_NUMERIC)
    private float amount1000;

    @DatabaseField(columnName = "Amount_1250", dataType = DataType.BIG_DECIMAL_NUMERIC)
    private float amount1250;

    @DatabaseField(columnName = "Amount_1500", dataType = DataType.BIG_DECIMAL_NUMERIC)
    private float amount1500;

    @DatabaseField(columnName = "Amount_1750", dataType = DataType.BIG_DECIMAL_NUMERIC)
    private float amount1750;

    @DatabaseField(columnName = "Amount_2000", dataType = DataType.BIG_DECIMAL_NUMERIC)
    private float amount2000;

    @DatabaseField(columnName = "Amount_2250", dataType = DataType.BIG_DECIMAL_NUMERIC)
    private float amount2250;

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getHatedFoods() {
        return hatedFoods;
    }

    public void setHatedFoods(String hatedFoods) {
        this.hatedFoods = hatedFoods;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean getIsStandard() {
        return Boolean.parseBoolean(this.isStandard+"");
    }

    public void setIsStandard(int isStandard) {
        this.isStandard = isStandard;
    }

    public float getAmount1000() {
        return amount1000;
    }

    public void setAmount1000(float amount1000) {
        this.amount1000 = amount1000;
    }

    public float getAmount1250() {
        return amount1250;
    }

    public void setAmount1250(float amount1250) {
        this.amount1250 = amount1250;
    }

    public float getAmount1500() {
        return amount1500;
    }

    public void setAmount1500(float amount1500) {
        this.amount1500 = amount1500;
    }

    public float getAmount1750() {
        return amount1750;
    }

    public void setAmount1750(float amount1750) {
        this.amount1750 = amount1750;
    }

    public float getAmount2000() {
        return amount2000;
    }

    public void setAmount2000(float amount2000) {
        this.amount2000 = amount2000;
    }

    public float getAmount2250() {
        return amount2250;
    }

    public void setAmount2250(float amount2250) {
        this.amount2250 = amount2250;
    }
}
