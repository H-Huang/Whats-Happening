package csm117.whatshappening;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by anthonyxue on 11/20/16.
 */

public class MarkerActivity extends MapsActivity{

    private String GET_NOTE = "";
    TextView title;
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markers);

        title = (TextView) findViewById(R.id.markerTitle);
        content = (TextView) findViewById(R.id.markerContent);

        Intent getId = getIntent();
        Integer id = 0;
        id = getId.getIntExtra("id", 0);
        // Append ID
        GET_NOTE = getString(R.string.get_location_notes) + id  + '/';
        getLocations(GET_NOTE);

        // Get device display and make pop up percentage
        DisplayMetrics display_metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display_metrics);

        int height = display_metrics.heightPixels;
        int width = display_metrics.widthPixels;

        getWindow().setLayout((int) (width * .7), (int) (height * .7));
    }

    private void getLocations(String url){

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            title.setText(response.getString("title"));
                            content.setText(response.getString("description"));
                        } catch (JSONException e) {
                            e.printStackTrace();
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

        int socketTimeout = 6000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        getRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(getRequest);
    }
}

