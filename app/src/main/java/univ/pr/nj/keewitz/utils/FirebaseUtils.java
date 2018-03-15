package univ.pr.nj.keewitz.utils;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public final class FirebaseUtils {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public FirebaseUtils(){}

    public static StorageReference putFile(String reference, Uri fileUri, OnSuccessListener successListener){
        mAuth.signInAnonymously();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference(reference);
        storageReference.putFile(fileUri).addOnSuccessListener(successListener);
        return storageReference;
    }

    public static void writeValue(String value, String... path){
        mAuth.signInAnonymously();
        DatabaseReference mDB = FirebaseDatabase.getInstance().getReference();
        for (String p : path) {
            mDB = mDB.child(p);
        }
        mDB.setValue(value);
    }

    public static void readValue(ValueEventListener valueEventListener, String... path){
        mAuth.signInAnonymously();
        DatabaseReference mDB = FirebaseDatabase.getInstance().getReference();
        for (String p : path) {
            mDB = mDB.child(p);
        }
        mDB.addListenerForSingleValueEvent(valueEventListener);
    }

}
