package com.example.barna.odusafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by barna on 9/9/2017.
 */

public class Signup extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void OnSendUserProfile(View view) {
        Intent dashboard = new Intent (Signup.this,Login.class);
        startActivity(dashboard);
    }
}
