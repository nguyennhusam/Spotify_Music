package hcmute.edu.vn.musicmediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import hcmute.edu.vn.musicmediaplayer.Model.Song;

public class UploadActivity extends AppCompatActivity {

    private EditText mEditTextTitle;
    private EditText mEditTextArtist;
    private Button mButtonSelectFile;
    private ImageView mImageViewSelectedFile;
    private Button mButtonUpload;

    private String mSelectedFilePath;
    private String sSelectedFilePath;

    private ProgressBar mProgressBar;


    private Uri sImageUri;

    private Uri sSongUri;


    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mEditTextTitle = findViewById(R.id.editText_title);
        mEditTextArtist = findViewById(R.id.editText_artist);
        mButtonSelectFile = findViewById(R.id.button_select_file);
        mImageViewSelectedFile = findViewById(R.id.imageView_selected_file);
        mButtonUpload = findViewById(R.id.button_upload);

        mProgressBar = findViewById(R.id.progress_bar);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance("https://musicapp-694ed-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("uploads");


        mButtonSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(UploadActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/mpeg");
        startActivityForResult(intent, 2);
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            sImageUri = data.getData();
            mSelectedFilePath = sImageUri.getPath();
            mImageViewSelectedFile.setVisibility(View.VISIBLE);
        }

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            sSongUri = data.getData();
            sSelectedFilePath = sSongUri.getPath();
            mImageViewSelectedFile.setVisibility(View.VISIBLE);

        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        String title = mEditTextTitle.getText().toString();
        String artist = mEditTextArtist.getText().toString();
        if (sSongUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(sSongUri));

            mUploadTask = fileReference.putFile(sSongUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(UploadActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Song upload = new Song(title,
                                    fileReference.getDownloadUrl().toString(),
                                    artist,
                                    "");
                            Task<Uri> downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            downloadUri.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    //get final downloaded url for uploaded file
                                    String finalDownloadUrl = task.getResult().toString();
                                    upload.setsSongUrl(finalDownloadUrl);
                                    //push to firebase db
                                    String uploadId = mDatabaseRef.push().getKey();
                                    boolean b = mDatabaseRef.child(uploadId).setValue(upload).isSuccessful();
                                    System.out.println(b);
                                }
                            });
                            //boolean b= mDatabaseRef.child(uploadId).setValue(upload).isSuccessful();

                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }

                    });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}