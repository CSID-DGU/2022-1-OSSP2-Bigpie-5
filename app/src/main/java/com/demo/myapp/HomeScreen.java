package com.demo.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.demo.myapp.Forms.LoginActivity;
import com.demo.myapp.Forms.UpdateActivity;
import com.demo.loginregisterretrofit.R;

public class HomeScreen extends AppCompatActivity {
    Button logout, edit;
    TextView text_userid, text_email, text_fname, text_lname;
    String str_email, str_fname, str_lname, str_userid, str_passowrd;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        str_userid = sharedPreferences.getString(LoginActivity.USER_ID, "");
        str_fname = sharedPreferences.getString(LoginActivity.FNAME, "");
        str_lname = sharedPreferences.getString(LoginActivity.LNAME, "");
        str_email = sharedPreferences.getString(LoginActivity.EMAIL, "");

        text_userid = (TextView) findViewById(R.id.user_id);
        text_email = (TextView) findViewById(R.id.user_email);
        text_fname = (TextView) findViewById(R.id.user_fname);
        text_lname = (TextView) findViewById(R.id.user_lname);
        logout = findViewById(R.id.logout_btn);
        edit = findViewById(R.id.edit);

        text_userid.setText("UserId: " + str_userid);
        text_email.setText("Email: " + str_email);
        text_fname.setText("First Name: " + str_fname);
        text_lname.setText("Last Name:  " + str_lname);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
                startActivity(intent);
            }
        });

    }

    private void logout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.confirmation);
        dialog.setMessage(R.string.logout_confirmation_text);
        dialog.setNegativeButton(R.string.CANCEL, null);
        dialog.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SessionOut();
            }
        });
        dialog.setCancelable(false);
        dialog.show();

    }

    private void SessionOut() {
        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(LoginActivity.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }


}
