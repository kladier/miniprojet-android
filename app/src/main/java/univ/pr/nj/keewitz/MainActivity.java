package univ.pr.nj.keewitz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public void onMapButtonClick(View view) {
        startActivity(new Intent(this, MapActivity.class));
    }

    public void onTimeTableButtonClick(View view) {
        startActivity(new Intent(this, TimeTableActivity.class));
    }

    public void onSettingsButtonClick(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void onScanQRCode(View view) {
        startActivity(new Intent(this, ScanQRCodeActivity.class));
    }
}
