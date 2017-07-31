package ir.eynakgroup.diet.database.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Shayan on 7/29/2017.
 */
@DatabaseTable(tableName = "PackageFoods")
public class PackageFood {
    @DatabaseField(columnName = "_id", dataType = DataType.STRING)
    private String id;

    @DatabaseField(columnName = "foodId", dataType = DataType.INTEGER)
    private int foodId;

    @DatabaseField(columnName = "amount1000", dataType = DataType.STRING)
    private String amount1000;

    @DatabaseField(columnName = "amount1250", dataType = DataType.STRING)
    private String amount1250;

    @DatabaseField(columnName = "amount1500", dataType = DataType.STRING)
    private String amount1500;

    @DatabaseField(columnName = "amount1750", dataType = DataType.STRING)
    private String amount1750;

    @DatabaseField(columnName = "amount2000", dataType = DataType.STRING)
    private String amount2000;

    @DatabaseField(columnName = "amount2250", dataType = DataType.STRING)
    private String amount2250;

    @DatabaseField(columnName = "isStandard", dataType = DataType.INTEGER)
    private int isStandard;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getAmount1000() {
        return amount1000;
    }

    public void setAmount1000(String amount1000) {
        this.amount1000 = amount1000;
    }

    public String getAmount1250() {
        return amount1250;
    }

    public void setAmount1250(String amount1250) {
        this.amount1250 = amount1250;
    }

    public String getAmount1500() {
        return amount1500;
    }

    public void setAmount1500(String amount1500) {
        this.amount1500 = amount1500;
    }

    public String getAmount1750() {
        return amount1750;
    }

    public void setAmount1750(String amount1750) {
        this.amount1750 = amount1750;
    }

    public String getAmount2000() {
        return amount2000;
    }

    public void setAmount2000(String amount2000) {
        this.amount2000 = amount2000;
    }

    public String getAmount2250() {
        return amount2250;
    }

    public void setAmount2250(String amount2250) {
        this.amount2250 = amount2250;
    }

    public int getIsStandard() {
        return isStandard;
    }

    public void setIsStandard(int isStandard) {
        this.isStandard = isStandard;
    }
}
