package com.example.emergencypartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
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

public class CallCompleteActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    String name;
    String phoneNo;
    boolean cancel = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_complete);

        /*DB*/
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String Uid = user.getUid();
        /*환자이름, 보호자연락처 DB에서 가져오는 부분*/
        mDatabase.child("users").child(Uid).child("state").setValue("1");
        mDatabase.child("users").child(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Client_info group = dataSnapshot.getValue(Client_info.class);
                name = group.getName();
                phoneNo = group.getGuardphone();

                /*화면에 전화번호 출력*/
                final TextView text_info = (TextView) findViewById(R.id.text_info); //폰번호
                final TextView text_infoText = (TextView) findViewById(R.id.text_infoText); //안내문
                text_info.setText(phoneNo);
                text_infoText.setText(name + "님의 보호자 연락처");

                /*GPS추출 및 SMS전송 동작*/
                if (!cancel) {
                    gpsTracker = new GpsTracker(CallCompleteActivity.this);
                    double latitude = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();
                    String address = getCurrentAddress(latitude, longitude);
                    String sms = name + "님의 넘어짐이 감지되어 신고를 요청합니다.\n현재 환자의 위치는 [" + address + "]입니다.";
                    try {
                        //전송
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                        Toast.makeText(getApplicationContext(), "전송 완료!", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                /*버튼*/
                final Button btn1 = (Button) findViewById(R.id.btn1);
                final Button btn2 = (Button) findViewById(R.id.btn2);
                final Button btn_cancle = (Button) findViewById(R.id.btn_cancle);
                final EditText editText = (EditText) findViewById(R.id.editText);
                final TextView text = (TextView) findViewById(R.id.text_center);
                final TextView text_state = (TextView) findViewById(R.id.text_state);
                //호송 시작 버튼
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancel=true;
                        btn2.setEnabled(true);
                        text_state.setText("119\n호송 중");
                        text.setVisibility(View.INVISIBLE);
                     //   btn_cancle.setVisibility(View.INVISIBLE);
                        editText.setVisibility(View.VISIBLE);
                        mDatabase.child("users").child(Uid).child("state").setValue("2");
                    }
                });


                //호송완료, 병원정보 전달 후, mainActivity로 이동
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancel=true;
                        //db로 병원정보 전달
                        mDatabase.child("users").child(Uid).child("hospname").setValue(editText.getText().toString());
                        mDatabase.child("users").child(Uid).child("state").setValue("3");
                        //editText.getText(); //db로 전달할 정보
                        Intent intent = new Intent(CallCompleteActivity.this, HomeActivity.class);
                        startActivity(intent);

                        String hospital = editText.getText().toString();
                        String sms = hospital+" 응급실에 도착하였습니다.";
                        //mDatabase.child("users").child(Uid).child("state").setValue("0");
                        try {
                            //전송
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                            Toast.makeText(getApplicationContext(), "전송 완료.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                        onPause();
                        finish();
                    }
                });

                //취소 버튼
                btn_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancel=true;
                        ///////////////////////////////////////
                        /*06-08 호출취소 메시지 보내는 부분*/
                        String sms = "호출을 취소합니다.";
                        mDatabase.child("users").child(Uid).child("state").setValue("0");
                        try {
                            //전송
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                            Toast.makeText(getApplicationContext(), "취소 메시지 전송 완료.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        ///////////////////////////////////////

                        Intent intent = new Intent(CallCompleteActivity.this, HomeActivity.class);
                        startActivity(intent);
                        onPause();
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("callComplete", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
    }

    //여기까지
    public void onBackPressed() {
        //super.onBackPressed();
    }

    protected void onPause() {
        super.onPause();
    }

    ///////////////////////////////////////
    /*06-08 현재 gps 끌고와서 메시지 보내는부분*/


    private PermissionSupport permission;

    private void permissionCheck() {
        // SDK 23버전 이하 버전 Permission 필요X
        if (Build.VERSION.SDK_INT >= 23) {
            permission = new PermissionSupport(this, this);
            // 권한 체크한 후 리턴이 false로 들어온다면
            if (!permission.checkPermission()) {
                // 권한 요청
                permission.requestPermission();
            }
        }
    }

    // Request Permission에 대한 결과 값
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 리턴이 false로 들어온다면 (사용자가 권한 허용을 거부하였다면)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!permission.permissionResult(requestCode, permissions, grantResults)) {
            // 다시 Permission 요청을 걸기
            permission.requestPermission();
        }
    }



    public void call(View view) {
        Context c = view.getContext();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNo));

        try {
            c.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        public void call(View view) {
            FirebaseUser user = mAuth.getCurrentUser();
            String Uid = user.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users").child(Uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Client_info group = dataSnapshot.getValue(Client_info.class);
                    phoneNo = group.getGuardphone();

                    Context c = view.getContext();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phoneNo));

                    try {
                        c.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                }
            });
        }
    */
    /*GPS 값 출력*/
    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    public void gps(View view) {
        gpsTracker = new GpsTracker(CallCompleteActivity.this);

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        String address = getCurrentAddress(latitude, longitude);

        //Toast.makeText(HomeActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
        Toast.makeText(CallCompleteActivity.this, "현재주소:" + address, Toast.LENGTH_LONG).show();
    }

    public String getCurrentAddress(double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString() + "\n";

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        permissionCheck();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    ///////////////////////////////////////
}