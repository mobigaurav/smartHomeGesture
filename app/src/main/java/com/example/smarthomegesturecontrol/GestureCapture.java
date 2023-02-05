package com.example.smarthomegesturecontrol;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class GestureCapture extends MainActivity {
    Button practiceBtn;
    Button uploadBtn;
    int Counter = 0;
    int gestureType;
    File mediaFile;
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

    /**
     * This method uploads to firebase server
     */
    private void uploadVideo() {
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        if (fileUri != null) { String fileName = getVideoName(gestureType);
           String fileType = ".mp4";
           String completeFileName = fileName + fileType;
           FirebaseStorage storage = FirebaseStorage.getInstance();
           StorageReference storageRef = storage.getReference();
           storageRef = storageRef.child("/videos/" + "/" + completeFileName);

           UploadTask uploadTask = storageRef.putFile(fileUri);
            uploadTask.addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Upload succeeded
                    Log.d("SUCCESSFULLY DONE", "uploadFromUri:onSuccess");
                    // Get the public download URL
                    String mDownloadUrl = taskSnapshot.getMetadata().getPath();
                    Log.i("PATH",  mDownloadUrl);
                    progressDialog.hide();
                    Toast.makeText(GestureCapture.this, "Successfully done", Toast.LENGTH_SHORT).show();
                    Intent returnBtn = new Intent(getApplicationContext(),
                            MainActivity.class);

                    startActivity(returnBtn);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.hide();
                    Log.w("EXCEPTION", "uploadFromUri:onFailure", e);
                    Toast.makeText(GestureCapture.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent returnBtn = new Intent(getApplicationContext(),
                            MainActivity.class);

                    startActivity(returnBtn);
                }
            });

        }
    }

    /**
     * This method returns videoName based on gesture selection
     * @param  gestureSelection  dropdown selection user did on Main activity
     * @return gestureName based on gestureSelection
     */

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

    /**
     * This method checks if camera is available or not
     * @return boolean value as true or false
     */
    private boolean isCameraAvailable(){
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)) {
        return true;
        }else { return false;
        }
    }

    /**
     * This method request for camera permissions
     */
    private void getCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    /**
     * This method starts capturing recording from video camera
     */
    private void startVideoCapture(){
        Counter++;
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        intent.putExtra(MediaStore.Video.Media.DISPLAY_NAME, getVideoName(gestureType));
        startActivityForResult(intent, VIDEO_RECORD_CODE);
    }

    /**
     * This method get Uri from File
     * @param  type  is a Media type for Either image or video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * This method return mediaFile url
     *  * @param  type  is a Media type for Either image or video
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
                fileUri = data.getData();
                Log.i("VIDEO_RECORD_TAG", "Video recorded at path" + videoPath);
                Log.i("VIDEO_RECORD_TAG_ACTUAL", "ACTUAL Video recorded at path" + fileUri);
            }
            else if(resultCode == RESULT_CANCELED) {
                Log.i("VIDEO_RECORD_TAG", "Recording video Cancelled");
            } else {
                Log.i("VIDEO_RECORD_TAG", "Recording video Error");
            }
        }
    }
}
