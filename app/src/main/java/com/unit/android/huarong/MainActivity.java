package com.unit.android.huarong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGame(View view) {
        Intent intent = new Intent(MainActivity.this, chooseActivity.class);
        startActivity(intent);
    }

    public void practice(View view) {
        Intent intent = new Intent(MainActivity.this, custom.class);
        startActivity(intent);
    }
}
