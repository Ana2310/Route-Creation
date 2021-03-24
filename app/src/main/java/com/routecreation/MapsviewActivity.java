package com.routecreation;

import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsviewActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String startlat,startlon,endlat,endlon,name,id;
    EditText routename;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapsview);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button = findViewById(R.id.button);
        routename = findViewById(R.id.routename);
        startlat = getIntent().getExtras().getString("startlat");
        startlon = getIntent().getExtras().getString("startlon");
        endlat = getIntent().getExtras().getString("endlat");
        endlon = getIntent().getExtras().getString("endlon");
        name = getIntent().getExtras().getString("name");
        id = getIntent().getExtras().getString("id");
        routename.setText(name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(routename.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter routename",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("name", routename.getText().toString());
                    returnIntent.putExtra("id", id);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel1 = 12.0f;
        LatLng location = new LatLng(Double.parseDouble(startlat), Double.parseDouble(startlon));
        LatLng location1 = new LatLng(Double.parseDouble(endlat), Double.parseDouble(endlon));
        mMap.addMarker(new MarkerOptions().position(location).snippet("Start Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.dote)));
        mMap.addMarker(new MarkerOptions().position(location1).snippet("End Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.dots)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel1));
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(Double.parseDouble(startlat), Double.parseDouble(startlon)), new LatLng(Double.parseDouble(endlat), Double.parseDouble(endlon)))
                .width(8)
                .color(Color.BLUE));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View row = getLayoutInflater().inflate(R.layout.snipet, null);

                TextView t1 = (TextView) row.findViewById(R.id.name);
                t1.setText(marker.getSnippet());
                return row;
            }
        });


    }
}