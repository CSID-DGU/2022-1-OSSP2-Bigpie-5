package kr.co.bigpie.myapp.Recorder;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

public class AudioSender implements Callable<String> {

    String outputFile;

    AudioSender(String fileLocation) {outputFile=fileLocation; }

    public String call(){
        File output = new File(outputFile);
        String response = new String();
        try {
            MultipartUtility multipart = new MultipartUtility("http://localhost:8080");
            multipart.addFilePart("audio", output);
            response = multipart.finish();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            {
                Log.d("SR", "SERVER REPLIED");
                return response;
            }
        }
    }
}
