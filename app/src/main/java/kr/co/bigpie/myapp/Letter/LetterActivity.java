package kr.co.bigpie.myapp.Letter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import kr.co.bigpie.myapp.R;

public class LetterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        // 편지쓰기 클릭시 Write_letter로 전환
        Button developer_info_btn1 = (Button) findViewById(R.id.button7);
        developer_info_btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), LetterSender.class);
                startActivity(intent);
            }
        });

        // 목록보기 클릭시 List_letter로 전환
        Button developer_info_btn2 = (Button) findViewById(R.id.button8);
        developer_info_btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
            }
        });
    }
}
