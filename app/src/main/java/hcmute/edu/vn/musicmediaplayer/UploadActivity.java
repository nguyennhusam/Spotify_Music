package hcmute.edu.vn.musicmediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.musicmediaplayer.Model.Song;

public class UploadActivity extends AppCompatActivity {

    private EditText mEditTextTitle;
    private EditText mEditTextArtist;
    private Button mButtonSelectFile, mButtonSelectImage;
    private TextView sTextViewInfo;
    private ImageView mImageViewSelectedFile;
    private Button mButtonUpload;

    private String mSelectedFilePath;
    private String sSelectedFilePath;

    private ProgressBar sProgressBar, iProgressBar;


    private Uri sImageUri;

    private Uri sSongUri;


    private StorageReference audioStorageRef;
    private StorageReference imageStorageRef;

    private DatabaseReference mDatabaseRef;

    private StorageTask audioUploadTask;
    private StorageTask imageUploadTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mEditTextTitle = findViewById(R.id.editText_title);
        mEditTextArtist = findViewById(R.id.editText_artist);
        mButtonSelectFile = findViewById(R.id.button_select_file);
        mImageViewSelectedFile = findViewById(R.id.imageView_selected_file);
        mButtonUpload = findViewById(R.id.button_upload);
        mButtonSelectImage = findViewById(R.id.button_select_image);
        sTextViewInfo = findViewById(R.id.tv_song_info);

        sProgressBar = findViewById(R.id.progress_bar);
        iProgressBar = findViewById(R.id.progress_bar_image);


        audioStorageRef = FirebaseStorage.getInstance().getReference("audio");
        imageStorageRef = FirebaseStorage.getInstance().getReference("image");

        mDatabaseRef = FirebaseDatabase.getInstance("https://musicapp-694ed-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("uploads");

        mButtonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        mButtonSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioUploadTask != null && audioUploadTask.isInProgress()) {
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
            System.out.println("Selected File Path: "+mSelectedFilePath);

            Picasso.get().load(sImageUri).into(mImageViewSelectedFile);
        }

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            sSongUri = data.getData();
            String fileName = getAudioName(sSongUri);
            int audioDurationInMs = getAudioDuration(sSongUri);
            int audioDurationInMin = (int) Math.ceil(audioDurationInMs / 1000.0 / 60.0);

            sTextViewInfo.setText(fileName +"--"+audioDurationInMin);


//            mImageViewSelectedFile.setVisibility(View.VISIBLE);

        }
    }
    private String getAudioName(Uri audioUri) {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(audioUri, null, null, null, null);
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String audioName = cursor.getString(nameIndex);
        cursor.close();
        return audioName;
    }
    private int getAudioDuration(Uri audioUri)  {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, audioUri);
        String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Integer.parseInt(durationStr);
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        String title = mEditTextTitle.getText().toString();
        String artist = mEditTextArtist.getText().toString();
        Song upload = new Song();


        if (sSongUri != null) {
            StorageReference audioRef = audioStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(sSongUri));
            StorageReference imageRef = imageStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(sImageUri));

            // Upload file mp3 và file ảnh đồng thời
            List<Task<Uri>> tasks = new ArrayList<>();
            tasks.add(audioRef.putFile(sSongUri)
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        sProgressBar.setProgress((int) progress);
                        // Update tiến trình cho file ảnh
                    })
                    .continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Trả về Uri của file audio đã upload


                return audioRef.getDownloadUrl();
            }));
            tasks.add(imageRef.putFile(sImageUri)
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        iProgressBar.setProgress((int) progress);
                        // Update tiến trình cho file ảnh
                    })
                    .continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Trả về Uri của file ảnh đã upload
                return imageRef.getDownloadUrl();
            }));


            Tasks.whenAllSuccess(tasks).addOnSuccessListener(results -> {
                Uri audioUrl = (Uri) results.get(0);
                Uri imageUrl = (Uri) results.get(1);
                // Thực hiện các thao tác tiếp theo với Uri của 2 file
                upload.setsName(title);
                upload.setsArtist(artist);
                upload.setsSongUrl(audioUrl.toString());
                upload.setsImageUrl(imageUrl.toString());

                String uploadId = mDatabaseRef.push().getKey();
                boolean b = mDatabaseRef.child(uploadId).setValue(upload).isSuccessful();
                Toast.makeText(UploadActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

            }).addOnFailureListener(e -> {
                Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                // Xử lý lỗi khi upload file không thành công
            });

//            audioUploadTask = fileReference.putFile(sSongUri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        Song upload  = new Song();
//
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mProgressBar.setProgress(0);
//                                }
//                            }, 500);
//
//                            Toast.makeText(UploadActivity.this, "Upload audio successful", Toast.LENGTH_LONG).show();
////                            Song upload = new Song(title,
////                                    fileReference.getDownloadUrl().toString(),
////                                    artist,
////                                    "");
//                            Task<Uri> downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl();
//                            downloadUri.addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Uri> task) {
//                                    //get final downloaded url for uploaded file
//                                    String finalDownloadUrl = task.getResult().toString();
//                                    upload.setsSongUrl(finalDownloadUrl);
//                                    //push to firebase db
//                                    String uploadId = mDatabaseRef.push().getKey();
//                                    boolean b = mDatabaseRef.child(uploadId).setValue(upload).isSuccessful();
//                                    System.out.println(b);
//                                }
//                            });
//                            //boolean b= mDatabaseRef.child(uploadId).setValue(upload).isSuccessful();
//
//                        }
//
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                            mProgressBar.setProgress((int) progress);
//                        }
//
//                    });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}