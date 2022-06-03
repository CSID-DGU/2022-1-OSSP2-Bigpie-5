package kr.co.bigpie.myapp.LoginResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateResponse {
    @SerializedName("user_id")
    @Expose()
    public String user_id;


    @SerializedName("name")
    @Expose()
    public String name;

    @SerializedName("email")
    @Expose()
    public String email;


    public String getEmail() {
        return email;
    }

    public String get_name() {
        return name;
    }

    public String getUser_id() {
        return user_id;
    }
}
