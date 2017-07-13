package ir.eynakgroup.diet.database.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Shayan on 4/30/2017.
 */
@DatabaseTable(tableName = "Foods")
public class Food {
    @DatabaseField(columnName = "Food_ID", dataType = DataType.INTEGER)
    private int foodId;
    @DatabaseField(columnName = "Food_Name", dataType = DataType.STRING)
    private String foodName;
    @DatabaseField(columnName = "Std_Energy", dataType = DataType.BIG_DECIMAL_NUMERIC)
    private float stdEnergy;
    @DatabaseField(columnName = "Std_Protein", dataType = DataType.BIG_DECIMAL_NUMERIC)
    private float stdProtein;
    @DatabaseField(columnName = "Sec_Unit_ID", dataType = DataType.INTEGER)
    private int unitId;
    @DatabaseField(columnName = "Unit_Energy", dataType = DataType.BIG_DECIMAL_NUMERIC)
    private float unitEnergy;
    @DatabaseField(columnName = "Unit_Protein", dataType = DataType.BIG_DECIMAL_NUMERIC)
    private float unitProtein;


    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public float getStdEnergy() {
        return stdEnergy;
    }

    public void setStdEnergy(float stdEnergy) {
        this.stdEnergy = stdEnergy;
    }

    public float getStdProtein() {
        return stdProtein;
    }

    public void setStdProtein(float stdProtein) {
        this.stdProtein = stdProtein;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public float getUnitEnergy() {
        return unitEnergy;
    }

    public void setUnitEnergy(float unitEnergy) {
        this.unitEnergy = unitEnergy;
    }

    public float getUnitProtein() {
        return unitProtein;
    }

    public void setUnitProtein(float unitProtein) {
        this.unitProtein = unitProtein;
    }
}
