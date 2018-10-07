package marsplay.com.views;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import marsplay.com.model.Resource;
import marsplay.com.database.DataBaseHandler;

public class UploadingPageActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mImageView;
    private Button uploadImageButton;
    private Uri myUri;
    private DataBaseHandler helper;
    private ProgressBar progressBar;
    final int PIC_CROP = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading_page);
        initializeView();
        setOnClickListner();
    }

    private void initializeView(){

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mImageView = (ImageView) findViewById(R.id.uploadImage);
        uploadImageButton = (Button) findViewById(R.id.uploadButton);


        progressBar.setVisibility(View.GONE);
        helper = new DataBaseHandler(MainApplication.get());


        Intent intent = getIntent();
        int activityIndex = intent.getIntExtra("activityIndex",0);
        String uriString = intent.getStringExtra("imageUri");

        setImageView(activityIndex, uriString);

    }


    private void setOnClickListner(){
        uploadImageButton.setOnClickListener(this);
    }

    private void setImageView(int index,String uri){

        Bitmap bitmap=null;

        if(index==0){
            myUri = Uri.fromFile(new File(uri));
        }else {
            myUri = Uri.parse(uri);
        }

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), myUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(bitmap!=null) {
            try {
                mImageView.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.uploadButton:
                uploadImageToServer();
                break;
            default:
                break;
        }
    }

    private void uploadImageToServer(){

        try {
            progressBar.setVisibility(View.VISIBLE);
            String requestId = MediaManager.get().upload(myUri).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                    System.out.println("requestId"+requestId);
                }
                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                    Double progress = (double) bytes/totalBytes;
                    int prog = Integer.valueOf(progress.intValue());
                    progressBar.setProgress(prog);
                }
                @Override
                public void onSuccess(String requestId, Map resultData) {
                    String publicId = (String) resultData.get("public_id");
                    insertResource(publicId,requestId);
                }
                @Override
                public void onError(String requestId, ErrorInfo error) {
                    progressBar.setVisibility(View.GONE);
                }
                @Override
                public void onReschedule(String requestId, ErrorInfo error) {
                    progressBar.setVisibility(View.GONE);
                }}).dispatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertResource(String publicId,String requestId){

        Resource resource = new Resource();
        resource.setRequestId(requestId);
        resource.setLocalUri(myUri.toString());
        resource.setCloudinaryPublicId(publicId);

        long id = helper.insertResourceRequest(resource);
        if(id!=-1){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Image is Uploaded Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
