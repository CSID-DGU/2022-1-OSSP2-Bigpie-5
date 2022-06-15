package kr.co.bigpie.flying;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import kr.co.bigpie.flying.Letter.ListActivity;
import kr.co.bigpie.flying.Letter.WriteActivity;
import kr.co.bigpie.flying.Recorder.VoiceRecord;


public class MainActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 10; //외부 파일 읽기 권한 요청 request 값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 편지쓰기 클릭시 recording으로 전환
        Button go_recording_button = (Button) findViewById(R.id.go_recording);
        go_recording_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VoiceRecord.class);
                startActivity(intent);
            }
        });

        // 편지쓰기 클릭시 Write_letter로 전환
        Button go_writing_button = (Button) findViewById(R.id.go_writing);
        go_writing_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(intent);
            }
        });

        // 목록보기 클릭시 List_letter로 전환
        Button go_list_button = (Button) findViewById(R.id.go_list);
        go_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
            }
        });

        //mp3 파일을 스마트폰에서 읽어오기 위해 권한 요청하기
        //외부저장소 읽기/쓰기, 사진, 카메라 권한 부여 확인 및 권한요청 코드
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d("permission","checkSelfPermission");
            // 설명이 필요한가?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Log.d("permission","shouldShowRequestPermissionRationale");
                // 사용자에게 설명을 보여줍니다.
                // 권한 요청을 다시 시도합니다.

            } else {
                // 권한요청

                Log.d("permission","권한 요청");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            }
        }
    }
    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }
}