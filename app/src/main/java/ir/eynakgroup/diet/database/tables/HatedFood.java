package ir.eynakgroup.diet.database.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Shayan on 7/12/2017.
 */
@DatabaseTable(tableName = "Hated_Foods")
public class HatedFood {
    @DatabaseField(columnName = "_id", dataType = DataType.INTEGER)
    private int id;
    @DatabaseField(columnName = "Food_Name", dataType = DataType.STRING)
    private String foodName;

}
