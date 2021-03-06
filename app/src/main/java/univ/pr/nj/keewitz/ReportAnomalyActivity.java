package univ.pr.nj.keewitz;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import univ.pr.nj.keewitz.models.Anomaly;
import univ.pr.nj.keewitz.utils.FirebaseUtils;


public class ReportAnomalyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri imageUri;
    private String fileName;
    private Anomaly anomaly = new Anomaly();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_anomaly);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                0);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        addListenerOnSpinnerItemSelection();
        addListenerOnButtons();
        fileName = getFileName();
    }

    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Long timestamp = System.currentTimeMillis()/1000;
        File photo = new File(Environment.getExternalStorageDirectory(), "photo-"+timestamp+".png");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);

        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void addListenerOnSpinnerItemSelection() {
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    getContentResolver().notifyChange(imageUri, null);
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageURI(imageUri);
                }
        }
    }

    private void goToMain(){
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        anomaly.setCriticity(parent.getItemAtPosition(pos).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    public void addListenerOnButtons() {
        Button btnSubmit = findViewById(R.id.buttonSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null){
                    OnSuccessListener successListener = new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            anomaly.setUrlImage(taskSnapshot.getDownloadUrl().toString());
                            anomaly.sendInfosToFirebase(fileName);
                        }
                    };
                    StorageReference stRef = FirebaseUtils.putFile("anomalies/" + anomaly.getCriticity().toLowerCase() + "/" + fileName  + ".png", imageUri, successListener);
                    goToMain();
                } else {
                    toastNoImage();
                }

            }
        });

        Button btnPhoto = findViewById(R.id.buttonPhoto);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
    }

    @NonNull
    private String getFileName() {
        Long timestamp = System.currentTimeMillis()/1000;
        return "img-" + timestamp.toString();
    }

    private void toastNoImage(){
        Toast.makeText(this, "No image to upload", Toast.LENGTH_SHORT)
                .show();
    }
}
