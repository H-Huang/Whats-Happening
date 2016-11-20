package csm117.whatshappening;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Intent getLocation = getIntent();
        String stringLat = getLocation.getStringExtra("paramLat");
        String stringLong = getLocation.getStringExtra("paramLong");
        EditText replacelatValue = (EditText) findViewById(R.id.latValue);
        replacelatValue.setText(stringLat);
        EditText replacelongValue = (EditText) findViewById(R.id.longValue);
        replacelongValue.setText(stringLong);
    }

    public final static String EXTRA_MESSAGE = "hello";
    public void sendMessage(View view) {
        Intent send = new Intent(this, MapsActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        send.putExtra(EXTRA_MESSAGE, message);
        startActivity(send);
    }
}
