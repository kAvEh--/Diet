package ir.eynakgroup.diet.network;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import ir.eynakgroup.diet.network.response_models.LoginResponse;
import ir.eynakgroup.diet.network.response_models.CommonResponse;
import ir.eynakgroup.diet.network.response_models.CreateDietResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Shayan on 3/9/2017.
 */
public interface RequestMethod {

    @FormUrlEncoded
    @POST("/login")
    Call<LoginResponse> login(@Field("username") String phoneNumber, @Field("password") String password, @Field("android") String status);

    @FormUrlEncoded
    @POST("/profile/forgot-password-req")
    Call<Void> forgotPass(@Field("username") String phoneNumber);

    @FormUrlEncoded
    @POST("/profile/forgot-password-code-check")
    Call<CommonResponse> forgotPassCode(@Field("username") String phoneNumber, @Field("code") String code);

    @FormUrlEncoded
    @POST("/profile/reset-password")
    Call<CommonResponse> resetPass(@Field("username") String phoneNumber, @Field("code") String code, @Field("newPassword") String newPass);

    @FormUrlEncoded
    @POST("/diet/new")
    Call<CreateDietResponse> createDiet(@Header("cookie") String cookie, @Field("userId") String userId, @Field("apikey") String apiKey, @Field("type") String type, @Field("startDate") String startDate, @Field("diet") String diet);

    @FormUrlEncoded
    @POST("/diet/purchase")
    Call<CommonResponse> purchaseSend(@Header("cookie") String cookie, @Field("apikey") String apiKey, @Field("userId") String userId, @Field("package_name") String pName, @Field("product_id") String pId, @Field("token") String token);


}
