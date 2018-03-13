package univ.pr.nj.keewitz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;

/**
 * Created by what on 08/03/18.
 */

public class ScanQRCodeActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.takePhoto();
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

    public void takePhoto() {
        final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

        //Création d'un intent
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        //Création du fichier image
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);

        //On lance l'intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                Bitmap qrCodeBitmap = BitmapFactory.decodeResource(
                        getApplicationContext().getResources(),
                        R.drawable.example_qrcode_1);

                BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext())
                                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                                .build();
                if(!detector.isOperational()){
                    displayTxtView("Could not set up the detector!");
                    return;
                }

                String urlFromCode = getUrlFromCode(detector, qrCodeBitmap);

                displayQRCodeImg(qrCodeBitmap);
                displayTxtView(urlFromCode);
                displayPdfInFrame(urlFromCode);
        }
    }

    private void displayTxtView(String txt) {
        TextView txtView = findViewById(R.id.qrcode);
        txtView.setText(txt);
    }

    private void displayQRCodeImg(Bitmap qrCodeBitmap) {
        ImageView myImageView = findViewById(R.id.imageView);
        myImageView.setImageBitmap(qrCodeBitmap);
    }

    private void displayPdfInFrame(String url) {
        WebView wb = findViewById(R.id.pdf_webview);
        wb.setBackgroundColor(0x00000000);
        wb.getSettings().setJavaScriptEnabled(true);

        if (url == null || url.isEmpty()) {
            Log.w("ScanQRCodeActivity", "Error URL empty or null");
        }

        String url_with_google_viewer = "http://docs.google.com/gview?embedded=true&url="+url;
        wb.loadUrl(url_with_google_viewer );
    }

    public String getUrlFromCode(BarcodeDetector detector, Bitmap qrCodeBitmap) {
        Frame frame = new Frame.Builder().setBitmap(qrCodeBitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);

        return barcodes.valueAt(0).rawValue;
    }
}
