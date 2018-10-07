package marsplay.com.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_IMAGE = 100;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private TextView uploadFromGallery,uploadFromCamera,viewUploadedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initializeView();
        setonClickListner();

        /*Map config = new HashMap();
        config.put("cloud_name", "shank");
        config.put("api_key", "644617911542992");
        config.put("api_secret", "oW3bQk8luOT9UlEkRsH21KoQkxY");
        MediaManager mediaManager = MediaManager.get();
        MediaManager.init(this, config);*/
    }

    private void initializeView(){
        uploadFromGallery = (Button)findViewById(R.id.upload_from_gallery);
        uploadFromCamera = (Button)findViewById(R.id.upload_from_camera);
        viewUploadedImage = (Button)findViewById(R.id.View_uploaded_image);
    }

    private void setonClickListner(){
        uploadFromGallery.setOnClickListener(this);
        uploadFromCamera.setOnClickListener(this);
        viewUploadedImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.upload_from_gallery:
                openGallery();
                break;
            case R.id.upload_from_camera:
                if (!checkPermission()) {
                    requestPermission();
                }else {
                    Intent intent = new Intent(HomeScreen.this,CameraActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.View_uploaded_image:
                Intent intent = new Intent(HomeScreen.this,ImageListActivity.class);
                startActivity(intent);
            default:
                break;
        }
    }

    private boolean checkPermission() {
        int result = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA, RECORD_AUDIO, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalStorage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalStorage = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && writeExternalStorage && readExternalStorage) {
                        Intent intent = new Intent(HomeScreen.this,CameraActivity.class);
                        startActivity(intent);
                    } else {
                        requestPermission();
                    }
                }
                break;
        }
    }

    public void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                if(bitmap!=null) {
                    Intent intent = new Intent(this, UploadingPageActivity.class);
                    intent.putExtra("imageUri", uri.toString());
                    intent.putExtra("activityIndex", 1);
                    startActivity(intent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
