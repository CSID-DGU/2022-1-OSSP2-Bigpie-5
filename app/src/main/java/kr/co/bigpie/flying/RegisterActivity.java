package kr.co.bigpie.flying;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.DefaultRetryPolicy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity{
    private EditText et_email, et_password;
    private Button go_login_button, registerButton;
    final static private String URL = "http://172.30.1.11:8080/signup";
    //localhost 자리에 ip 주소

    @Override
    protected void onCreate(Bundle savedInstanceState) { //액티비티 시작시 처음으로 실행
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //아이디값 찾아주기
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        go_login_button = findViewById(R.id.go_login_button);
        registerButton = findViewById(R.id.register_btn);

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText에 현재 입력되어있는 값을 가져온다.
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.v("Response:%n %s", response.toString(4));
                                    if(response.toString().equals("existing email")){
                                        Toast.makeText(getApplicationContext(),"이미 존재하는 회원정보입니다.",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "회원가입에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error: ", error.toString());
                    }
                });
                //커스텀 정책을 생성하여 지정한다.
                req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(req);
            }
        });

        //고로그인 버튼 누르면 다시 로그인 화면으로 넘어감
        go_login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
