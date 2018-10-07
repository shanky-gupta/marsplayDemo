package marsplay.com.views;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cloudinary.Url;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.ResponsiveUrl;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.cloudinary.android.ResponsiveUrl.Preset.FIT;

public class ImageActivity extends AppCompatActivity {

    private ImageView imageView ;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        initializeView();
    }

    private void initializeView(){
        imageView = (ImageView) findViewById(R.id.fullscreenImage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();
        String publicId = intent.getStringExtra("publicId");
        showImage(publicId);
    }

    private void showImage(String id){
        final Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Toast.makeText(ImageActivity.this, "Error loading resource: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).build();

        Url baseUrl = MediaManager.get().url().publicId(id);
        MediaManager.get().responsiveUrl(imageView, baseUrl, FIT, new ResponsiveUrl.Callback() {
            @Override
            public void onUrlReady(Url url) {
                String uriString = url.generate();
                picasso.load(Uri.parse(uriString)).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}
