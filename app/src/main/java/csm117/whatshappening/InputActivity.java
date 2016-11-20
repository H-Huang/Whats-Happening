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
    }

    // Function to display the message that the user enters
    public final static String EXTRA_MESSAGE = "hello";
    public void sendMessage(View view) {
        Intent display = new Intent(this, MapsActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        display.putExtra(EXTRA_MESSAGE, message);
        startActivity(display);
    }
}
