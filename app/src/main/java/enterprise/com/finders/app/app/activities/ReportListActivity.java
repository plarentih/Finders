package enterprise.com.finders.app.app.activities;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import enterprise.com.finders.R;
import enterprise.com.finders.app.app.model.Report;
import enterprise.com.finders.app.app.tools.PermissionHelper;

public class ReportListActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;

    static List<Report> reportList = new ArrayList<>();
    //final Set<Report> reportList = new HashSet<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        textView = findViewById(R.id.textView_noReports);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        button = findViewById(R.id.button_go_toR);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddReportActivity.class);
                startActivity(intent);
            }
        });

        if(PermissionHelper.checkForPermissions(ReportListActivity.this)){

        }else {
            PermissionHelper.askForPermissions(ReportListActivity.this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.report_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signOut) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), PhoneAuthenticationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isGPSEnabled()
    {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                double lat = latLng.latitude;
                double lng = latLng.longitude;
                for(int index = 0; index < reportList.size(); index++){
                    if(reportList.get(index).getLatitude() == lat && reportList.get(index).getLongitude() == lng){
                        Report report = reportList.get(index);
                        Intent intent = new Intent(getApplicationContext(), ReportDetailActivity.class);
                        intent.putExtra("REPORT", report);
                        startActivity(intent);
                    }
                }
            }
        });

        databaseReference.child("reports").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child : children){
                    Report report = child.getValue(Report.class);
                    reportList.add(report);
                }
                for(int index = 0; index < reportList.size(); index++){
                    Report report = reportList.get(index);
                    double lat = report.getLatitude();
                    double lon = report.getLongitude();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(report.getTitle()));
                }
                textView.setText(String.valueOf(reportList.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(49.5009, 5.9861)).zoom(13).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(49.61167, 6.13)));
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
        textView.setText(String.valueOf(reportList.size()));
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
