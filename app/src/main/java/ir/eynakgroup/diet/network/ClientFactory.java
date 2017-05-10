package ir.eynakgroup.diet.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shayan on 3/9/2017.
 */
public class ClientFactory {
    public static final String BASE_URL = "http://eynakgroup.com:7799";
    private static Retrofit retrofit = null;

    private ClientFactory(){}

    public static Retrofit getClientInstance(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}