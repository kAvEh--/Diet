package ir.eynakgroup.diet.network.response_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shayan on 4/3/2017.
 */
public class CommonResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("err")
    private String error;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
