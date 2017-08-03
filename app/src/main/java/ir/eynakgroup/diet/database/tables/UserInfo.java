package ir.eynakgroup.diet.database.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.types.DoubleObjectType;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Shayan on 4/30/2017.
 */
@DatabaseTable(tableName = "Users")
public class UserInfo {
    @DatabaseField(columnName = "Activity_Level", dataType = DataType.INTEGER)
    private int activityLevel;
    @DatabaseField(columnName = "Age", dataType = DataType.INTEGER)
    private int age;
    @DatabaseField(columnName = "Api_Key", dataType = DataType.STRING)
    private String apiKey;
    @DatabaseField(columnName = "Birthday", dataType = DataType.STRING)
    private String birthday;
    @DatabaseField(columnName = "Disease", dataType = DataType.STRING)
    private String disease;
    @DatabaseField(columnName = "Email", dataType = DataType.STRING)
    private String email;
    @DatabaseField(columnName = "Height", dataType = DataType.STRING)
    private String height;
    @DatabaseField(columnName = "Last_Login_Date", dataType = DataType.STRING)
    private String lastLoginDate;
    @DatabaseField(columnName = "Register_Date", dataType = DataType.STRING)
    private String registerDate;
    @DatabaseField(columnName = "Name", dataType = DataType.STRING)
    private String name;
    @DatabaseField(columnName = "Gender", dataType = DataType.INTEGER)
    private int gender;
    @DatabaseField(columnName = "User_ID", dataType = DataType.STRING)
    private String userId;
    @DatabaseField(columnName = "Session_ID", dataType = DataType.STRING)
    private String sessionId;
    @DatabaseField(columnName = "Weight", dataType = DataType.STRING)
    private String weight;
    @DatabaseField(columnName = "Credit", dataType = DataType.INTEGER)
    private int credit;
    @DatabaseField(columnName = "hatedList", dataType = DataType.STRING)
    private String hatedList;
    @DatabaseField(columnName = "_id", dataType = DataType.STRING)
    private String id;




    public int getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getHeight() {
        return Float.valueOf(height.trim());
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public float getWeight() {
        return Float.valueOf(weight.trim());
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getHatedList() {
        return hatedList;
    }

    public void setHatedList(String hatedList) {
        this.hatedList = hatedList;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
