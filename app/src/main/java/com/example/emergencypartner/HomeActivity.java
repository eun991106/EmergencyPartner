package com.example.emergencypartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;

import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends AppCompatActivity implements SensorEventListener {

    /*권한체크*/

    private PermissionSupport permission;
    private void permissionCheck(){
        // SDK 23버전 이하 버전 Permission 필요X
        if(Build.VERSION.SDK_INT >= 23){
            permission = new PermissionSupport(this, this);
            // 권한 체크한 후 리턴이 false로 들어온다면
            if (!permission.checkPermission()){
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

    /*센서*/

    private long        m_lLastTime;
    private float        m_fSpeed;
    private float        m_fCurX,  m_fCurY,  m_fCurZ;
    private float        m_fLastX,  m_fLastY,  m_fLastZ;

    // 임계값 설정
    private static final int  SHAKE_THRESHOLD = 1200;

    // 매니저 객체
    private SensorManager    m_senMng;
    private Sensor        m_senAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        permissionCheck();
        m_senMng = (SensorManager)getSystemService(SENSOR_SERVICE);
        // TYPE_ACCELEROMETER의 기본 센서객체를 획득
        m_senAccelerometer = m_senMng.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void gotoModifyMemberInfo(View view) {
        Intent intent = new Intent( HomeActivity.this, MemberInfo1Activity.class );
        intent.putExtra("mod_ck",1);
        startActivity( intent );
    }

    public void goToLogin(View view) {
        Intent intent = new Intent( HomeActivity.this, LoginActivity.class );
        startActivity( intent );
        onPause();
        finish();
    }

    public void gotoCall(View view) {
        Intent intent = new Intent( HomeActivity.this, CallActivity.class );
        startActivity( intent );
        onPause();
        finish();
    }



    /*GPS 값 출력*/
    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    public void gps(View view) {
        gpsTracker = new GpsTracker(HomeActivity.this);

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        String address = getCurrentAddress(latitude, longitude);

        //Toast.makeText(HomeActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
        Toast.makeText(HomeActivity.this, "현재주소:" + address, Toast.LENGTH_LONG).show();
    }

    public String getCurrentAddress( double latitude, double longitude) {

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
        return address.getAddressLine(0).toString()+"\n";

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

    /*센서*/
    public void onStart()
    {
        Log.i("kmsTest", "onStart()");
        super.onStart();
        if(m_senAccelerometer != null)
            m_senMng.registerListener(this, m_senAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void onStop()
    {
        Log.i("kmsTest", "onStop()");
        super.onStop();
        if(m_senMng != null)
            m_senMng.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            long lCurTime  = System.currentTimeMillis();
            long lGabOfTime  = lCurTime - m_lLastTime;

            // 0.1초보다 오래되면 다음을 수행 (100ms)
            if(lGabOfTime > 100)
            {
                m_lLastTime = lCurTime;

                m_fCurX = event.values[0];
                m_fCurY = event.values[1];
                m_fCurZ = event.values[2];

                // 변위의 절대값에  / lGabOfTime * 10000 하여 스피드 계산
                m_fSpeed = Math.abs(m_fCurX + m_fCurY + m_fCurZ - m_fLastX - m_fLastY - m_fLastZ) / lGabOfTime * 10000;

                // 임계값보다 크게 움직였을 경우 다음을 수행
                if(m_fSpeed > SHAKE_THRESHOLD)
                {
                    //Toast.makeText(this, "낙상이 감지되었습니다.", Toast.LENGTH_SHORT).show();
                    ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
                    tone.startTone(ToneGenerator.TONE_DTMF_S, 500);
                    Intent intent = new Intent( HomeActivity.this, CallActivity.class );
                    startActivity( intent );
                    onPause();
                    finish();
                }

                // 마지막 위치 저장
                // m_fLastX = event.values[0]; 그냥 배열의 0번 인덱스가 X값
                m_fLastX = event.values[0];
                m_fLastY = event.values[1];
                m_fLastZ = event.values[2];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}