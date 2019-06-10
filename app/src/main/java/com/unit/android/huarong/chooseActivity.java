package com.unit.android.huarong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class chooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
    }

    public void startPass(View view) {
        Intent intent = new Intent(chooseActivity.this, gamectivity.class);
        switch(view.getId()){
            case R.id.pass1:
                intent.putExtra("passId", "1");
                break;
            case R.id.pass2:
                intent.putExtra("passId", "2");
                break;
            case R.id.pass3:
                intent.putExtra("passId", "3");
                break;
            case R.id.pass4:
                intent.putExtra("passId", "4");
                break;
            case R.id.pass5:
                intent.putExtra("passId", "5");
                break;
            case R.id.pass6:
                intent.putExtra("passId", "6");
                break;
            case R.id.pass7:
                intent.putExtra("passId", "7");
                break;
            case R.id.pass8:
                intent.putExtra("passId", "8");
                break;
            case R.id.pass9:
                intent.putExtra("passId", "9");
                break;
            case R.id.pass10:
                intent.putExtra("passId", "10");
                break;
            case R.id.pass11:
                intent.putExtra("passId", "11");
                break;
            case R.id.pass12:
                intent.putExtra("passId", "12");
                break;
            default:
                intent.putExtra("passId", "1");
                break;
        }
        startActivity(intent);
    }
}
