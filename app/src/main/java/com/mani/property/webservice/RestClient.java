package com.mani.property.webservice;

import com.mani.property.userdetails.SigninResponse;
import com.mani.property.userdetails.UserRequest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by SelvaGanesh on 25-04-2017.
 */

public class RestClient {

    public static APIInterface apiInterface;
    public static String BASE_URL = "http://45.56.80.177/";
    public static String api_Token="08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a";


    public static APIInterface getapiclient() {

        if (apiInterface == null) {

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


            //OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
            OkHttpClient okHttpClient1 = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(130, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(httpLoggingInterceptor)
                    .build();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .client(okHttpClient1)
                    .addConverterFactory(GsonConverterFactory.create()).build();

            apiInterface = retrofit.create(APIInterface.class);
        }

        return apiInterface;
    }


    public interface APIInterface {
         //  "Api-Token", "08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"
        //@Headers("Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a")
        //@Headers("Content-Type: application/json")
        @Headers({
                "Content-Type: application/json","Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"
        })

        @POST("/api/v1/users/sign_in")
        Call<SigninResponse> getSignin(@Body UserRequest signinRequest);

        @Headers({"Content-Type: application/json","Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"})
        @POST("/api/v1/users/sign_up")
        Call<SigninResponse> getRegister(@Body UserRequest signinRequest);

        @Headers({
                "Content-Type: application/json","Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"
        })
        @POST("/api/v1/users/reset_password")
        Call<SigninResponse> getForgot(@Body UserRequest signinRequest);
        @Headers({
                "Content-Type: application/json","Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"
        })
        @PUT("/api/v1/users")
        Call<SigninResponse> getProfileUpdate(@Body UserRequest signinRequest);
    }
}
