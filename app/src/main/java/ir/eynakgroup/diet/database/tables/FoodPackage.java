package ir.eynakgroup.diet.database.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Shayan on 7/29/2017.
 */

@DatabaseTable(tableName = "FoodPackages")
public class FoodPackage {
    @DatabaseField(columnName = "_id", dataType = DataType.STRING)
    private String id;
    @DatabaseField(columnName = "mealId", dataType = DataType.INTEGER)
    private int mealID;
    @DatabaseField(columnName = "packageId", dataType = DataType.INTEGER)
    private int packageId;
    @DatabaseField(columnName = "hatedList", dataType = DataType.STRING)
    private String hatedList;
    @DatabaseField(columnName = "updatedAt", dataType = DataType.STRING)
    private String updatedAt;
    @DatabaseField(columnName = "categoryId", dataType = DataType.INTEGER)
    private int categoryId;
    @DatabaseField(columnName = "deleted", dataType = DataType.INTEGER)
    private int deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMealID() {
        return mealID;
    }

    public void setMealID(int mealID) {
        this.mealID = mealID;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getHatedList() {
        return hatedList;
    }

    public void setHatedList(String hatedList) {
        this.hatedList = hatedList;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
