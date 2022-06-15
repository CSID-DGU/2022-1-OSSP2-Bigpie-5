package kr.co.bigpie.flying.Letter;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    public static final String PREFERENCES_NAME = "memo_contain";

    private static final String DEFAULT_VALUE_STRING = " ";
    private static final boolean DEFAULT_VALUE_BOOLEAN = false;
    private static final int DEFAULT_VALUE_INT = -1;
    private static final long DEFAULT_VALUE_LONG = -1L;
    private static final float DEFAULT_VALUE_FLOAT = -1F;

    private static SharedPreferences getPreferences(Context context) {

        return context.getSharedPreferences(PREFERENCES_NAME, context.MODE_PRIVATE);

    }

    /* String 값 저장
       param context
       param key
       param value
     */
    public static void setString(Context context, String key, String value){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /* String 값 로드
       param context
       param key
       return
     */
    public static String getString(Context context, String key){
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);
        return value;
    }


    /* 키 값 삭제
      param context
      param key
    */

    public static void removeKey(Context context, String key){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
    }

    /* 모든 저장 데이터 삭제
      param context
    */

    public static void clear(Context context){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

}
