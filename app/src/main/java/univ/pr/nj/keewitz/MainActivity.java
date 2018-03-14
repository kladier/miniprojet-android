package univ.pr.nj.keewitz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import univ.pr.nj.keewitz.utils.FirebaseUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView map, settings, timetable, qrcode, infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        map = findViewById(R.id.map);
        settings = findViewById(R.id.settings);
        timetable = findViewById(R.id.time_table);
        qrcode = findViewById(R.id.qrcode);
        infos = findViewById(R.id.informations);

        map.setOnClickListener(this);
        settings.setOnClickListener(this);
        timetable.setOnClickListener(this);
        qrcode.setOnClickListener(this);
        infos.setOnClickListener(this);

        this.exampleFirebase();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.map:
                startActivity(new Intent(this, MapActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.time_table:
                startActivity(new Intent(this, TimeTableActivity.class));
                break;
            case R.id.qrcode:
                startActivity(new Intent(this, ScanQRCodeActivity.class));
                break;
            case R.id.informations:
                startActivity(new Intent(this, InformationActivity.class));
                break;
            default:
                Log.d(this.getClass().getCanonicalName(), "You forgot to set the startActivity binding with your new button");
        }
    }

    public void exampleFirebase() {
        FirebaseUtils.writeValue("myValue", "level1", "level2");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.w("MainActivity.class", (String)snapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        FirebaseUtils.readValue(valueEventListener, "level1", "level2");
    }
}
