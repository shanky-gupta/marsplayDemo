package marsplay.com.views;


import android.app.Application;

import com.cloudinary.android.LogLevel;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.policy.GlobalUploadPolicy;
import com.cloudinary.android.policy.UploadPolicy;

import java.util.HashMap;
import java.util.Map;

public class MainApplication extends Application {
    static MainApplication _instance;
    public static MainApplication get() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // This can be called any time regardless of initialization.
        MediaManager.setLogLevel(LogLevel.DEBUG);

        // Mandatory - call a flavor of init. Config can be null if cloudinary_url is provided in the manifest.
        Map config = new HashMap();
        config.put("cloud_name", "shank");
        config.put("api_key", "644617911542992");
        config.put("api_secret", "oW3bQk8luOT9UlEkRsH21KoQkxY");
        MediaManager.init(this,config);

        // Optional - configure global policy.
        MediaManager.get().setGlobalUploadPolicy(
                new GlobalUploadPolicy.Builder()
                        .maxConcurrentRequests(4)
                        .networkPolicy(UploadPolicy.NetworkType.ANY)
                        .build());

        _instance = this;
    }
}
