package kr.co.bigpie.myapp.Letter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import kr.co.bigpie.myapp.R;

// import android.preference.PreferenceManager;

public class LetterSender extends AppCompatActivity {

    //PreferenceManager pref;
    Button back_btn; //여기서부터 , 뷰가 종료됨
    Button save_btn; //저장버튼
    EditText title;
    EditText content;//여기까지 이용

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        title = findViewById(R.id.memo_title_edit);
        content = findViewById(R.id.memo_content_edit);
        back_btn = findViewById(R.id.back_btn);
        save_btn = findViewById(R.id.save_btn);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //스레드 안에서 send() 메소드 호출
               final String urlStr1 = title.getText().toString();
               final String urlStr2 = content.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request(urlStr1);
                        request(urlStr2);
                    }
                }).start();
            }
        });

    }

    public void request(String urlStr){
        StringBuilder output = new StringBuilder();
        try{
            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(conn != null){
                conn.setConnectTimeout(10000); //연결 대기 시간 설정
                conn.setRequestMethod("GET");
                conn.setDoOutput(true); //객체의 입력이 가능하도록 만들어줌

                int resCode = conn.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while(true){
                    line = reader.readLine();
                    if(line == null){
                        break;
                    }
                    output.append(line+"\n");
                }
                reader.close();
                conn.disconnect();
            }
        }catch (Exception ex){
            println("에외 발생함: " + ex.toString());
        }
        println("응답 -> " + output.toString());
    }

    public void println(final String data){
        handler.post(new Runnable() {
            @Override
            public void run() {
                title.append(data + "\n");
                content.append(data + "\n");
            }
        });
    }

}