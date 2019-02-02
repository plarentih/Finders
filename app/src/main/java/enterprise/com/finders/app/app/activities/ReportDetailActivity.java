package enterprise.com.finders.app.app.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import enterprise.com.finders.R;
import enterprise.com.finders.app.app.adapter.SwipePagerAdapter;
import enterprise.com.finders.app.app.model.Report;

public class ReportDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;

    private TextView textViewTitle;

    private Report report;

    private SwipePagerAdapter swipePagerAdapter;

    private ViewPager mViewPager;

    public static String description;
    public static String urlPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        getSupportActionBar().hide();
        initializeWidgets(savedInstanceState);

        report = (Report) getIntent().getSerializableExtra("REPORT");
        description = report.getDescription();
        urlPhoto = report.getUrlPhoto();

        swipePagerAdapter = new SwipePagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(swipePagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mViewPager, true);

        textViewTitle.setText(report.getTitle());
    }

    private void initializeWidgets(Bundle savedInstanceState){
        mapView = findViewById(R.id.mapViewDesc);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        textViewTitle = findViewById(R.id.textViewTitle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(new LatLng(report.getLatitude(), report.getLongitude())));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(report.getLatitude(), report.getLongitude())).zoom(16).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
