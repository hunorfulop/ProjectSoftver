package com.example.huni.weekendplaner.Sidebar;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huni.weekendplaner.Login.User;
import com.example.huni.weekendplaner.Main.ListDataEvent;
import com.example.huni.weekendplaner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfilActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference ref;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button choose;
    private Button upload;
    private EditText profil_file_name;
    private ImageView imageView;
    private String user_number;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    DatabaseReference mDatabaseref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.apply();
        final String s = settings.getString("username","Dummy");
        user_number = s;
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    System.out.println("TAG771 " + dataSnapshot1.getKey() + '\t' + s);
                    if(Objects.equals(dataSnapshot1.getKey(), s)) {
                    System.out.println("TAG771 " + dataSnapshot1.getKey() + '\t' + s);
                    User user = dataSnapshot1.getValue(User.class);
                    User listDataUser = new User();
                    assert user != null;
                    String firstname = user.getFirstname();
                    String lastname = user.getLastname();
                    listDataUser.setFirstname(firstname);
                    listDataUser.setLastname(lastname);
                    setText(firstname,lastname);
                } }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Gone wrong",Toast.LENGTH_LONG).show();
            }
        });

        choose = findViewById(R.id.button_profil_chosse);
        upload = findViewById(R.id.button_profil_upload);
        profil_file_name = findViewById(R.id.edit_text_profil_file);
        imageView = findViewById(R.id.profil_image);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseref = FirebaseDatabase.getInstance().getReference("uploads");


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });
    }

    public void setText(String firstname, String lastname){
        TextView textView_firstName = findViewById(R.id.textView_firstName);
        TextView textView_lastName = findViewById(R.id.textView2_lastName);
            textView_firstName.setText(firstname);
            textView_lastName.setText(lastname);
    }

    public void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(imageView);

        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile(){
        if(mImageUri != null){
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() +
                    "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //mProgressbar
                        }
                    },5000);
                    Toast.makeText(ProfilActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                    Upload upload = new Upload(profil_file_name.getText().toString().trim(),
                            fileReference.getDownloadUrl().toString());
                    //String uploadID = mDatabaseref.push().getKey();
                    mDatabaseref.child(user_number).setValue(upload);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfilActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    //mProgressbar
                }
            });
        }else {
            Toast.makeText(this,"No file selected",Toast.LENGTH_SHORT).show();
        }
    }
}
