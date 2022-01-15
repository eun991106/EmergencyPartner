package com.example.emergencypartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StateActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    public TextView text_state;
    public TextView text_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        text_state = (TextView)findViewById(R.id.state);
        text_location = (TextView)findViewById(R.id.location);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String Uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Client_info group = dataSnapshot.getValue(Client_info.class);
                String state;// db에서 상태정보 전달받음
                state = group.getState();


                if (state.equals("1")) {
                    text_state.setTextColor(Color.parseColor("#e42428"));
                    text_state.setText("119 호출 접수");
                    text_location.setText("119 대기 중");
                } else if (state.equals("2")) {
                    text_state.setText("호송 중");
                  //  text_location.setText(group.getHospname());
                    text_state.setTextColor(Color.parseColor("#e42428"));
                } else if (state.equals("3")) {
                    text_state.setText("응급실 도착");
                    text_location.setText(group.getHospname());
                    text_state.setTextColor(Color.parseColor("#e42428"));
                } else {
                    text_state.setText("이상 없음");
                    text_location.setText("");
                    text_state.setTextColor(Color.parseColor("#29ce6f"));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
    }

}