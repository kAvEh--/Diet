package ir.eynakgroup.diet.database.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Shayan on 7/12/2017.
 */
@DatabaseTable(tableName = "Diets")
public class Diet {
    @DatabaseField(columnName = "serverId", dataType = DataType.STRING)
    private String serverId;
    @DatabaseField(columnName = "_id", dataType = DataType.INTEGER)
    private int id;
    @DatabaseField(columnName = "startDate", dataType = DataType.STRING)
    private String startDate;
    @DatabaseField(columnName = "day", dataType = DataType.INTEGER)
    private int day;
    @DatabaseField(columnName = "dietType", dataType = DataType.STRING)
    private String dietType;
    @DatabaseField(columnName = "breakfastPackage1", dataType = DataType.STRING)
    private String breakfastPack1;
    @DatabaseField(columnName = "lunchPackage1", dataType = DataType.STRING)
    private String lunchPack1;
    @DatabaseField(columnName = "snackPackage1", dataType = DataType.STRING)
    private String snackPack1;
    @DatabaseField(columnName = "dinnerPackage1", dataType = DataType.STRING)
    private String dinnerPack1;
    @DatabaseField(columnName = "breakfastPackage2", dataType = DataType.STRING)
    private String breakfastPack2;
    @DatabaseField(columnName = "lunchPackage2", dataType = DataType.STRING)
    private String lunchPack2;
    @DatabaseField(columnName = "snackPackage2", dataType = DataType.STRING)
    private String snackPack2;
    @DatabaseField(columnName = "dinnerPackage2", dataType = DataType.STRING)
    private String dinnerPack2;
    @DatabaseField(columnName = "breakfastPackage3", dataType = DataType.STRING)
    private String breakfastPack3;
    @DatabaseField(columnName = "lunchPackage3", dataType = DataType.STRING)
    private String lunchPack3;
    @DatabaseField(columnName = "snackPackage3", dataType = DataType.STRING)
    private String snackPack3;
    @DatabaseField(columnName = "dinnerPackage3", dataType = DataType.STRING)
    private String dinnerPack3;
    @DatabaseField(columnName = "selectedBreakfast", dataType = DataType.STRING)
    private String selectedBreakfast;
    @DatabaseField(columnName = "selectedLunch", dataType = DataType.STRING)
    private String selectedLunch;
    @DatabaseField(columnName = "selectedSnack", dataType = DataType.STRING)
    private String selectedSnack;
    @DatabaseField(columnName = "selectedDinner", dataType = DataType.STRING)
    private String selectedDinner;
    @DatabaseField(columnName = "startWeight", dataType = DataType.STRING)
    private String startWeight;
    @DatabaseField(columnName = "goalWeight", dataType = DataType.STRING)
    private String goalWeight;


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDietType() {
        return dietType;
    }

    public void setDietType(String dietType) {
        this.dietType = dietType;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getBreakfastPack1() {
        return breakfastPack1;
    }

    public void setBreakfastPack1(String breakfastPack1) {
        this.breakfastPack1 = breakfastPack1;
    }

    public String getLunchPack1() {
        return lunchPack1;
    }

    public void setLunchPack1(String lunchPack1) {
        this.lunchPack1 = lunchPack1;
    }

    public String getSnackPack1() {
        return snackPack1;
    }

    public void setSnackPack1(String snackPack1) {
        this.snackPack1 = snackPack1;
    }

    public String getDinnerPack1() {
        return dinnerPack1;
    }

    public void setDinnerPack1(String dinnerPack1) {
        this.dinnerPack1 = dinnerPack1;
    }

    public String getBreakfastPack2() {
        return breakfastPack2;
    }

    public void setBreakfastPack2(String breakfastPack2) {
        this.breakfastPack2 = breakfastPack2;
    }

    public String getLunchPack2() {
        return lunchPack2;
    }

    public void setLunchPack2(String lunchPack2) {
        this.lunchPack2 = lunchPack2;
    }

    public String getSnackPack2() {
        return snackPack2;
    }

    public void setSnackPack2(String snackPack2) {
        this.snackPack2 = snackPack2;
    }

    public String getDinnerPack2() {
        return dinnerPack2;
    }

    public void setDinnerPack2(String dinnerPack2) {
        this.dinnerPack2 = dinnerPack2;
    }

    public String getBreakfastPack3() {
        return breakfastPack3;
    }

    public void setBreakfastPack3(String breakfastPack3) {
        this.breakfastPack3 = breakfastPack3;
    }

    public String getLunchPack3() {
        return lunchPack3;
    }

    public void setLunchPack3(String lunchPack3) {
        this.lunchPack3 = lunchPack3;
    }

    public String getSnackPack3() {
        return snackPack3;
    }

    public void setSnackPack3(String snackPack3) {
        this.snackPack3 = snackPack3;
    }

    public String getDinnerPack3() {
        return dinnerPack3;
    }

    public void setDinnerPack3(String dinnerPack3) {
        this.dinnerPack3 = dinnerPack3;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getStartWeight() {
        return startWeight;
    }

    public void setStartWeight(String startWeight) {
        this.startWeight = startWeight;
    }

    public String getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(String goalWeight) {
        this.goalWeight = goalWeight;
    }

}
