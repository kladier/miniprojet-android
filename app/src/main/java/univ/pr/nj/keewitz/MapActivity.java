package univ.pr.nj.keewitz;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import univ.pr.nj.keewitz.models.PointOfInterest;
import univ.pr.nj.keewitz.utils.PermissionUtils;

public class MapActivity extends AppCompatActivity
    implements SearchView.OnQueryTextListener, GoogleMap.OnMapClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int DEFAULT_ZOOM_LEVEL = 15;
    private static final double UPS_LAT = 43.5612975;
    private static final double UPS_LGT = 1.4649479;
    // Use for the CursorAdapter suggestions
    private static final String COLUMN_NAME = "name";

    private GoogleMap gMap;
    private SearchView searchView;
    private SimpleCursorAdapter myAdapter;
    private List<PointOfInterest> pointsOfInterests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myAdapter = new SimpleCursorAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            null,
            new String[] { COLUMN_NAME },
            new int[] { android.R.id.text1 }, // text1 = id of the TextField Widget
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toast.makeText(this, R.string.help, Toast.LENGTH_LONG).show();

        new FetchPointOfInterest().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create the search button on Action Bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        // Suggestions setup
        searchView.setSuggestionsAdapter(myAdapter);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int i) {
                CursorAdapter ca = searchView.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(i);
                searchView.setQuery(
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    false
                );
                return true;
            }
        });
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        gMap = map;
        gMap.setOnMapClickListener(this);
        zoomOnPositionWithCamera(UPS_LAT, UPS_LGT);
        enableMyLocation();
    }

    @Override
    public void onMapClick(final LatLng latLng) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText recordNameInput = new EditText(this);

        builder
            .setView(recordNameInput)
            .setTitle(R.string.dialog_title)
            .setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String name = recordNameInput.getText().toString();
                    PointOfInterest pi = new PointOfInterest(latLng, name);
                    pointsOfInterests.add(pi);
                    // TODO save the point of interest in firebase
                    addMarkerToMap(pi.getPosition(), pi.getName());
                }
            })
            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        builder.create();
        builder.show();
    }


    /**
     * Search for point of interest saved and zoom to it
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        PointOfInterest p = getPointOfInterestByName(query);

        if (p != null) {
            LatLng latLng = p.getPosition();
            zoomOnPositionWithCamera(latLng.latitude, latLng.longitude);
        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String search = newText.toLowerCase();
        int count = 0;
        final MatrixCursor mc = new MatrixCursor(new String[]{ BaseColumns._ID, COLUMN_NAME });

        for (PointOfInterest p : pointsOfInterests) {
            if (p.getName().toLowerCase().startsWith(search)) {
                mc.addRow(new Object[] {count, p.getName()});
            }
            count++;
        }
        myAdapter.changeCursor(mc);

        return false;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        }
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


    /**
     * Move the google Map with a translate effect
     */
    private void zoomOnPositionWithCamera(double lat, double lng) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, lng));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(DEFAULT_ZOOM_LEVEL);
        gMap.moveCamera(center);
        gMap.animateCamera(zoom);
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE
            );
        }
        else {
            // Permission has already been granted
            gMap.setMyLocationEnabled(true);
        }
    }

    private void addMarkerToMap(final LatLng latLng, String title) {
        MarkerOptions record = new MarkerOptions();
        record.position(latLng);
        record.title(title);
        gMap.addMarker(record);
    }

    private void addMarkersFromPointOfInterests(List<PointOfInterest> pis) {
        for (PointOfInterest p : pis) {
            addMarkerToMap(p.getPosition(), p.getName());
        }
    }

    private PointOfInterest getPointOfInterestByName(String name) {
        for (PointOfInterest p : pointsOfInterests) {
            if (p.getName().equals(name)) {
                return p;
            }
        }

        return null;
    }


    /**
     * Retrieve the points of interests in a async way from a external services (Firebase).
     * We'll set the marker on the map and use this infos to propose suggestions for the search.
     */
    private class FetchPointOfInterest extends AsyncTask<String, List<String>, List<PointOfInterest>> {

        @Override
        protected List<PointOfInterest> doInBackground(String... params) {
            //TODO: Use firebase data
            return new ArrayList<PointOfInterest>() {{
                add(new PointOfInterest(new LatLng(43.5603773, 1.4707398), "u1"));
                add(new PointOfInterest(new LatLng(43.5608447, 1.4704827), "u2"));
                add(new PointOfInterest(new LatLng(43.5608052, 1.4699231), "u3"));
                add(new PointOfInterest(new LatLng(43.5628344, 1.4690326), "u4"));
                add(new PointOfInterest(new LatLng(43.5638412, 1.465313), "Library of Science"));
                add(new PointOfInterest(new LatLng(43.56226, 1.4634004), "Restaurant 1"));
            }};
        }

        @Override
        protected void onPostExecute(List<PointOfInterest> result) {
            Log.d(this.getClass().getCanonicalName(), "get " + result.size() + "points of interests");
            addMarkersFromPointOfInterests(result);
            pointsOfInterests = result;
        }
    }
}
