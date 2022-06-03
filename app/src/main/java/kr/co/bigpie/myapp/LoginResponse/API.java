package kr.co.bigpie.myapp.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {

    //String BaseURL = "http://192.168.43.207/digital_reader/";
    String BaseURL = "http://localhost:8080/login";

    //register

    @FormUrlEncoded
    @POST("register/php")
    <RegsiterResponse>
    Call<RegsiterResponse> getUserRegi(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );
 //Update user
    @FormUrlEncoded
    @POST("Update_user.php/{user_id}")
    Call<UpdateUserResponse> sentUserUpdateddata(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("user_id") String user_id
    );
    //login
    @FormUrlEncoded
    @POST("login.php")
    Call<LoginUserResponse> getUserLogin(
            @Field("email") String email,
            @Field("password") String password
    );
}