package hajjhackathon.wool.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hajjhackathon.wool.R;
import hajjhackathon.wool.models.LocationModel;
import hajjhackathon.wool.ui.fragments.BottomSheetFragment;
import hajjhackathon.wool.ui.widget.PopUpPlace;
import hajjhackathon.wool.utils.GPSTracker;
import hajjhackathon.wool.utils.UiUtils;

public class ClientActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String FREE_LOCATION_REFERENCE = "arfatZone/freeLocation";
    private static final String BUSY_LOCATION_REFERENCE = "arfatZone/busyLocation";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    List<LocationModel> data = new ArrayList<>();
    private double lat = 0;
    private double lng = 0;
    private LatLng currentLocation;
    private GoogleMap mMap;
    private MarkerOptions marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        prepareMap();
    }

    @OnClick(R.id.fab)
    public void fabClicked(View view) {
        PopUpPlace popUpPlace = new PopUpPlace(this, new PopUpPlace.CallbackResult() {
            @Override
            public void busyClicked() {
                writeToFirebase(LocationModel.TYPE_BUSY);
            }

            @Override
            public void freeClicked() {
                writeToFirebase(LocationModel.TYPE_FREE);
            }
        });
        popUpPlace.showDialog();

    }

    private void prepareMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), "bottomSheet");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        getLatLng();
        mMap = googleMap;
        marker = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
        currentLocation = new LatLng(21.3546629, 39.9836817);
        mMap.addMarker(marker.position(currentLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                mMap.addMarker(marker.position(point));
                lat = point.latitude;
                lng = point.longitude;
                addDataToMap();
            }
        });

        getDataForMap();
    }


    private void getLatLng() {
        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            //currentPresenter.locationPermissionGranted(gps.getLatitude(), gps.getLongitude());
            lat = gps.getLatitude();
            lng = gps.getLongitude();
        } else {
            UiUtils.loadSnackBar(getString(R.string.please_open_gps), this);
        }
    }

    private void writeToFirebase(int type) {
        String reference;
        LocationModel locationModel;
        switch (type) {
            case LocationModel.TYPE_BUSY:
                reference = BUSY_LOCATION_REFERENCE;
                locationModel = LocationModel.getBusyType(lat, lng);
                break;
            default:
                reference = FREE_LOCATION_REFERENCE;
                locationModel = LocationModel.getFreeType(lat, lng);
                break;

        }

        FirebaseDatabase.getInstance()
                .getReference(reference)
                .push()
                .setValue(locationModel);

        UiUtils.loadSnackBar(getString(R.string.data_sent), ClientActivity.this);
    }

    private void getDataForMap() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        DatabaseReference main = databaseReference.child(BUSY_LOCATION_REFERENCE);

        main.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                data.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child != null) {
                        LocationModel model = child.getValue(LocationModel.class);
                        data.add(model);

                    }
                }
                addDataToMap();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addDataToMap() {
        for (LocationModel model : data) {
            mMap.addMarker(marker.position(new LatLng(model.lat, model.lng)));
        }
    }

    public void getLocationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    GPSTracker.PERMISSIONS_REQUEST_ACCESS_LOCATION);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            prepareMap();

        }
    }
}
