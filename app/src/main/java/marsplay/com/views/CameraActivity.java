package marsplay.com.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import marsplay.com.controller.CameraPreview;

public class CameraActivity extends Activity implements View.OnClickListener {
    private Camera mCamera;
    private CameraPreview mPreview;
    private Context myContext;
    private FrameLayout cameraPreviewLayout;
    boolean isFlash;
    Button captureButton, toggle_Flash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;

        initialize();

        setOnClickListner();
    }

    public void initialize() {

        cameraPreviewLayout = (FrameLayout) findViewById(R.id.camera_preview);
        captureButton = (Button) findViewById(R.id.button);
        toggle_Flash = (Button) findViewById(R.id.button_flash);

        mPreview = new CameraPreview(myContext, mCamera);
        cameraPreviewLayout.addView(mPreview);
    }

    private void setOnClickListner() {
        toggle_Flash.setOnClickListener(this);
        captureButton.setOnClickListener(this);
    }

    public void onResume() {
        super.onResume();
        if (!hasCamera(myContext)) {
            Toast toast = Toast.makeText(myContext, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        if (mCamera == null) {
            mCamera = Camera.open(findBackFacingCamera());
            mPreview.refreshCamera(mCamera);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    private boolean hasCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_flash:
                if (!isFlash) {
                    mPreview.toggleFlash(true);
                    isFlash = true;
                    toggle_Flash.setBackgroundResource(R.drawable.ic_flash_on);
                } else {
                    mPreview.toggleFlash(false);
                    isFlash = false;
                    toggle_Flash.setBackgroundResource(R.drawable.ic_flash_off);
                }
                break;
            case R.id.button:
                mCamera.takePicture(null, null, pictureCallback);
                break;
            default:
                break;
        }
    }

    PictureCallback pictureCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bitmap == null) {
                Toast.makeText(CameraActivity.this, "Captured image is empty", Toast.LENGTH_LONG).show();
                return;
            }

            String filePath = tempFileImage(CameraActivity.this, bitmap, "name");

            Intent intent = new Intent(CameraActivity.this, UploadingPageActivity.class);
            intent.putExtra("imageUri", filePath);
            intent.putExtra("activityIndex", 0);
            startActivity(intent);
        }
    };

    public static String tempFileImage(Context context, Bitmap bitmap, String name) {

        File outputDir = context.getCacheDir();
        File imageFile = new File(outputDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing file", e);
        }
        return imageFile.getAbsolutePath();
    }
}