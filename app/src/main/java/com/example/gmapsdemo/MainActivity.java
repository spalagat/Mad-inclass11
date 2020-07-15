package com.example.gmapsdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {


    //AIzaSyAdJ1CrS1vI_2rIPDL-OujkvXzJezdftAM
    private GoogleMap gmap;
    ArrayList<PathCoordinates> pathCoordinates=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Paths Activity");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        loadJSONFromAsset();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        gmap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        PolylineOptions polygonOptions = new PolylineOptions();

        LatLng startPoint;
        LatLng endPoint;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        String jsonString = loadJSONFromAsset();
        Gson gson = new Gson();
        JSONObject points  = null;
        try {
            points = new JSONObject(jsonString);
            JSONArray pointsArray = points.getJSONArray("points");

            for (int i = 0; i < pointsArray.length(); i++) {
                JSONObject item = pointsArray.getJSONObject(i);
                PathCoordinates pointsItem = gson.fromJson(item.toString(), PathCoordinates.class);
                polygonOptions.add(new LatLng(pointsItem.getLatitude(), pointsItem.getLongitude()));

                if(i==0){

                    startPoint = new LatLng(pointsItem.getLatitude(), pointsItem.getLongitude());

                    gmap.addMarker(new MarkerOptions().position(startPoint)
                            .title("Starting Point"));

                }

                if(i== pointsArray.length()-1){

                    endPoint = new LatLng(pointsItem.getLatitude(), pointsItem.getLongitude());

                    gmap.addMarker(new MarkerOptions().position(endPoint).title("Ending Point"));
                    gmap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(pointsItem.getLatitude(), pointsItem.getLongitude())));

                }

                builder.include(new LatLng(pointsItem.getLatitude(), pointsItem.getLongitude()));
                pathCoordinates.add(pointsItem);
            }
            polygonOptions.color(Color.DKGRAY);
           Polyline polygon = gmap.addPolyline(polygonOptions);


            final LatLngBounds.Builder finalBuilder = builder;

            gmap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {

                    LatLngBounds bounds = finalBuilder.build();
                    gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getBaseContext().getAssets().open("trip.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            Log.d("data",json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }
}
