package kr.co.bigpie.flying;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitInterface {
    //api를 관리해주는 인터페이스
    @Multipart
    @POST("/upload2")
    Call<ResponseBody> wavUpload(@Part MultipartBody.Part wav);

//    @Multipart
//    @POST("/voice")
//    Call<ResponseBody> synthesizedResult(@Part MultipartBody.Part sentence);

    @FormUrlEncoded
    @POST("/voice")
    Call<ResponseBody> synthesizedResult(@Field("text") String text);
}
