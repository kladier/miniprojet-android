package univ.pr.nj.keewitz;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import univ.pr.nj.keewitz.utils.FirebaseUtils;

public class TimeTableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final WebView wb = (WebView) findViewById(R.id.time_table);
        wb.getSettings().setJavaScriptEnabled(true);

        String pathToTimeTableUrl = getString(R.string.timetableUrl);

        // Celcat Calendar For M2DL
        FirebaseUtils.readValue(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.w("TimeTableActivity.class", "Time table URL : "+snapshot.getValue().toString());
                wb.loadUrl(snapshot.getValue().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        },pathToTimeTableUrl);

       /* String url = getResources().getString(R.string.default_url_time_table);
        if (url == null || url.isEmpty()) {
            url = getResources().getString(R.string.default_url_time_table);
        }
        wb.loadUrl(url);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
