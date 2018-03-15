package univ.pr.nj.keewitz;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import univ.pr.nj.keewitz.timetable.CustomOnItemSelectedListener;
import univ.pr.nj.keewitz.utils.FirebaseUtils;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.configureSpinner();

        final EditText t = findViewById(R.id.editText3);
        this.getUsername(t);
        this.setListenerOnEdit(t);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getSpinnerValue((Spinner)findViewById(R.id.spinner));
    }

    public void configureSpinner() {
        //configure spinner : add items
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.formation, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        this.getSpinnerValue(spinner);
    }

    public void getSpinnerValue(final Spinner spinner) {
        FirebaseUtils.readValue(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int position = 0;
                switch (dataSnapshot.getValue().toString()) {
                    case "M1 DL":
                        position = 2;
                        break;
                    case "M2 DL":
                        position = 1;
                        break;
                    case "L3 info":
                        position = 3;
                        break;
                }
                spinner.setSelection(position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }, "timetableUrl");
    }

    public void setListenerOnEdit(final EditText t) {
        t.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FirebaseUtils.writeValue( t.getText().toString(), "username");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getUsername(final EditText t) {
        FirebaseUtils.readValue(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                t.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }, "username");
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

