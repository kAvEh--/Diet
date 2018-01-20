package ir.eynakgroup.diet.network.response_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eynak_PC2 on 9/6/2017.
 */

public class UpdateResponse extends CommonResponse {

    @SerializedName("apikey")
    private String apikey;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String api) {
        this.apikey = api;
    }
}
