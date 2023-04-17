package hcmute.edu.vn.musicmediaplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import androidx.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import hcmute.edu.vn.musicmediaplayer.Model.Album;
import hcmute.edu.vn.musicmediaplayer.Model.Song;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewPlaylistDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPlaylistDialogFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private EditText playlistNameEditText,playlistDescriptionEditText ;
    private Button saveButton,cancelButton;

    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    public NewPlaylistDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewPlaylistDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewPlaylistDialogFragment newInstance(String param1, String param2) {
        NewPlaylistDialogFragment fragment = new NewPlaylistDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_playlist_dialog, container, false);
        // Lấy kích thước màn hình
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        // Tính toán kích thước cho DialogFragment
        int dialogWidth = (int) (screenWidth * 0.8);
        int dialogHeight = (int) (screenHeight * 0.6);

        System.out.println(dialogHeight +"   " + dialogWidth);
        // Set chiều rộng và chiều cao cho DialogFragment
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);


         playlistNameEditText = view.findViewById(R.id.playlist_name_edit_text);
         playlistDescriptionEditText = view.findViewById(R.id.playlist_description_edit_text);
         saveButton = view.findViewById(R.id.save_button);
         cancelButton = view.findViewById(R.id.cancel_button);

        mDatabaseRef = FirebaseDatabase.getInstance("https://musicapp-694ed-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("albums");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý lưu thông tin playlist mới ở đây
                addNewAlbum();
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Inflate the layout for this fragment
        // Tìm các thành phần UI của Dialog Fragment ở đây
        return view;    }

    private void addNewAlbum() {
        String name = playlistNameEditText.getText().toString();
        String description = playlistDescriptionEditText.getText().toString();
        if (name != null) {

            Album album = new Album();
            album.setName(name);
            album.setDescription(description);
            album.setAlbumImageUrl("https://cdn-icons-png.flaticon.com/512/25/25667.png");

            String albumId = mDatabaseRef.push().getKey();
            album.setId(albumId);
            mDatabaseRef.child(albumId).setValue(album);
        }
    }
}