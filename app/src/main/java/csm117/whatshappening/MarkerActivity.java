package csm117.whatshappening;

import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * Created by anthonyxue on 11/20/16.
 */

public class MarkerActivity extends MapsActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_markers);

        // Get device display and make pop up percentage
        DisplayMetrics display_metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display_metrics);

        int height = display_metrics.heightPixels;
        int width = display_metrics.widthPixels;

        getWindow().setLayout((int) (width * .7), (int) (height * .7));
    }
}
