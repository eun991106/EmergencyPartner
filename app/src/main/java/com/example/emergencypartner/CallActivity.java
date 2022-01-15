package com.example.emergencypartner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CallActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        boolean cancel=false;
        final int count = 10;
        Button btn = (Button) findViewById(R.id.btn);
        final TextView text = (TextView) findViewById(R.id.count);

        final CountDownTimer timer = new CountDownTimer(count * 1000, 1000) {
            int run = count;

            @Override
            public void onTick(long l) {
                text.setText(String.valueOf(run));
                run = run - 1;
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(CallActivity.this, CallCompleteActivity.class);
                startActivity(intent);
                onPause();
                finish();
            }

        };
        timer.start();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                Intent intent = new Intent(CallActivity.this, HomeActivity.class);
                startActivity(intent);
                onPause();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }


/*
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }
*/
}
