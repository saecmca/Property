package com.mani.property.webservice;

import com.mani.property.boards.BoardResp;
import com.mani.property.favourite.FavResp;
import com.mani.property.favourite.FavouriteReq;
import com.mani.property.home.PropertyResp;
import com.mani.property.searches.DeleteSearch;
import com.mani.property.searches.SaveSearchReq;
import com.mani.property.searches.SavedResp;
import com.mani.property.userdetails.DemoResp;
import com.mani.property.userdetails.SigninRequest;
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

        @Headers({
                "Content-Type: application/json","Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"
        })

        @POST("/api/v1/users/sign_in")
        Call<SigninResponse> getSignin(@Body UserRequest signinRequest);

      //facebook
                @Headers({
                "Content-Type: application/json","Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"
        })

        @POST("/api/v1/users/facebook")
        Call<SigninResponse> getFacebookSignin(@Body SigninRequest signinRequest);

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

        @Headers({
                "Content-Type: application/json","Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"
        })
        @POST("/api/v1/properties")
        Call<PropertyResp> getPrpertyList(@Body UserRequest signinRequest);

        @Headers({
                "Content-Type: application/json","Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"
        })
        @POST("/api/v1/properties/change_favorite_property")
        Call<FavResp> getSavePropety(@Body FavouriteReq favouriteReq);

        @Headers({
                "Content-Type: application/json","Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"
        })
        @POST("/api/v1/properties/favorites")
        Call<PropertyResp> getFavPrpertyList(@Body UserRequest signinRequest);


        @Headers({
                "Content-Type: application/json","Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"
        })
        @POST("/api/v1/properties/save_search_list")
        Call<SavedResp> getSavedSearch(@Body UserRequest signinRequest);

        //save search
        @Headers({
                "Content-Type: application/json","Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"
        })
        @POST("/api/v1/properties/save_search")
        Call<PropertyResp> getSaveSearch(@Body SaveSearchReq searchReq);

        //save search
        @Headers({
                "Content-Type: application/json","Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"
        })
        @POST("/api/v1/properties/delete_search")
        Call<FavResp> deletesearch(@Body DeleteSearch deleteSearch);

        //save search
        @Headers({
                "Content-Type: application/json","Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"
        })
        @POST("/api/v1/landing")
        Call<DemoResp> getDemo();

        //save search
        @Headers({
                "Content-Type: application/json","Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"
        })
        @POST("/api/v1/boards/collections")
        Call<BoardResp> getBoard(@Body UserRequest userRequest);

        @Headers({
                "Content-Type: application/json","Api-Token: 08d0906bb2579eca5c590ffbd447857419b356a194cdf39dfa6dabc35529734a"
        })
        @POST("/api/v1/boards/delete_board_property")
        Call<BoardResp> delBoardProperty(@Body UserRequest delBoardReq);

    }
}
