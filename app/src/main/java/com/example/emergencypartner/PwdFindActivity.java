package com.example.emergencypartner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PwdFindActivity extends AppCompatActivity {
    private EditText EmailTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_find);
        EmailTxt = (EditText)findViewById(R.id.EmailsTxt);

    }

    public void goToLogin(View view) {
        Intent intent = new Intent( PwdFindActivity.this, LoginActivity.class );
        startActivity( intent );
        onPause();
        finish();
    }

    public void sendmail(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = EmailTxt.getText().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "비밀번호 변경 이메일이 전송되었습니다.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent( PwdFindActivity.this, LoginActivity.class );
                            startActivity( intent );
                        } else{
                            Toast.makeText(getApplicationContext(), "이메일 전송에 실패하였습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void goToPrevious(View view) {
        super.onBackPressed();
        onPause();
        finish();
    }

}
