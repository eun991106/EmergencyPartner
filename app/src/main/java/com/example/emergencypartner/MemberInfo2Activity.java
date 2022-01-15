package com.example.emergencypartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MemberInfo2Activity extends AppCompatActivity {
    private EditText guardianphonenumberText;
    String guard_ck;
    String guard_phone;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    int mod_ck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info_2);
        RadioGroup ColGroup = (RadioGroup)findViewById(R.id.Radiogroup);
        ColGroup.setOnCheckedChangeListener(mRadioCheck);
        RadioButton r1 = (RadioButton)findViewById(R.id.allRadioButton);
        RadioButton r2 = (RadioButton)findViewById(R.id.oneonenineRadioButton);
        RadioButton r3 = (RadioButton)findViewById(R.id.guardianRadioButton);
        guardianphonenumberText = (EditText) findViewById(R.id.guardianphonenumberTxt); //보호자연락처
        Intent pre_intent = getIntent();
        mod_ck = pre_intent.getIntExtra("mod_ck",0);
        if(mod_ck ==1||mod_ck==2) {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            String Uid = user.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users").child(Uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Client_info group = dataSnapshot.getValue(Client_info.class);

                    guardianphonenumberText.setText(group.getGuardphone());
                    guard_ck = group.getGuard_ck();
                    switch (guard_ck){
                        case "1":
                            r1.setChecked(true);
                            break;
                        case "2":
                            r2.setChecked(true);
                            break;
                        case "3":
                            r3.setChecked(true);
                            break;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                }
            });
        }

    }
    RadioGroup.OnCheckedChangeListener mRadioCheck =
            new RadioGroup.OnCheckedChangeListener(){
                public void onCheckedChanged(RadioGroup group, int checkedId){
                    if (group.getId() == R.id.Radiogroup){
                        switch (checkedId){
                            case R.id.allRadioButton:
                                guard_ck = "1";
                                break;
                            case R.id.oneonenineRadioButton:
                                guard_ck="2";
                                break;
                            case R.id.guardianRadioButton:
                                guard_ck="3";
                                break;
                        }
                    }
                }
            };
    public void goToAddMemberInfo(View view) {
        // Intent intent = new Intent( LoginActivity.this, AddMemberInfoActivity.class );
        if(guardianphonenumberText.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "공백없이 채워주세요.", Toast.LENGTH_LONG).show();
        } else {
            guard_phone = guardianphonenumberText.getText().toString();
            Intent pre_intent = getIntent();
            String name = pre_intent.getStringExtra("name");
            String addr = pre_intent.getStringExtra("addr");
            String re_addr = pre_intent.getStringExtra("readdr");
            String phone = pre_intent.getStringExtra("phone");
            String birth = pre_intent.getStringExtra("birth");

            //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
            Intent intent = new Intent( MemberInfo2Activity.this, MemberInfo3Activity.class );
            intent.putExtra("name",name);
            intent.putExtra("addr",addr);
            intent.putExtra("readdr",re_addr);
            intent.putExtra("phone",phone);
            intent.putExtra("birth",birth);
            intent.putExtra("guard_ck",guard_ck);
            intent.putExtra("guard_phone",guard_phone);
            intent.putExtra("mod_ck",mod_ck);
            startActivity( intent );
        }

    }

    public void Cancel(View view) {
        if(mod_ck == 1) {
            Intent intent = new Intent(MemberInfo2Activity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else if(mod_ck==2){
            Intent intent = new Intent(MemberInfo2Activity.this, GuardianHomeActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(MemberInfo2Activity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void goToPrevious(View view) {
        super.onBackPressed();
    }
}