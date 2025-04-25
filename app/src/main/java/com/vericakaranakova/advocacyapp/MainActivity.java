package com.vericakaranakova.advocacyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static ArrayList<Officials> officialsLst = new ArrayList<>();
    private RecyclerView recyclerView;
    private static MainAdapter mainAdapter;
    private static TextView loc;

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;
    private static String locationString = "Unspecified Location";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loc = findViewById(R.id.currLocation);


        if (locationString.equals("Unspecified Location")) {
            mFusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(this);
            determineLocation();
        } else {
            doDownload(locationString);
        }
        recyclerView = findViewById(R.id.mainViewRecycler);
        mainAdapter = new MainAdapter(officialsLst, this);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (hasNetworkConnection()) {
            if (locationString.equals("Unspecified Location")) {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);    //device location
                determineLocation();
            } else {
                doDownload(locationString);
            }
        } else {
            loc.setText("No Data For Location");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Data cannot be accessed/loaded without an internet connection.");
            builder.setTitle("No Network Connection");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void doDownload(String location) {
        if (hasNetworkConnection()) {
            if (location.equals("") || location == null) {

            } else {
                locationString = location;
                OfficialsDownloader.downloadOfficials(this, location);
            }
        } else {
            loc.setText("No Data For Location");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Data cannot be accessed/loaded without an internet connection.");
            builder.setTitle("No Network Connection");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public static void updateData(String location, ArrayList<Officials> o) {
        loc.setText(location);
        officialsLst.clear();
        officialsLst.addAll(o);
        mainAdapter.notifyItemRangeChanged(0, officialsLst.size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.aboutMenu) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.locationMenu) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final EditText et = new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            et.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(et);

            builder.setPositiveButton("OK", (dialog, id) -> doDownload(et.getText().toString()));

            builder.setNegativeButton("CANCEL", (dialog, id) -> doDownload(""));

            builder.setTitle("Enter Address");

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Officials o = officialsLst.get(pos);

        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("OFFICIAL", o);
        intent.putExtra("LOCATION", OfficialsDownloader.getLoc());
        startActivity(intent);
    }

    private void determineLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        locationString = getPlace(location);
                        loc.setText(locationString);
                        doDownload(locationString);
                    }
                })
                .addOnFailureListener(this, e ->
                        Toast.makeText(MainActivity.this,
                                e.getMessage(), Toast.LENGTH_LONG).show());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    loc.setText(R.string.deniedText);
                }
            }
        }
    }

    private String getPlace(Location loc) {

        StringBuilder sb = new StringBuilder();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            sb.append(String.format(
                    Locale.getDefault(),
                    "%s %s %s %s, %s",
                    (addresses.get(0).getSubThoroughfare() == null ? "" : addresses.get(0).getSubThoroughfare()),
                    (addresses.get(0).getThoroughfare() == null ? "" : addresses.get(0).getThoroughfare()),
                    (addresses.get(0).getLocality() == null ? "" : addresses.get(0).getLocality()),
                    (addresses.get(0).getAdminArea() == null ? "" : addresses.get(0).getAdminArea()),
                    (addresses.get(0).getPostalCode() == null ? "" : addresses.get(0).getPostalCode())));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
}