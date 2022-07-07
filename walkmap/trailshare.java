package com.example.walkmap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.CompassView;
import com.naver.maps.map.widget.LocationButtonView;
import com.naver.maps.map.widget.ScaleBarView;
import com.naver.maps.map.widget.ZoomControlView;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class trailshare extends AppCompatActivity implements OnMapReadyCallback {
    private DatabaseReference mDatabase;

    String myJSON; //DB

    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final String TAG = "trailshare";
    private static final int PERMISSION_REQUEST_CODE = 100;
    //현재 위치 정보 받을 때 필요한 권한.
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    // 현재 위치를 불러오기 위한 객체 선언
    private FusedLocationSource mLocationSource;
    // 네이버 맵 객체
    private NaverMap mNaverMap;

    JSONArray pathes = null;

    ArrayList<HashMap<String, String>> pathList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailshare);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map1);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map1, mapFragment).commit();
        }

        // getMapAsync를 호출하여 비동기로 onMapReady 콜백 메서드 호출
        // onMapReady에서 NaverMap 객체를 받음
        mapFragment.getMapAsync(this);

        // 위치를 반환하는 구현체인 FusedLocationSource 생성
        mLocationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);



        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }

    }


    private List<LatLng> List = new ArrayList<>();;


    //지도에 마커찍기.
    public void onMapReady(@NonNull NaverMap naverMap) {



        Intent intent = getIntent();
        String number = (String) intent.getStringExtra("trail_number");
        String username = intent.getStringExtra("trail_viewuser");
        // NaverMap 객체 받아서 NaverMap 객체에 현재 위치 찍기
        mNaverMap = naverMap;
        mNaverMap.setLocationSource(mLocationSource);
        System.out.println(number);
        System.out.println(username);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("trails").child(username).child(number).child("dbpaths").addListenerForSingleValueEvent(
                    new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            try {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                List.add(new LatLng(Double.parseDouble(snapshot.child("latitude").getValue().toString()),Double.parseDouble(snapshot.child("longitude").getValue().toString())));

                                }


                                LatLng cpath_LatLng = List.get(0);
                                LatLng path_LatLng;

                                Marker marker = new Marker();
                                marker.setPosition(cpath_LatLng);
                                marker.setCaptionText("출발");
                                marker.setMap(mNaverMap);

                                LatLng pathPosition1 = cpath_LatLng;
                                CameraUpdate cameraUpdate2 = CameraUpdate.scrollTo(cpath_LatLng);
                                naverMap.moveCamera(cameraUpdate2);
                                for (int i = 0; i < List.size(); i++) {
                                    path_LatLng = List.get(i);
                                    if (List.size() == 1) {
                                        LatLng pathPosition = pathPosition1;
                                        pathPosition1 = path_LatLng;
                                        // 아직 반영되지 않음
                                        PathOverlay path = new PathOverlay();
                                        path.setCoords(Arrays.asList(
                                                pathPosition, pathPosition1
                                        ));
                                        path.setMap(naverMap);
                                        // 반영
                                        path.setColor(Color.GREEN);
                                        path.setOutlineColor(Color.GREEN);
                                    }


                                    if (i >= 1) {
                                        LatLng pathPosition = pathPosition1;
                                        pathPosition1 = path_LatLng;
                                        // 아직 반영되지 않음
                                        PathOverlay path = new PathOverlay();
                                        path.setCoords(Arrays.asList(
                                                pathPosition, pathPosition1
                                        ));
                                        path.setMap(naverMap);
                                        // 반영
                                        path.setColor(Color.GREEN);
                                        path.setOutlineColor(Color.GREEN);

                                        if((i==List.size()-1))
                                        {
                                            Marker marker1 = new Marker();
                                            marker1.setPosition(pathPosition1);
                                            marker1.setCaptionText("도착");
                                            marker1.setMap(mNaverMap);
                                        }
                                    }


                                }

                            }catch(Exception e){

                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                        }
                    });


            // 지도 ui 컨트롤
            UiSettings uiSettings = mNaverMap.getUiSettings();
            uiSettings.setCompassEnabled(true); // 기본값 : true 나침반
            uiSettings.setScaleBarEnabled(false); // 기본값 : true 축적 바
            uiSettings.setZoomControlEnabled(false); // 기본값 : true 확대
            uiSettings.setLocationButtonEnabled(false); // 기본값 : false
            uiSettings.setLogoGravity(Gravity.RIGHT | Gravity.BOTTOM);

            CompassView compassView = findViewById(R.id.compass1);
            compassView.setMap(mNaverMap);
            ScaleBarView scaleBarView = findViewById(R.id.scalebar1);
            scaleBarView.setMap(mNaverMap);
            ZoomControlView zoomControlView = findViewById(R.id.zoom1);
            zoomControlView.setMap(mNaverMap);
            LocationButtonView locationButtonView = findViewById(R.id.location1);
            locationButtonView.setMap(mNaverMap);
            CameraUpdate cameraUpdate = CameraUpdate.zoomTo(18);
            naverMap.moveCamera(cameraUpdate);
            // 권한확인. 결과는 onRequestPermissionsResult 콜백 매서드 호출
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);


    }

    public LatLng getCurrentPosition(NaverMap naverMap) {
        CameraPosition cameraPosition = naverMap.getCameraPosition();
        return new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
    }



    //gps 권한 획득 코드
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // request code와 권한획득 여부 확인 네이버 지도 용
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mNaverMap.setLocationTrackingMode(LocationTrackingMode.Face);
            }
        }
        // 현재 위치 얻기 용
        if ( requestCode == PERMISSION_REQUEST_CODE && grantResults.length == PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[1])) {

                    Toast.makeText(trailshare.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(trailshare.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(trailshare.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(trailshare.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(trailshare.this, PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(trailshare.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(trailshare.this, PERMISSIONS,
                        PERMISSION_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(trailshare.this, PERMISSIONS,
                        PERMISSION_REQUEST_CODE);
            }

        }

    }


    //좌표를 주소로 변환
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


    //여기부터는 GPS 활성화
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(trailshare.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
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







}