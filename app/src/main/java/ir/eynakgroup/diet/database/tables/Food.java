package ir.eynakgroup.diet.database.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Shayan on 4/30/2017.
 */
@DatabaseTable(tableName = "Foods")
public class Food {
    @DatabaseField(columnName = "_id", dataType = DataType.STRING)
    private String id;
    @DatabaseField(columnName = "foodId", dataType = DataType.INTEGER)
    private int foodId;
    @DatabaseField(columnName = "foodName", dataType = DataType.STRING)
    private String foodName;
    @DatabaseField(columnName = "secondUnitId", dataType = DataType.INTEGER)
    private int unitId;
    @DatabaseField(columnName = "isPacked", dataType = DataType.INTEGER)
    private int isPacked;
    @DatabaseField(columnName = "deleted", dataType = DataType.INTEGER)
    private int deleted;


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

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsPacked() {
        return isPacked;
    }

    public void setIsPacked(int isPacked) {
        this.isPacked = isPacked;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}
