package com.example.emergencypartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MemberInfo3Activity extends AppCompatActivity {
    private EditText hospitalnameText;
    private EditText currentdiseaseText;
    private EditText surgeryhistoryText;
    private EditText familihistoryText;
    private EditText symptomText;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    String hosp_name;
    String curdisease;
    String surhistory;
    String famly;
    String symptom;
    int mod_ck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info_3);
        Intent pre_intent = getIntent();
        hospitalnameText = (EditText)findViewById(R.id.hospitalTxt); //지정 병원명
        currentdiseaseText = (EditText)findViewById(R.id.currentdiseaseTxt);//현재질환
        surgeryhistoryText = (EditText)findViewById(R.id.surgeryhistoryTxt);//수술이력
        familihistoryText = (EditText)findViewById((R.id.familihistoryTxt));//가족력
        symptomText = (EditText)findViewById(R.id.symptomTxt);//증세
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

                    hospitalnameText.setText(group.getHospname());
                    currentdiseaseText.setText(group.getCurdisease());
                    surgeryhistoryText.setText(group.getSurhistory());
                    familihistoryText.setText(group.getFmlyhistory());
                    symptomText.setText(group.getSymptom());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                }
            });
        }
    }

    public void goToAddMemberInfo(View view) {
        // Intent intent = new Intent( LoginActivity.this, AddMemberInfoActivity.class );
    hosp_name = hospitalnameText.getText().toString();
    curdisease = currentdiseaseText.getText().toString();
    surhistory = surgeryhistoryText.getText().toString();
    famly = familihistoryText.getText().toString();
    symptom = symptomText.getText().toString();

        Intent pre_intent = getIntent();
        String name = pre_intent.getStringExtra("name");
        String addr = pre_intent.getStringExtra("addr");
        String re_addr = pre_intent.getStringExtra("readdr");
        String phone = pre_intent.getStringExtra("phone");
        String birth = pre_intent.getStringExtra("birth");
        String guard_ck = pre_intent.getStringExtra("guard_ck");
        String guard_phone = pre_intent.getStringExtra("guard_phone");


        Intent intent = new Intent( MemberInfo3Activity.this, MemberInfo4Activity.class );
        intent.putExtra("name",name);
        intent.putExtra("addr",addr);
        intent.putExtra("readdr",re_addr);
        intent.putExtra("phone",phone);
        intent.putExtra("birth",birth);
        intent.putExtra("guard_ck",guard_ck);
        intent.putExtra("guard_phone",guard_phone);
        intent.putExtra("name",name);
        intent.putExtra("addr",addr);
        intent.putExtra("readdr",re_addr);
        intent.putExtra("phone",phone);
        intent.putExtra("birth",birth);
        intent.putExtra("hosp_name",hosp_name);
        intent.putExtra("curdisease",curdisease);
        intent.putExtra("surhistory",surhistory);
        intent.putExtra("famly",famly);
        intent.putExtra("symptom",symptom);
        intent.putExtra("mod_ck",mod_ck);
        //post1.getThree(hospitalnameText.getText().toString(), currentdiseaseText.getText().toString(), surgeryhistoryText.getText().toString(), familihistoryText.getText().toString(), symptomText.getText().toString());

        startActivity( intent );
    }

    public void Cancel(View view) {
        if(mod_ck == 1) {
            Intent intent = new Intent(MemberInfo3Activity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else if(mod_ck==2){
            Intent intent = new Intent(MemberInfo3Activity.this, GuardianHomeActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(MemberInfo3Activity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void goToPrevious(View view) {
        super.onBackPressed();
    }
}