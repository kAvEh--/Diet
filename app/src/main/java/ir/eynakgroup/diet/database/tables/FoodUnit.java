package ir.eynakgroup.diet.database.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Shayan on 7/11/2017.
 */

@DatabaseTable(tableName = "Food_Units")
public class FoodUnit {

    @DatabaseField(columnName = "Unit_ID", dataType = DataType.INTEGER)
    private int unitId;
    @DatabaseField(columnName = "Unit_Name", dataType = DataType.STRING)
    private String unitName;

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
