package marsplay.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import marsplay.com.model.Resource;

/**
 * Created by shashank on 07/10/2018.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    Context DATABASE_CONTEXT;
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "image.sqlite";

    private static String KEY_ID = "primary_id";
    private static String KEY_PUBLIC_ID = "publicId";
    private static String KEY_REQUEST_ID = "requestId";
    private static String KEY_LOCAL_URI = "uri";


    private static String TABLE = "images";
    private static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PUBLIC_ID + " TEXT, "
            + KEY_REQUEST_ID + " TEXT, " + KEY_LOCAL_URI + " TEXT" + ")";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.DATABASE_CONTEXT = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    @Override
    public synchronized void close() {
        super.close();
    }


    public long insertResourceRequest(Resource resource) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PUBLIC_ID, resource.getCloudinaryPublicId());
        values.put(KEY_REQUEST_ID, resource.getRequestId());
        values.put(KEY_LOCAL_URI, resource.getLocalUri());

        // insert row
        long id = db.insert(TABLE, null, values);
        return id;
    }

    public List<Resource> getAllResourceRequest() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE;
        Cursor c = db.rawQuery(selectQuery, null);

        List<Resource> travelRequestBeanList = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Resource travelRequestBean = new Resource();
                travelRequestBean.setCloudinaryPublicId(c.getString((c.getColumnIndex(KEY_PUBLIC_ID))));
                travelRequestBean.setRequestId(c.getString((c.getColumnIndex(KEY_REQUEST_ID))));
                travelRequestBean.setLocalUri(c.getString((c.getColumnIndex(KEY_LOCAL_URI))));
                travelRequestBeanList.add(travelRequestBean);
            } while (c.moveToNext());
        }
        return travelRequestBeanList;
    }
}
