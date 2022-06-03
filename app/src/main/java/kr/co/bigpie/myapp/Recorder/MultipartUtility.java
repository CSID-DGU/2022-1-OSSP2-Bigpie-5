package kr.co.bigpie.myapp.Recorder;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MultipartUtility {
    private final String boundary ;
    private static final String LINE_FEED= "\r\n";
    private HttpURLConnection httpConn;
    private OutputStream outputStream;
    /**
     * HTTP POST 요청
     * @param requestURL
     * @throws IOException
     */
    public MultipartUtility(String requestURL) throws IOException {
        boundary = "===" + System.currentTimeMillis() + "===";
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        outputStream = httpConn.getOutputStream();
    }
    /**
     * 요청에 따라 파일 업로드
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile)throws IOException{
        String fileName = uploadFile.getName();
        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
            Log.d("sending", buffer.toString());
        }
        outputStream.flush();;
        inputStream.close();
    }
    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public String finish() throws IOException{
        String response = new String();
        //서버 코드 확인 필요
        int status = httpConn.getResponseCode();;
        if (status == HttpURLConnection.HTTP_OK){
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            response=reader.readLine();
            reader.close();
            httpConn.disconnect();
        }else{
            throw new IOException("Server returned non-OK status " + status);
        }
        return response;
    }
}
