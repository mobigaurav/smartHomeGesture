package com.example.smarthomegesturecontrol;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;


import java.io.File;
import java.util.HashMap;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class GestureCapture extends MainActivity {
    Button practiceBtn;
    Button uploadBtn;
    int Counter = 0;
    int gestureType;
    private static int CAMERA_PERMISSION_CODE = 100;
    private static int VIDEO_RECORD_CODE = 101;
    private static int MEDIA_TYPE_VIDEO = 102;
    private static int MEDIA_TYPE_IMAGE = 103;
    private static String VIDEO_DIRECTORY_NAME = "GESTURE_RECORDING";
    String videoPath;
    Uri fileUri;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesture_capture);
        practiceBtn = (Button)findViewById(R.id.practiceBtn);
        uploadBtn = (Button)findViewById(R.id.uploadBtn);
        Bundle extras = getIntent().getExtras();
        gestureType = extras.getInt("gestureType");
        if(isCameraAvailable()) {
            Log.i("VIDEO_RECORD", "Camera is detected");
            getCameraPermission();
            startVideoCapture();
        }else {
            Log.i("VIDEO_RECORD", "Camera is not detected");
        }

        practiceBtn = (Button) findViewById(R.id.practiceBtn);
        practiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVideoCapture();
            }
        });

        uploadBtn = (Button) findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("SEVER_UPLOAD", "Upload video to server");
                progressDialog = new ProgressDialog(GestureCapture.this);
                uploadVideo();
            }
        });
    }

//    private String filetype() {
//        ContentResolver r = getContentResolver();
//        // get the file type ,in this case its mp4
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        return mimeTypeMap.getExtensionFromMimeType(r.getType(fileUri));
//    }

    private void uploadVideo() {
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        if (fileUri != null) {
            // save the selected video in Firebase storage
            String fileName = getVideoName(gestureType);
            String fileType = ".mp4";
            // Create instance of StorageReference
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            final StorageReference photoRef = storageRef.child("FolderToCreate").child("NameYoWantToAdd");
// add File/URI
            photoRef.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Upload succeeded
                            Toast.makeText(getApplicationContext(), "Upload Success...", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Upload failed
                            Toast.makeText(getApplicationContext(), "Upload failed...", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(
                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
    }



    private String getVideoName(int gestureSelection) {
        String gestureName = "";
        switch (gestureSelection) {
            case 1:
                gestureName = "LightOn" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 2:
                gestureName = "LightOff" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 3:
                gestureName = "FanOn" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 4:
                gestureName = "FanOff" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 5:
                gestureName = "FanUp" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 6:
                gestureName = "FanDown" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 7:
                gestureName = "SetThermo" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 8:
                gestureName = "Num0" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 9:
                gestureName = "Num1" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 10:
                gestureName = "Num2" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 11:
                gestureName = "Num3" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 12:
                gestureName = "Num4" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 13:
                gestureName = "Num5" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 14:
                gestureName = "Num6" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 15:
                gestureName = "Num7" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 16:
                gestureName = "Num8" + "_" + "PRACTICE" + "_" + Counter;
                break;
            case 17:
                gestureName = "Num9" + "_" + "PRACTICE" + "_" + Counter;
                break;
            default:
                break;
        }
        return  gestureName;
    }

    private boolean isCameraAvailable(){
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)) {
        return true;
        }else { return false;
        }
    }

    private void getCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void startVideoCapture(){
        Counter++;
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
       // intent.putExtra("android.intent.extras.CAMERA_FACING", cameraId());
        intent.putExtra(MediaStore.Video.Media.DISPLAY_NAME, getVideoName(gestureType));
        startActivityForResult(intent, VIDEO_RECORD_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private  File getOutputMediaFile(int type) {
        String videoName = getVideoName(gestureType);
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                VIDEO_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(String.valueOf(VIDEO_DIRECTORY_NAME), "Oops! Failed create "
                        + VIDEO_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + videoName + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + videoName + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == VIDEO_RECORD_CODE) {
            if(resultCode == RESULT_OK) {
                String videoPath = fileUri.getPath();
                Log.i("VIDEO_RECORD_TAG", "Video recorded at path" + videoPath);
            }
            else if(resultCode == RESULT_CANCELED) {
                Log.i("VIDEO_RECORD_TAG", "Recording video Cancelled");
            } else {
                Log.i("VIDEO_RECORD_TAG", "Recording video Error");
            }
        }
    }
}
