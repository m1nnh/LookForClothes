package com.sangmin.LookForClothes;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.sangmin.LookForClothes.DBsearch.arr;
import static com.sangmin.LookForClothes.DBsearch.loc;


public class GoogleMaps extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btn_home;
    private Button btn_mypage;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_home= findViewById(R.id.btn_home);
        btn_mypage= findViewById(R.id.btn_mypage);
        button3 = findViewById(R.id.button3);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoogleMaps.this, TextCameraActivity.class);
                startActivity(intent);
            }
        });

        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = getIntent();
                String name = intent2.getStringExtra("DATA2");
                Intent intent3 = getIntent();
                String size = intent3.getStringExtra("SIZE2");
                Intent intent4 = getIntent();
                String color = intent4.getStringExtra("COLOR2");
                Intent intent = new Intent(GoogleMaps.this, MyPageActivity.class);
                intent.putExtra("DATA3", name);
                intent.putExtra("SIZE3", size);
                intent.putExtra("COLOR3", color);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < 7; i++)
        {
                // 1. 마커 옵션 설정 (만드는 과정)
                MarkerOptions makerOptions = new MarkerOptions();
                makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                        .position(new LatLng(arr[i][0], arr[i][1]))
                        .title(loc[i]); // 타이틀.

                // 2. 마커 생성 (마커를 나타냄)
                mMap.addMarker(makerOptions);
            }

        //카메라를 단국대 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.321641, 127.126729)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    public void OnClickHandler(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Keep").setMessage("상품이 저장되었습니다.");

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
}