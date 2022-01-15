package com.example.emergencypartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText emailtxt;
    private EditText pwdtxt;
    private String email1;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailtxt = findViewById(R.id.log_idtxt);
        pwdtxt = findViewById(R.id.log_pwtxt);

    }


    public void login() {
        email1 = emailtxt.getText().toString();
        password = pwdtxt.getText().toString();
        mAuth.signInWithEmailAndPassword(email1, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = null;
                            if(((CheckBox)findViewById(R.id.guardianlogincheckBox)).isChecked())
                                intent = new Intent( LoginActivity.this, GuardianHomeActivity.class );
                            else
                                intent = new Intent( LoginActivity.this, HomeActivity.class );


                            startActivity( intent );
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void goToAddMemberInfo(View view) {
       // Intent intent = new Intent( LoginActivity.this, AddMemberInfoActivity.class );

        Intent intent = new Intent( LoginActivity.this, MemberInfo1Activity.class );
        intent.putExtra("mod_ck",0);
        startActivity( intent );
        onPause();
        finish();
    }

    public void goToHome(View view) {
        if(emailtxt.getText().toString().equals("")||pwdtxt.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "아이디, 비밀번호를 모두 입력해주세요.", Toast.LENGTH_LONG).show();
        } else
        login();

    }
    public void findPwd(View view) {
        Intent intent = new Intent( LoginActivity.this, PwdFindActivity.class );
        startActivity( intent );
        onPause();
        finish();
    }
}