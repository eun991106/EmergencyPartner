package com.example.emergencypartner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GuardianHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_home);
    }

    public void goToLogin(View view) {
        Intent intent = new Intent( GuardianHomeActivity.this, LoginActivity.class );
        startActivity( intent );
        onPause();
        finish();
    }

    public void gotoModifyMemberInfo(View view) {
        Intent intent = new Intent( GuardianHomeActivity.this, MemberInfo1Activity.class );
        intent.putExtra("mod_ck",2);
        startActivity( intent );
    }
    public void goToState(View v) {
        Intent intent = new Intent( GuardianHomeActivity.this, StateActivity.class );
        startActivity( intent );
    }


}