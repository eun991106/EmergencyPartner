package com.example.emergencypartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MemberInfo4Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private EditText emailtxt;
    private EditText pwdtxt;
    private EditText re_pwd;
    private Button button;
    public String email;
    public String pw;
    int mod_ck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info_4);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        emailtxt = findViewById(R.id.idTxt);
        pwdtxt = findViewById(R.id.pwTxt);
        re_pwd = findViewById(R.id.pwTxt2);
        button = findViewById(R.id.next);
        Intent pre_intent = getIntent();
        mod_ck = pre_intent.getIntExtra("mod_ck",0);
        if(mod_ck ==1||mod_ck==2) {
            emailtxt.setText("이메일은 변경할 수 없습니다.");
            emailtxt.setEnabled(false);
            button.setText("회원 정보 변경");
        }
    }
    public class user_info {
        public String UID;
        public String name;
        public String birth;
        public String addr;
        public String readdr;
        public String phone;
        public String guardphone;
        public String guard_ck;
        public String hospname;
        public String curdisease;
        public String surhistory;
        public String fmlyhistory;
        public String symptom;
    }
    public void user_data() {

        Intent pre_intent = getIntent();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        user_info uin = new user_info();
        uin.name = pre_intent.getStringExtra("name");
        uin.UID = uid;
        uin.addr = pre_intent.getStringExtra("addr");
        uin.readdr = pre_intent.getStringExtra("readdr");
        uin.birth = pre_intent.getStringExtra("birth");
        uin.phone = pre_intent.getStringExtra("phone");
        uin.guardphone = pre_intent.getStringExtra("guard_phone");
        uin.guard_ck = pre_intent.getStringExtra("guard_ck");
        uin.hospname = pre_intent.getStringExtra("hosp_name");
        uin.curdisease = pre_intent.getStringExtra("curdisease");
        uin.surhistory = pre_intent.getStringExtra("surhistory");
        uin.fmlyhistory = pre_intent.getStringExtra("famly");
        uin.symptom = pre_intent.getStringExtra("symptom");
        mDatabase.getReference().child("users").child(uid)
                .setValue(uin);
        String newPassword = pwdtxt.getText().toString();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "회원 정보가 변경되었습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void sign(){

        email = emailtxt.getText().toString();
        pw = pwdtxt.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String uid = task.getResult().getUser().getUid();


                            Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_LONG).show();
                            Intent pre_intent = getIntent();
                            FirebaseUser user = mAuth.getCurrentUser();
                            user_info uin = new user_info();
                            uin.name = pre_intent.getStringExtra("name");
                            uin.UID = uid;
                            uin.addr = pre_intent.getStringExtra("addr");
                            uin.readdr = pre_intent.getStringExtra("readdr");
                            uin.birth = pre_intent.getStringExtra("birth");
                            uin.phone = pre_intent.getStringExtra("phone");
                            uin.guardphone = pre_intent.getStringExtra("guard_phone");
                            uin.guard_ck = pre_intent.getStringExtra("guard_ck");
                            uin.hospname = pre_intent.getStringExtra("hosp_name");
                            uin.curdisease = pre_intent.getStringExtra("curdisease");
                            uin.surhistory = pre_intent.getStringExtra("surhistory");
                            uin.fmlyhistory = pre_intent.getStringExtra("famly");
                            uin.symptom = pre_intent.getStringExtra("symptom");
                            mDatabase.getReference().child("users").child(uid)
                                    .setValue(uin);
                            Intent intent = new Intent( MemberInfo4Activity.this, HomeActivity.class );

                            startActivity( intent );
                        } else {
                            Toast.makeText(getApplicationContext(), "회원가입 실패",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }

                });
    }
    public void goToAddMemberInfo(View view) {
        // Intent intent = new Intent( LoginActivity.this, AddMemberInfoActivity.class );
        if(mod_ck==1||mod_ck==2) {
            if(pwdtxt.getText().toString().equals(re_pwd.getText().toString()) && pwdtxt.getText().toString().length()>7
                    && pwdtxt.getText().toString().length() <16) {
                user_data();
            }
            Intent intent = new Intent( MemberInfo4Activity.this, HomeActivity.class );
            startActivity( intent );
            onPause();
            finish();
        }
        else if(pwdtxt.getText().toString().equals(re_pwd.getText().toString()) && pwdtxt.getText().toString().length()>7
                && pwdtxt.getText().toString().length() <16){
            sign();
        } else {
            Toast.makeText(getApplicationContext(), "비밀번호를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT).show();
        }


    }

    public void Cancel(View view) {
        if(mod_ck == 1) {
            Intent intent = new Intent(MemberInfo4Activity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else if(mod_ck==2){
            Intent intent = new Intent(MemberInfo4Activity.this, GuardianHomeActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(MemberInfo4Activity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void goToPrevious(View view) {
        super.onBackPressed();
    }

}