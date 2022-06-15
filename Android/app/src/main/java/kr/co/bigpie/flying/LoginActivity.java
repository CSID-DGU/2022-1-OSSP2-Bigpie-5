package kr.co.bigpie.flying;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText etemail, etpassword;
    private Button login_button, go_register_button, go_skip_button;
    final static private String URL = "http://192.168.123.102:8080/login";
    //localhost 자리에 ip주소

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etemail = findViewById(R.id.etemail);
        etpassword = findViewById(R.id.etpassword);
        login_button = findViewById(R.id.login_button);
        go_register_button = findViewById(R.id.go_register_button);

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        //레지스터 버튼 클릭시 레지스터 페이지로 넘어감
        go_register_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼 클릭시 아무 변화 없음
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etemail.getText().toString();
                String password = etpassword.getText().toString();

                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.v("Response:%n %s", response.toString(4));
                            String message = response.getString("message");

                            if (message.equals("wrong password")) {
                                Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                            } else if (message.equals("wrong email")) {
                                Toast.makeText(getApplicationContext(), "아이디가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
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
                req.setRetryPolicy(new DefaultRetryPolicy(200 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(req);
        }
    });

        //스킵 버튼 클릭시 메인 페이지로 넘어감
        /*go_skip_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });*/
    }

}
