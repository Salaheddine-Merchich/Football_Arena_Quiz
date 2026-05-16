package com.example.quizapp_merchich;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StadiumMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private String userContinent = "Global";
    private TextView tvMapSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium_map);

        userContinent = getIntent().getStringExtra("continent");
        if (userContinent == null) userContinent = "Global";

        tvMapSubtitle = findViewById(R.id.tvMapSubtitle);
        tvMapSubtitle.setText("Region: " + userContinent);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        FloatingActionButton fabBack = findViewById(R.id.fabBack);
        fabBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Custom Map Styling (Optional, simple)
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getCurrentLocation();
        }

        addFootballMarkers();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 5f));
            }
        });
    }

    private void addFootballMarkers() {
        // --- MOROCCO ---
        addMarker(33.582, -7.648, "Stade Mohammed V", "Casablanca, Morocco");
        addMarker(33.948, -6.837, "Prince Moulay Abdellah Stadium", "Rabat, Morocco");

        // --- EUROPE ---
        addMarker(40.453, -3.688, "Santiago Bernabéu", "Madrid, Spain");
        addMarker(41.381, 2.122, "Camp Nou", "Barcelona, Spain");
        addMarker(53.463, -2.291, "Old Trafford", "Manchester, UK");
        addMarker(51.554, -0.108, "Emirates Stadium", "London, UK");
        addMarker(48.218, 11.624, "Allianz Arena", "Munich, Germany");
        addMarker(48.924, 2.360, "Stade de France", "Paris, France");

        // --- AFRICA ---
        addMarker(-26.234, 27.982, "FNB Stadium (Soccer City)", "Johannesburg, South Africa");
        addMarker(30.069, 31.264, "Cairo International Stadium", "Cairo, Egypt");

        // --- SOUTH AMERICA ---
        addMarker(-22.912, -43.230, "Maracanã Stadium", "Rio de Janeiro, Brazil");
        addMarker(-34.635, -58.364, "La Bombonera", "Buenos Aires, Argentina");
    }

    private void addMarker(double lat, double lng, String title, String snippet) {
        LatLng position = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }
}
