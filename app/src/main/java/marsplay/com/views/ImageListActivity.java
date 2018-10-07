package marsplay.com.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import marsplay.com.adapter.ImageListAdapter;
import marsplay.com.model.Resource;
import marsplay.com.database.DataBaseHandler;

public class ImageListActivity extends AppCompatActivity implements ImageListAdapter.ImageClickedListener{

    RecyclerView mRecyclerView;
    ImageListAdapter imageListAdapter;

    private LinearLayoutManager linearLayoutManager;
    DataBaseHandler dataBaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        init();


        List<Resource> allResourceRequest = dataBaseHandler.getAllResourceRequest();

        imageListAdapter = new ImageListAdapter(allResourceRequest, this,this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setAdapter(imageListAdapter);
    }

    private void init() {
        dataBaseHandler = new DataBaseHandler(MainApplication.get());
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    public void onImageClicked(Resource resource) {
        Intent intent = new Intent(ImageListActivity.this,ImageActivity.class);
        intent.putExtra("publicId",resource.getCloudinaryPublicId());
        startActivity(intent);
    }
}
