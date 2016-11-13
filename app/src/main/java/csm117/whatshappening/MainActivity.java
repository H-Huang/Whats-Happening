package csm117.whatshappening;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private String CREATE_NOTE = "";
    private String GET_NOTE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CREATE_NOTE = getString(R.string.create_location_notes);
        GET_NOTE = getString(R.string.get_location_notes);

        final Button map_redirect = (Button) findViewById(R.id.map_redirect);
        assert map_redirect != null;
        map_redirect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(i);
            }
        });

        final Button create_note = (Button) findViewById(R.id.create_note);
        assert create_note != null;
        create_note.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String postReturnText = "please work";
                createLocation();

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, postReturnText, duration);
                toast.show();
            }
        });

        final Button get_notes = (Button) findViewById(R.id.get_notes);
        assert get_notes != null;
        get_notes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String postReturnText = "get notes";
                getLocations();

//                Context context = getApplicationContext();
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast = Toast.makeText(context, postReturnText, duration);
//                toast.show();
            }
        });
    }

    private void createLocation(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CREATE_NOTE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("created", "2016-11-13T03:53:17.826999Z");
                params.put("title", "post");
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

    private void getLocations(){

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, GET_NOTE, (String)null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Response", response.toString());
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

        int socketTimeout = 6000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        getRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(getRequest);
    }


}
