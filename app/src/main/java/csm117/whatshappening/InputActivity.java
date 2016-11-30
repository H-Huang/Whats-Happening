package csm117.whatshappening;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class InputActivity extends AppCompatActivity {

    private String CREATE_NOTE = "";
    private String GET_NOTE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        CREATE_NOTE = getString(R.string.create_location_notes);
        GET_NOTE = getString(R.string.get_location_notes);

        // This is from the pressing the plus button Intent and gives pre-filled value
        Intent getLocation = getIntent();
        final String stringLat = getLocation.getStringExtra("paramLat");
        final String stringLong = getLocation.getStringExtra("paramLong");
        EditText replacelatValue = (EditText) findViewById(R.id.latValue);
        replacelatValue.setText(stringLat);
        EditText replacelongValue = (EditText) findViewById(R.id.longValue);
        replacelongValue.setText(stringLong);

        final Button create_note = (Button) findViewById(R.id.sendButton);
        assert create_note != null;
        create_note.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Grabs the string that the user inputs
                EditText stringlatValue = (EditText) findViewById(R.id.latValue);
                final String finallatValue = stringlatValue.getText().toString();
                EditText stringlongValue = (EditText) findViewById(R.id.longValue);
                final String finallongValue = stringlongValue.getText().toString();
                EditText stringTitle = (EditText) findViewById(R.id.edit_title);
                final String title = stringTitle.getText().toString();
                EditText stringMessage = (EditText) findViewById(R.id.edit_message);
                final String message = stringMessage.getText().toString();

                createLocation(title, message, finallatValue, finallongValue);
                Intent redirect = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(redirect);
                finish();
            }
        });

        //back button
        final Button map_redirect = (Button) findViewById(R.id.back_button);
        assert map_redirect != null;
        map_redirect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void createLocation(final String stringTitle, final String stringDescription, final String latLocation, final String longLocation){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CREATE_NOTE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(InputActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(InputActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                HashMap<String, String> params = new HashMap<String, String>();
                // Just 5 parameters for hashing
                params.put("title", stringTitle);
                params.put("description", stringDescription);
                params.put("latitude", latLocation);
                params.put("longitude", longLocation);
                params.put("upvotes", "30");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
