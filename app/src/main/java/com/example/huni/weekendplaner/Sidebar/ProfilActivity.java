package com.example.huni.weekendplaner.Sidebar;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.huni.weekendplaner.Login.User;
import com.example.huni.weekendplaner.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

        //With Shared Preferences we get the current user
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.apply();
        final String s = settings.getString("username","Dummy");
        user_number = s;

        //We set up the Firebase connection to the user node
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            //Here we set the User Lastname and Firstname in the profile
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    if(Objects.equals(dataSnapshot1.getKey(), s)) {
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

        //Here we set up the view elements
        choose = findViewById(R.id.button_profil_chosse);
        upload = findViewById(R.id.button_profil_upload);
        profil_file_name = findViewById(R.id.edit_text_profil_file);
        imageView = findViewById(R.id.profil_image);

        //The storage reference is set up here
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseref = FirebaseDatabase.getInstance().getReference("uploads");

        //Choose button onClickListener
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        //Upload button onClickListener
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

    //Here we load the image into the imageview from the ProfileActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            mImageUri = data.getData();

            Glide.with(this)
                    .load(mImageUri)
                    .apply(new RequestOptions().override(500,500))
                    .into(imageView);

        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //Here we Upload the image to the storage
    private void uploadFile(){
        if(mImageUri != null){
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() +
                    "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        Upload upload = new Upload(profil_file_name.getText().toString().trim(),
                                downloadUri.toString());
                        mDatabaseref.child(user_number).setValue(upload);
                    }else
                    {
                        Toast.makeText(ProfilActivity.this, "upload failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}