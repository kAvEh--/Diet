package ir.eynakgroup.diet.network.response_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shayan on 3/12/2017.
 */
public class LoginResponse extends CommonResponse {


    @SerializedName("user")
    private User user;

//    @SerializedName("updatedAt")
//    private Date date;
    @SerializedName("message")
    private String message;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
}
