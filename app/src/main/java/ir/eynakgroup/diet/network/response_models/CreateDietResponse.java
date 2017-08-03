package ir.eynakgroup.diet.network.response_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shayan on 8/2/2017.
 */

public class CreateDietResponse extends CommonResponse {

    @SerializedName("_id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
