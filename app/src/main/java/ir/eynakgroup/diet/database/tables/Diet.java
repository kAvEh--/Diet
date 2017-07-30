package ir.eynakgroup.diet.database.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Shayan on 7/12/2017.
 */
@DatabaseTable(tableName = "Diets")
public class Diet {
    @DatabaseField(columnName = "_id", dataType = DataType.INTEGER, generatedId = true)
    private int id;
    @DatabaseField(columnName = "startDate", dataType = DataType.STRING)
    private String startDate;
    @DatabaseField(columnName = "currentDate", dataType = DataType.INTEGER)
    private int currentDate;
    @DatabaseField(columnName = "dietType", dataType = DataType.STRING)
    private String dietType;
    @DatabaseField(columnName = "breakfastPackage", dataType = DataType.STRING)
    private String breakfastPack;
    @DatabaseField(columnName = "lunchPackage", dataType = DataType.STRING)
    private String lunchPack;
    @DatabaseField(columnName = "snackPackage", dataType = DataType.STRING)
    private String snackPack;
    @DatabaseField(columnName = "dinnerPackage", dataType = DataType.STRING)
    private String dinnerPack;
    @DatabaseField(columnName = "selectedBreakfast", dataType = DataType.STRING)
    private String selectedBreakfast;
    @DatabaseField(columnName = "selectedLunch", dataType = DataType.STRING)
    private String selectedLunch;
    @DatabaseField(columnName = "selectedSnack", dataType = DataType.STRING)
    private String selectedSnack;
    @DatabaseField(columnName = "selectedDinner", dataType = DataType.STRING)
    private String selectedDinner;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(int currentDate) {
        this.currentDate = currentDate;
    }

    public String getDietType() {
        return dietType;
    }

    public void setDietType(String dietType) {
        this.dietType = dietType;
    }

    public String getBreakfastPack() {
        return breakfastPack;
    }

    public void setBreakfastPack(String breakfastPack) {
        this.breakfastPack = breakfastPack;
    }

    public String getLunchPack() {
        return lunchPack;
    }

    public void setLunchPack(String lunchPack) {
        this.lunchPack = lunchPack;
    }

    public String getSnackPack() {
        return snackPack;
    }

    public void setSnackPack(String snackPack) {
        this.snackPack = snackPack;
    }

    public String getDinnerPack() {
        return dinnerPack;
    }

    public void setDinnerPack(String dinnerPack) {
        this.dinnerPack = dinnerPack;
    }

    public String getSelectedBreakfast() {
        return selectedBreakfast;
    }

    public void setSelectedBreakfast(String selectedBreakfast) {
        this.selectedBreakfast = selectedBreakfast;
    }

    public String getSelectedLunch() {
        return selectedLunch;
    }

    public void setSelectedLunch(String selectedLunch) {
        this.selectedLunch = selectedLunch;
    }

    public String getSelectedSnack() {
        return selectedSnack;
    }

    public void setSelectedSnack(String selectedSnack) {
        this.selectedSnack = selectedSnack;
    }

    public String getSelectedDinner() {
        return selectedDinner;
    }

    public void setSelectedDinner(String selectedDinner) {
        this.selectedDinner = selectedDinner;
    }

    public int get_id() {
        return id;
    }

    public void set_id(int id) {
        this.id = id;
    }
}
