package ir.eynakgroup.diet.network.response_models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Eynak_PC2 on 12/11/2017.
 *
 */

public class DietListResponse extends CommonResponse {

    @SerializedName("_id")
    private String id;

    @SerializedName("userId")
    private int userId;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("diet")
    private JsonObject dietData;

    @SerializedName("type")
    private int type;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("startWeight")
    private int startWeight;

    @SerializedName("goalWeight")
    private int goalWeight;

    @SerializedName("point")
    private int point;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public JsonObject getDietData() {
        return dietData;
    }

    public void setDietData(JsonObject dietData) {
        this.dietData = dietData;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getStartWeight() {
        return startWeight;
    }

    public void setStartWeight(int startWeight) {
        this.startWeight = startWeight;
    }

    public int getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(int goalWeight) {
        this.goalWeight = goalWeight;
    }
}
