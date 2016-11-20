package csm117.whatshappening;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

public class MapsActivity extends FragmentActivity implements
        LocationListener,
        OnMapReadyCallback,
        OnMarkerClickListener{

    // Strings for IP address
    private String CREATE_NOTE = "";
    private String GET_NOTE = "";

    // Declaration for use with Google Maps API
    GoogleMap googleMap;
    double longitude, latitude;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show error dialog if GooglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        setContentView(R.layout.activity_maps);
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        googleMap = supportMapFragment.getMap();

        // Sync map, implement onMapReady
        supportMapFragment.getMapAsync(this);

        // Can't set this to true without runtime permissions
        googleMap.setMyLocationEnabled(false);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);

        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(MapsActivity.this);
            }
        }

        CREATE_NOTE = getString(R.string.create_location_notes);
        GET_NOTE = getString(R.string.get_location_notes);


        final ArrayList<String> eventNames = new ArrayList<String>();
        final ArrayList<LatLng> allLonLats = getLocations(eventNames);
        // add all pre-existing markers to the map
        // Marker[] otherMarkers = addAllMarkers(allLonLats, googleMap);

        int i = 0;
        System.out.println("IN ON CREATE -- ABOUT TO START ITERATION OF LON LATS");
        System.out.println("Size of the location arraylist: " + allLonLats.size());
        for (LatLng lon_lat : allLonLats) {
            String eventTitle = eventNames.get(i);
            System.out.println("ITERATION " + i + "Event name: " + eventTitle);
            Marker loc_i = googleMap.addMarker(new MarkerOptions().position(lon_lat)
                    .title(eventTitle));
            i++;
        }

        // Commented out for now to work on the emulator
        /*Location location = locationManager.getLastKnownLocation(bestProvider);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
        */

        // Create FAB variable, implement an OnClickListener
        // and cause it to create an intent and start the InputActivity
        final FloatingActionButton floatingAdd = (FloatingActionButton) findViewById(R.id.floatingAdd);
        assert floatingAdd != null;
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent inputWindow = new Intent(getApplicationContext(), InputActivity.class);
                inputWindow.putExtra("paramLat", "" + latitude);
                inputWindow.putExtra("paramLong", "" + longitude);
                startActivity(inputWindow);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onLocationChanged(Location location) {
        TextView locationTv = (TextView) findViewById(R.id.latlongLocation);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        //googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);
    }

    // onMapReady, load marker
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Test marker
        Marker test = map.addMarker(new MarkerOptions().position(new LatLng(34.071413, -118.452905)).title("Hello world"));

        googleMap.setOnMarkerClickListener(this);
    }

    /**
     * Adds all markers corresponding to locations extracted from the database
     */
    private Marker[] addAllMarkers(ArrayList<LatLng> locs, GoogleMap map) {

        int numMarkers = locs.size();
        Marker[] mapMarkers = new Marker[numMarkers];

        for (int i = 0; i < numMarkers; i++) {
            Marker marker_i = map.addMarker(new MarkerOptions().position(locs.get(i)));
            mapMarkers[i] = marker_i;
        }

        return mapMarkers;
    }

    /**
     * Retrieve all the locations of events posted from other users
     * from the database;
     * returns an arraylist of longitude and latitude for each of the locations
     */
    private ArrayList<LatLng> getLocations(final ArrayList<String> eventNames){

        final ArrayList<LatLng> allLatLons = new ArrayList<LatLng>();
        // final ArrayList<String> eventNames = new ArrayList<String>();

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET,
                GET_NOTE, (String)null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        //result.add(response);
                        Log.d("Response", response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject eventInfo = null;
                            try {
                                eventInfo = response.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // get the necessary info from each of the JSONObjects
                            // Variable Names: Title, Description, Latitude, Longitude, etc.
                            String title = "";
                            try {
                                title = eventInfo.getString("title");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                String descrip = eventInfo.getString("description");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            double lat = 0;
                            try {
                                lat = eventInfo.getDouble("latitude");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            double lon = 0;
                            try {
                                lon = eventInfo.getDouble("longitude");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // store in allLatLons
                            LatLng eventLoc = new LatLng(lat, lon);
                            System.out.println("Event " + i + " Lat: " + lat + " Lon: " + lon);

                            eventNames.add(title);
                            allLatLons.add(eventLoc);

                            googleMap.addMarker(new MarkerOptions().position(eventLoc)
                                    .title(title));
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        //////////////////////////////////////////////////////////
        int socketTimeout = 6000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        getRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(getRequest);
        /////////////////////////////////////////////////////////

        System.out.println("EXITING GET LOCATIONS... SIZE OF LOCATION ARRAYLIST: " + allLatLons.size());
        return allLatLons;

    } // end getLocations()

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // When we click a marker, we want a pop up window
        startActivity(new Intent(getApplicationContext(), MarkerActivity.class));
        // Return false means we have not consumed event, default behavior will continue
        return false;
    }

    private void createLocation(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CREATE_NOTE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MapsActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                HashMap<String, String> params = new HashMap<String, String>();
                // Just 5 parameters for hashing
                params.put("created", "2016-11-13T03:53:17.826999Z");
                params.put("description", "plz-post");
                params.put("latitude", "100");
                params.put("longitude", "200");
                params.put("upvotes", "30");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}