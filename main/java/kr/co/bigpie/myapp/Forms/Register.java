package kr.co.bigpie.myapp.Forms;

import static kr.co.bigpie.myapp.Response.API.BaseURL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.util.regex.Pattern;

import kr.co.bigpie.myapp.R;
import kr.co.bigpie.myapp.Response.API;
import kr.co.bigpie.myapp.Response.RegisterResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Register extends AppCompatActivity {
    private EditText et_fname, et_lname, et_email, etpassword;
    private FloatingActionButton btnregister;
    private TextView tvlogin;
    String firstName;
    String lastName;
    String email;
    String password;
    LinearLayout lyt_linear;
    ImageView u_image;
    Pattern pattern_pwd = Pattern.compile("^[a-zA-Z0-9]+$");

    public Register() {
    }

    protected void onCreate(Bundle savedInstanceState, String jsonresponse) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_register);

        et_fname = (EditText) findViewById(R.id.et_fname);
        et_lname = (EditText) findViewById(R.id.et_lname);
        et_email = (EditText) findViewById(R.id.et_email);
        etpassword = (EditText) findViewById(R.id.etpassword);
        btnregister = findViewById(R.id.btn);
        u_image = findViewById(R.id.u_image);
        lyt_linear = findViewById(R.id.lyt_linear);
        tvlogin = (TextView) findViewById(R.id.tvlogin);

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, LoginActivity.class);
                startActivity(intent);
                Register.this.finish();
            }
        });
        u_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, LoginActivity.class);
                startActivity(intent);
                Register.this.finish();
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = et_fname.getText().toString();
                lastName = et_lname.getText().toString();
                email = et_email.getText().toString();
                password = etpassword.getText().toString();

                Log.d("userdata", "onClick: " + email + password);
                registerMe();
                //응답이 없는 경우
                //response server
//                if (!firstName.isEmpty()) {
//                    if (!lastName.isEmpty()) {
//                        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//
//                            if (!password.isEmpty() && pattern_pwd.matcher(password).matches()) {
//
//                            } else {
//                                Snackbar.make(lyt_linear, "Enter the Valid Password", Snackbar.LENGTH_SHORT).show();
//
//                            }
//                        } else {
//                            Snackbar.make(lyt_linear, "Enter the Valid Email", Snackbar.LENGTH_SHORT).show();
//
//                        }
//                    } else {
//                        Snackbar.make(lyt_linear, "Enter the Valid Last name", Snackbar.LENGTH_SHORT).show();
//
//                    }
//                } else {
//                    Snackbar.make(lyt_linear, "Enter the Valid First name", Snackbar.LENGTH_SHORT).show();
//
//                }

            }
        });

    }

    protected void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.ic_gradient_bg);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    private void registerMe(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);
        Call<RegisterResponse> call = api.getUserRegi(firstName, lastName, email, password);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("responseLog", response.body().getResponse());
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().getResponse();
                        try {
                            parseRegData(jsonresponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response"); //toast
                    }
                }
            }

        @Override
        public void onFailure(Call<RegisterResponse> call, Throwable t){
            Log.d("go", "onFailure" + t.toString());
        }
        });
    }


    private void parseRegData(String response) throws JSONException {
        Log.d("juststring", response);
        if (response.equals("success")) {
            Toast.makeText(Register.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Register.this, LoginActivity.class);
            finish();
            startActivity(intent);
        } else {
            Toast.makeText(Register.this, "OOPS", Toast.LENGTH_SHORT).show();
        }
    }

//    private void saveInfo(String response) {
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            if (jsonObject.getString("response").contains("username")) {
//                JSONArray dataArray = jsonObject.getJSONArray("response");
//                for (int i = 0; i < dataArray.length(); i++) {
//                    JSONObject dataobj = dataArray.getJSONObject(i);
//                    Log.d("TAG", "saveInfo: " + dataobj.toString());
//
//                    sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
//                    editor = sharedPreferences.edit();
//                    editor.putString(USER_ID, dataobj.getString("user_id"));
//                    editor.putString(NAME, dataobj.getString("name"));
//                    editor.putString(HOBBY, dataobj.getString("hobby"));
//                    editor.putString(PASSWORD, dataobj.getString("password"));
//                    editor.putString(USER_NAME, dataobj.getString("username"));
//                    editor.apply();
//                    Log.d("kon", "saveInfo: " + USER_NAME);
//
//                }
//                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}