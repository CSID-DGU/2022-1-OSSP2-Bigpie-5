package kr.co.bigpie.flying.Letter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import kr.co.bigpie.flying.R;

public class ViewActivity extends AppCompatActivity {
    private int REQUEST_RE = 202;

    PreferenceManager pref;
    TextView view_title;
    TextView view_content;

    String title;
    String content;

    // 음악 재생
    private MediaPlayer mediaPlayer;
    private Button btnPause;
    private TextView txtState;

    private int stateMediaPlayer;
    private final int STATE_NOTSTARTER = 0;
    private final int STATE_PLAYING = 1;
    private final int STATE_PAUSING = 2;
    private final int STATEMP_ERROR = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        pref = new PreferenceManager();
        view_title = findViewById(R.id.view_title);
        view_content = findViewById(R.id.view_content);

        // 인텐트로 리사이클러뷰 목록 하나의 키값을 받는다
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        String value = pref.getString(getApplication(),key);
        try {
            JSONObject jsonObject = new JSONObject(value);
            title = (String) jsonObject.getString("title");
            String content = (String) jsonObject.getString("content");

            view_title.setText(title);
            view_content.setText(content);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnPause = (Button) findViewById(R.id.btnPlay);
        txtState = (TextView) findViewById(R.id.state_text);

        btnPause.setOnClickListener(buttonPlayPauseOnClickListener);

        initMediaPlayer(title);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_RE) {
            if (resultCode == RESULT_OK) {
                String title = data.getStringExtra("title");
                String content = data.getStringExtra("content");
                view_title.setText(title);
                view_content.setText(content);
            }
        }
    }

    //  음악 실행
    private void initMediaPlayer(String title) {
        // 해당 파일 이름만 바꿔주는 코드 짜면 잘 돌아갈 듯
        // 예시 - String PATH_TO_FILE = Environment.getExternalStorageDirectory()+ "/Download/Road.mp3";
        // String PATH_TO_FILE = Environment.getExternalStorageDirectory()+ "/추가경로 download 추천/" + title + ".파일형식";
        // title은 음성 파일 명을 의미함 = 편지 제목이랑 동일
        // String PATH_TO_FILE = Environment.getExternalStorageDirectory()+ "/Android/data/org.techtown.bigfive_voiceletter/files/" + "1_audio" + ".wav";
        String PATH_TO_FILE = Environment.getExternalStorageDirectory()+ "/download/" + title + ".wav";

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(PATH_TO_FILE);
            mediaPlayer.prepare();
            Toast.makeText(this, PATH_TO_FILE, Toast.LENGTH_LONG).show();
            stateMediaPlayer = STATE_NOTSTARTER;
            txtState.setText("연결 완료");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            stateMediaPlayer = STATEMP_ERROR;
            txtState.setText("- 에러!!! -");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            stateMediaPlayer = STATEMP_ERROR;
            txtState.setText("- 에러!!! -");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            stateMediaPlayer = STATEMP_ERROR;
            txtState.setText("- 에러!!! -");
        }
    }

    Button.OnClickListener buttonPlayPauseOnClickListener = new Button.OnClickListener() {

        public void onClick(View v) {
            switch (stateMediaPlayer) {
                case STATE_NOTSTARTER:
                    mediaPlayer.start();
                    btnPause.setText("Pause");
                    //txtState.setText("- 실행 -");
                    stateMediaPlayer = STATE_PLAYING;
                    break;
                case STATE_PLAYING:
                    mediaPlayer.pause();
                    btnPause.setText("Play");
                    //txtState.setText("- 일시중지 -");
                    stateMediaPlayer = STATE_PAUSING;
                    break;
                case STATE_PAUSING:
                    mediaPlayer.start();
                    btnPause.setText("Pause");
                    //txtState.setText("- 실행중 -");
                    stateMediaPlayer = STATE_PLAYING;
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.pause(); // 일시정지
        mediaPlayer.release();  // 초기화
    }

    @Override
    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        mediaPlayer.pause(); // 일시정지
        btnPause.setText("Play");
        stateMediaPlayer = STATE_PAUSING;
    }
}