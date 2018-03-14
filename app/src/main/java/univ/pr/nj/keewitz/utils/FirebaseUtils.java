package univ.pr.nj.keewitz.utils;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public final class FirebaseUtils {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public FirebaseUtils(){}

    public static UploadTask putFile(String reference, Uri fileUri){
        mAuth.signInAnonymously();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference(reference);
        return storageReference.putFile(fileUri);
    }

    public static void writeValue(String value, String... path){
        mAuth.signInAnonymously();
        DatabaseReference mDB = FirebaseDatabase.getInstance().getReference();
        for (String p : path) {
            mDB = mDB.child(p);
        }
        mDB.setValue(value);
    }

    public static String readValue(ValueEventListener valueEventListener, String... path){
        final String[] value = null;
        mAuth.signInAnonymously();
        DatabaseReference mDB = FirebaseDatabase.getInstance().getReference();
        for (String p : path) {
            mDB = mDB.child(p);
        }
        mDB.addListenerForSingleValueEvent(valueEventListener);
        return "";
    }

}
