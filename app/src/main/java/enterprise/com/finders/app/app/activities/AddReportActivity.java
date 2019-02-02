package enterprise.com.finders.app.app.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import enterprise.com.finders.R;
import enterprise.com.finders.app.app.model.Report;
import enterprise.com.finders.app.app.retro.VisionTest;
import enterprise.com.finders.app.app.tools.PermissionHelper;
import enterprise.com.finders.app.app.tools.UIHelper;

public class AddReportActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;

    private Button saveBtn, uploadBtn, cameraBtn;
    private EditText txtTitle, txtDescription;
    private ProgressBar progressBar;

    private String eventId;
    private String title;
    private String desc;

    private Uri filePath;
    private File file;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int CAPTURE_IMAGE_REQUEST = 72;

    FirebaseStorage storage;
    StorageReference storageReference;

    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;

    private Location location;
    private Location touchLocation;

    private String picContectResult;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    public static final int LOCATION_UPDATE_MIN_DISTANCE = 0;
    public static final int LOCATION_UPDATE_MIN_TIME = 0;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                double a =  location.getLatitude();
                double b = location.getLongitude();

                mLocationManager.removeUpdates(mLocationListener);
            } else {
                Log.d("PLARENT!!!","Location is null", new Throwable());
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        getSupportActionBar().hide();
        initializeWidgets();
        centerTitle();
        initializeFirebaseComponents();

        mapView = findViewById(R.id.mapViewReport);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        IntentFilter statusFilter = new IntentFilter("RESULT");
        Receiver receiver =new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,statusFilter);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//        if(PermissionHelper.checkForPermissions(AddReportActivity.this)){
//
//        }else {
//            PermissionHelper.askForPermissions(AddReportActivity.this);
//        }

        getCurrentLocation();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PermissionHelper.checkForPermissions(AddReportActivity.this)){
                    title = txtTitle.getText().toString();
                    desc = txtDescription.getText().toString();

                    if(TextUtils.isEmpty(txtTitle.getText().toString().trim())){
                        txtTitle.setError("This field can't be blank.");
                    }else if(TextUtils.isEmpty(txtDescription.getText().toString().trim())){
                        txtDescription.setError("This field can't be blank.");
                    }else {
                        try{
                            uploadImage();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    ReportListActivity.reportList.clear();
                }else {
                    PermissionHelper.askForPermissions(AddReportActivity.this);
                }
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PermissionHelper.checkForPermissions(AddReportActivity.this)){
                    chooseImage();
                }else {
                    PermissionHelper.askForPermissions(AddReportActivity.this);
                }
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            file = null;
            try{
                file = createImageFile();
            }catch (IOException ex){
                Toast.makeText(getApplicationContext(), "Error occurred while creating the File", Toast.LENGTH_SHORT).show();
            }
            if(file != null){
                filePath = FileProvider.getUriForFile(this, "enterprise.com.finders.app.app", file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if(PermissionHelper.checkForPermissions(AddReportActivity.this)){
            getCurrentLocation();
        }else {
            PermissionHelper.askForPermissions(AddReportActivity.this);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        mLocationManager.removeUpdates(mLocationListener);
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        location = null;
        if (!(isGPSEnabled || isNetworkEnabled)){

        } else {
            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }
        }
        if (location != null) {

        }
    }

    private void initializeWidgets(){
        txtTitle = findViewById(R.id.field_title);
        txtDescription = findViewById(R.id.field_description);
        saveBtn = findViewById(R.id.buttonSave);
        uploadBtn = findViewById(R.id.buttonUpload);
        cameraBtn = findViewById(R.id.buttonCamera);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void initializeFirebaseComponents(){
        mFirebaseInstance = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mFirebaseDatabase = mFirebaseInstance.getReference("reports/");
    }

    private void uploadImage() {
        if(filePath != null) {
            if(location == null){
                if(touchLocation == null){
                    Toast.makeText(getApplicationContext(),"Please select location on map.", Toast.LENGTH_SHORT).show();
                }else {
                    //create touch report
                    StorageReference refi = storageReference.child("images/"+ title);
                    refi.putFile(filePath)
                            .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath = taskSnapshot.getDownloadUrl();
                            createReport(eventId, title, desc, touchLocation.getLatitude(),
                                    touchLocation.getLongitude(), filePath.toString());
                            //Toast.makeText(AddReportActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                            AddReportActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtTitle.setText("");
                                    txtDescription.setText("");
                                }
                            });
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddReportActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.VISIBLE);
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            int currentprogress = (int) progress;
                            progressBar.setProgress(currentprogress);
                            //Toast.makeText(AddReportActivity.this, "Uploading...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }else {
                //create loc report
                StorageReference ref = storageReference.child("images/"+ title);
                ref.putFile(filePath)
                        .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                filePath = taskSnapshot.getDownloadUrl();
                                createReport(eventId, title, desc, location.getLatitude(), location.getLongitude(),
                                        filePath.toString());
                                Toast.makeText(AddReportActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                                AddReportActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtTitle.setText("");
                                        txtDescription.setText("");
                                    }
                                });
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddReportActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        int currentprogress = (int) progress;
                        progressBar.setProgress(currentprogress);
                    }
                });
            }
        }else {
            Toast.makeText(AddReportActivity.this, "Please take a picture ore select one from your gallery.", Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void createReport(String key, String title, String description, double latitude, double longitude, String urlPhoto) {
        if (TextUtils.isEmpty(eventId)) {
            eventId = mFirebaseDatabase.push().getKey();
        }
        Report event = new Report(eventId, title, description, latitude, longitude , urlPhoto);
        mFirebaseDatabase.child(eventId).setValue(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            filePath = data.getData();
            Uri uri = data.getData();
            Intent intent = new Intent(this,VisionTest.class);
            intent.putExtra("path", getPath(uri));
            startService(intent);
        }else if(requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK){
            Intent intent = new Intent(this,VisionTest.class);
            intent.putExtra("path", file.getPath());
            startService(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void centerTitle() {
        ArrayList<View> textViews = new ArrayList<>();

        getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);

        if(textViews.size() > 0) {
            AppCompatTextView appCompatTextView = null;
            if(textViews.size() == 1) {
                appCompatTextView = (AppCompatTextView) textViews.get(0);
            } else {
                for(View v : textViews) {
                    if(v.getParent() instanceof Toolbar) {
                        appCompatTextView = (AppCompatTextView) v;
                        break;
                    }
                }
            }

            if(appCompatTextView != null) {
                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                appCompatTextView.setLayoutParams(params);
                appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
    }
    // next part might need to seek a nicer location
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    private String getPath(Uri uri) {
        if (isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }


        }
        // DownloadsProvider
        else if (isDownloadsDocument(uri)) {

            final String id = DocumentsContract.getDocumentId(uri);
            final Uri contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

            return getDataColumn(getApplicationContext(), contentUri, null, null);
        }
        // MediaProvider
        else if (isMediaDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{
                    split[1]
            };

            return getDataColumn(getApplicationContext(), contentUri, selection, selectionArgs);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(getApplicationContext(), uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(location == null){
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(49.505609, 5.980213)).zoom(13).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng));

                    touchLocation = new Location(LocationManager.GPS_PROVIDER);
                    touchLocation.setLatitude(latLng.latitude);
                    touchLocation.setLongitude(latLng.longitude);

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(latLng.latitude, latLng.longitude)).zoom(16).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
        }else {
            LatLng yourLocation = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(yourLocation).title("You are here"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(yourLocation));
        }
    }

    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null &&intent.hasExtra("adult")){
                picContectResult = intent.getStringExtra("adult");
            }
        }
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
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
