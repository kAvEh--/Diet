package ir.eynakgroup.diet.network.response_models;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Shayan on 3/12/2017.
 */
public class User {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("sex")
    private int gender = -1;

    @SerializedName("bday")
    private String birthday;

    @SerializedName("userHeight")
    private float height = 0;

    @SerializedName("userWeight")
    private float weight = 0;

    @SerializedName("age")
    private int age = 0;

    @SerializedName("apikey")
    private String apiKey;

    @SerializedName("userId")
    private String userId;

    @SerializedName("activityLevel")
    private int activityLevel = -1;

    @SerializedName("registerDate")
    private String registerDate;

    @SerializedName("lastLoginDate")
    private String lastLoginDate;

    @SerializedName("diseases")
    private JsonArray diseases = new JsonArray();

//    @SerializedName("diseases")
//    private JSONArray diseases;

    @SerializedName("hatedFoods")
    private JsonArray hatedFoods = new JsonArray();

    @SerializedName("dietCount")
    private int credit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        if(gender == 0)
            return Gender.Male;

        if(gender == 1)
            return Gender.Female;

        return null;
    }

    public JsonArray getHatedFoods() {
        return hatedFoods;
    }

    public void setHatedFoods(JsonArray hatedFoods) {
        this.hatedFoods = hatedFoods;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public enum Gender{
        Male, Female
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public JsonArray getDiseases() {
        return diseases;
    }

    public void setDiseases(JsonArray diseases) {
        this.diseases = diseases;
    }

    @Override
    public String toString() {
        return getUserId()+" : " + getBirthday()+ " : " + getName();
    }
}
