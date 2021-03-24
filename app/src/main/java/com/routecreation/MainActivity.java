package com.routecreation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    GPSTracker gps;
    TextView address,time,distance;
    SQLiteDatabase db ;
    ArrayList<RoutelistModel> model1 = new ArrayList<>();
    RoutelistAdapter mAdapter;
    RecyclerView routerecycler;
    LinearLayout hint;
    List<Address> addresses;
    List<Address> desaddresses;
    Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        distance=findViewById(R.id.distance);
        time=findViewById(R.id.time);
        hint=findViewById(R.id.hint);
        hint.setVisibility(View.GONE);
        routerecycler=findViewById(R.id.routerecycler);
        db=openOrCreateDatabase("RouteDb",android.content.Context.MODE_PRIVATE ,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Route(Id INTEGER PRIMARY KEY AUTOINCREMENT,Startlan VARCHAR,Startlon VARCHAR,Endlat VARCHAR,Endlon VARCHAR,Distance VARCHAR,Time VARCHAR,Name VARCHAR,Fromaddress VARCHAR,Toaddress VARCHAR);");
        routerecycler.setLayoutManager(new LinearLayoutManager(this));
        routerecycler.setAdapter(mAdapter);
        show();
        enableSwipeToDeleteAndUndo();

        if (checkPermissions()) {
             gps = new GPSTracker(MainActivity.this);
             if (gps.canGetLocation()) {
                 String lat1 = String.valueOf(gps.getLatitude());
                 String lon1 = String.valueOf(gps.getLongitude());
             } else {
                 gps.showSettingsAlert();
             }
         }else {
             requestPermissions();
         }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissions()) {
                    gps = new GPSTracker(MainActivity.this);
                    if (gps.canGetLocation()) {
                        String lat1 = String.valueOf(gps.getLatitude());
                        String lon1 = String.valueOf(gps.getLongitude());
                        Intent intent=new Intent(MainActivity.this,MapsActivity.class);
                        startActivityForResult(intent,11);
                    } else {
                        gps.showSettingsAlert();
                    }
                }else {
                    requestPermissions();
                }

            }
        });
    }

    private boolean checkPermissions() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

    }

    @Override
    protected void onResume() {
        super.onResume();
        gps = new GPSTracker(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


            if (requestCode == 11) {
                try {
                    if (resultCode == Activity.RESULT_OK){
                        double lat1 = gps.getLatitude();
                        double lon1 = gps.getLongitude();
                        String stalat = data.getStringExtra("stalat");
                        String stalon = data.getStringExtra("stalon");
                        String deslat = data.getStringExtra("deslat");
                        String deslon = data.getStringExtra("deslon");
                        String route = data.getStringExtra("routename");
                        String hi=String.valueOf(distance(Double.parseDouble(stalat),Double.parseDouble(stalon),Double.parseDouble(deslat),Double.parseDouble(deslon)));
                        String time1=time(hi);
                        //.makeText(getApplicationContext(),"Route Added",Toast.LENGTH_SHORT).show();
                        geocoder = new Geocoder(this, Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(Double.parseDouble(stalat), Double.parseDouble(stalon), 1);
                            desaddresses = geocoder.getFromLocation(Double.parseDouble(deslat), Double.parseDouble(deslon), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String desaddress = desaddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String descity = desaddresses.get(0).getLocality();
                        String desstate = desaddresses.get(0).getAdminArea();
                        String descountry = desaddresses.get(0).getCountryName();
                        String start=city +","+ state;
                        String end=descity +","+ desstate;
                        db.execSQL("INSERT INTO Route(Startlan,Startlon,Endlat,Endlon,Distance,Time,Name,Fromaddress,Toaddress)VALUES('"+ stalat+"','"+ stalon+ "','"+deslat+"','"+deslon+"','"+String.format("%.2f", Double.parseDouble(hi))+"','"+String.format("%.2f", Double.parseDouble(time1))+"','"+route +"','"+start+"','"+end+"');");
                        routerecycler.setLayoutManager(new LinearLayoutManager(this));
                        routerecycler.setAdapter(mAdapter);
                        show();
                       // Toast.makeText(getApplicationContext(),time1,Toast.LENGTH_SHORT).show();
                    }
                    if (resultCode == Activity.RESULT_CANCELED) {
                        Toast.makeText(MainActivity.this, "cancelled", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }else if(requestCode==12){

                if (resultCode == Activity.RESULT_OK){
                String id = data.getStringExtra("id");
                String name = data.getStringExtra("name");
                Cursor c=db.rawQuery("SELECT * FROM Route WHERE Id='"+ id+"'", null);
                if(c.moveToFirst()) {
                    db.execSQL("UPDATE Route  SET Name ='"+ name+"' WHERE Id ='"+id+"'");
                    routerecycler.setLayoutManager(new LinearLayoutManager(this));
                    routerecycler.setAdapter(mAdapter);
                    show();
                    Toast.makeText(getApplicationContext(), "Record Modified", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    //msg(this, "Invalid Employee Name");
                    Toast.makeText(getApplicationContext(), "Invalid  Id", Toast.LENGTH_SHORT).show();

                }
                    if (resultCode == Activity.RESULT_CANCELED) {
                        Toast.makeText(MainActivity.this, "cancelled", Toast.LENGTH_LONG).show();
                    }
                }
            }
        super.onActivityResult(requestCode, resultCode, data);

    }
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private String time(String distance) {


        double speed=50;
        double time = Double.parseDouble(distance)/speed;
        return (String.valueOf(time));
    }

    public  void show(){
        model1.clear();
        //Cursor c=db.rawQuery("SELECT * FROM IncomeDetail", null);
        Cursor c=db.rawQuery("SELECT * FROM Route ", null);
        if(c.getCount()==0)
        {
            hint.setVisibility(View.VISIBLE);
            //Toast.makeText(getApplicationContext(), "No records found", Toast.LENGTH_SHORT).show();
            return;
        }else{
            hint.setVisibility(View.GONE);
        }
        StringBuffer buffer=new StringBuffer();
        while(c.moveToNext())
        {
            buffer.append("Employee Name: "+c.getString(c.getColumnIndex("Id"))+"\n");
            RoutelistModel value = new RoutelistModel(c.getString(0),c.getString(1), c.getString(2), c.getString(3) , c.getString(4) , c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9));
            model1.add(value);
        }

        mAdapter = new RoutelistAdapter(getApplicationContext(), model1,this);
        routerecycler.setAdapter(mAdapter);
        //msg(this, buffer.toString());
        //Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linear:
                 RoutelistModel model= (RoutelistModel) view.getTag();
                Intent intent2 = new Intent(this, MapsviewActivity.class);
                intent2.putExtra("startlat", model.getStartlat());
                intent2.putExtra("startlon", model.getStartlon());
                intent2.putExtra("endlat",model.getEndlat());
                intent2.putExtra("endlon",model.getEndlon());
                intent2.putExtra("name",model.getName());
                intent2.putExtra("id",model.getId());
                startActivityForResult(intent2,12);
                break;
        }

    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                mAdapter.removeItem(position);



            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(routerecycler);
    }
}