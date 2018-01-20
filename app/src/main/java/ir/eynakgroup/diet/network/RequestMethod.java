package ir.eynakgroup.diet.network;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import ir.eynakgroup.diet.network.response_models.DietListResponse;
import ir.eynakgroup.diet.network.response_models.LoginResponse;
import ir.eynakgroup.diet.network.response_models.CommonResponse;
import ir.eynakgroup.diet.network.response_models.CreateDietResponse;
import ir.eynakgroup.diet.network.response_models.UpdateResponse;
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
    Call<LoginResponse> login(@Field("username") String phoneNumber, @Field("password") String password, @Field("android") String status, @Field("version") String version, @Field("market") String market, @Field("firebaseToken") String firebaseToken, @Field("deviceModel") String deviceModel);

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
    @POST("/onLogin")
    Call<UpdateResponse> onLogin(@Header("cookie") String cookie, @Field("userId") String userId, @Field("apikey") String apiKey, @Field("version") String version, @Field("market") String market, @Field("firebaseToken") String firebaseToken);

    @FormUrlEncoded
    @POST("/diet/new")
    Call<CreateDietResponse> createDiet(@Header("cookie") String cookie, @Field("userId") String userId, @Field("apikey") String apiKey, @Field("type") String type, @Field("startDate") String startDate, @Field("diet") String diet, @Field("startWeight") String sWeight, @Field("goalWeight") String gWeight);

    @FormUrlEncoded
    @POST("/diet/purchase")
    Call<CommonResponse> purchaseSend(@Header("cookie") String cookie, @Field("apikey") String apiKey, @Field("userId") String userId, @Field("package_name") String pName, @Field("product_id") String pId, @Field("token") String token);

    @FormUrlEncoded
    @POST("/diet/list")
    Call<List<DietListResponse>> getDietList(@Header("cookie") String cookie, @Field("userId") String userId, @Field("apikey") String apiKey);

    @FormUrlEncoded
    @POST("/diet/change-status")
    Call<CommonResponse> changeStatus(@Header("cookie") String cookie, @Field("apikey") String apiKey, @Field("userId") String userId,
                                      @Field("dietId") String dietId, @Field("day") int day, @Field("mealId") int mealId,
                                      @Field("status") int status, @Field("selected") String selected);

    @FormUrlEncoded
    @POST("/profile/edit")
    Call<CommonResponse> editProfile(@Header("cookie") String cookie, @Field("userId") String userId, @Field("apikey") String apiKey,
                                     @Field("email") String email, @Field("sex") int sex, @Field("bday") String bday,
                                     @Field("height") float height, @Field("weight") float weight,
                                     @Field("age") int age, @Field("activityLevel") int activityLevel,
                                     @Field("deviceModel") String deviceModel, @Field("diseases") String diseases,
                                     @Field("version") String version, @Field("market") String market,
                                     @Field("firebasetoken") String firebasetoken);
}