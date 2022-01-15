package com.example.emergencypartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MemberInfo1Activity extends AppCompatActivity {
    private EditText nameText;
    private EditText birthText;
    private EditText addressText;
    private EditText readdressText;
    private EditText phonenumberText;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    String name;
    String birth;
    String addr;
    String re_addr;
    String phone;
    int mod_ck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info_1);
        Intent pre_intent = getIntent();
        mod_ck = pre_intent.getIntExtra("mod_ck",0);
        nameText = (EditText) findViewById(R.id.nameTxt); //환자이름
        birthText = (EditText) findViewById(R.id.birthTxt); //생년월일
        addressText = (EditText) findViewById(R.id.addressTxt); //주소
        readdressText = (EditText) findViewById(R.id.restaddressTxtView); //나머지주소
        phonenumberText = (EditText) findViewById(R.id.phonenumberTxt); //환자연락처
        if(mod_ck == 1||mod_ck==2) {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            String Uid = user.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users").child(Uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Client_info group = dataSnapshot.getValue(Client_info.class);

                    nameText.setText(group.getName());
                    birthText.setText(group.getBirth());
                    addressText.setText(group.getAddr());
                    readdressText.setText(group.getReaddr());
                    phonenumberText.setText(group.getPhone());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                }
            });
            //mDatabase.child("users").child(Uid).child("name").get();
        }
        //emailText = (EditText) findViewById(R.id.emailTxt);
    }

    public void goToAddMemberInfo(View view) {
        // Intent intent = new Intent( LoginActivity.this, AddMemberInfoActivity.class );
        if(nameText.getText().toString().equals("")||phonenumberText.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "필수 항목에 공백이있습니다.", Toast.LENGTH_LONG).show();
        } else {
            name = nameText.getText().toString();
            if(!birthText.getText().toString().equals("")) {
                birth = birthText.getText().toString();
            }

            addr = addressText.getText().toString();
            re_addr = readdressText.getText().toString();
            phone = (phonenumberText.getText().toString());
            Intent intent = new Intent(MemberInfo1Activity.this, MemberInfo2Activity.class);
            intent.putExtra("name",name);
            intent.putExtra("addr",addr);
            intent.putExtra("readdr",re_addr);
            intent.putExtra("phone",phone);
            intent.putExtra("birth",birth);
            intent.putExtra("mod_ck",mod_ck);

            startActivity(intent);
        }
    }

    public void Cancel(View view) {
        if(mod_ck == 1) {
            Intent intent = new Intent(MemberInfo1Activity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else if(mod_ck==2){
            Intent intent = new Intent(MemberInfo1Activity.this, GuardianHomeActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(MemberInfo1Activity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void goToPrevious(View view) {
        super.onBackPressed();
    }
}