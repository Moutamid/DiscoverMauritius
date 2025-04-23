package com.moutamid.sqlapp.activities.Organizer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fxn.stash.Stash;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.AppInfo.AppInfoActivity;
import com.moutamid.sqlapp.activities.ContactUs.ContactUsActivity;
import com.moutamid.sqlapp.activities.DashboardActivity;
import com.moutamid.sqlapp.activities.Explore.ExploreActivity;
import com.moutamid.sqlapp.activities.Iteneraries.ItinerariesActivity;
import com.moutamid.sqlapp.activities.LoginActivity;
import com.moutamid.sqlapp.activities.MyTripsActivity;
import com.moutamid.sqlapp.activities.NetworkTask;
import com.moutamid.sqlapp.activities.Organizer.Model.EditedText;
import com.moutamid.sqlapp.activities.Organizer.Model.ImageData;
import com.moutamid.sqlapp.activities.Organizer.helper.DatabaseContract;
import com.moutamid.sqlapp.activities.Organizer.helper.DatabaseHelper;
import com.moutamid.sqlapp.activities.TravelTipsActivity;
import com.moutamid.sqlapp.helper.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SendActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1;
    EditText name, email, message;
    DatabaseHelper databaseHelper;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        Utils.loginBtnMenuListener(this);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        message = findViewById(R.id.message);
        ActivityCompat.requestPermissions(SendActivity.this,
                permissions(),
                1);
        databaseHelper = new DatabaseHelper(SendActivity.this);
        List<EditedText> editedTexts = readAllEditedText();

        position = getIntent().getIntExtra("position", 0);
        List<String> imagePaths = new ArrayList<>();

        List<ImageData> imageData = databaseHelper.getImagesForEditedText(editedTexts.get(position).getId());
        for (int i = 0; i < imageData.size(); i++) {
            try {


//
                String pathFromUri = getPathFromUri(imageData.get(i).getImageUri());
                File file = new File(pathFromUri);
                String realPath = file.getAbsolutePath();


//               String realPath;
//               // SDK < API11
//               if (Build.VERSION.SDK_INT < 11)
//                   realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, imageData.get(i).getImageUri());
//
//                   // SDK >= 11 && SDK < 19
//               else if (Build.VERSION.SDK_INT < 19)
//                   realPath = RealPathUtil.getRealPathFromURI_API11to18(this, imageData.get(i).getImageUri());
//
//                   // SDK > 19 (Android 4.4)
//               else
//                   realPath = RealPathUtil.getRealPathFromURI_API19(this, imageData.get(i).getImageUri());
                imagePaths.add(realPath);
                Log.e("SendEmailTask", "sending image " + realPath + "  ");

            } catch (Exception e) {
                Log.e("SendEmailTask", "sending image error" + e.getMessage() + "  ");

            }
        }

        Log.e("SendEmailTask", "sending image" + imagePaths.size() + "  ");

        for (int i = 0; i < imagePaths.size(); i++) {
            Log.e("SendEmailTask", "sending image" + imagePaths.get(i).toString() + "  ");

        }

    }

    public void BackPress(View view) {
        onBackPressed();
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = SendActivity.this.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            return imagePath;
        }
        return null;
    }

    public List<EditedText> readAllEditedText() {
        List<EditedText> editedTextList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = {
                DatabaseContract.EditedTextEntry._ID,
                DatabaseContract.EditedTextEntry.COLUMN_NAME_DOCUMENT_TYPE,
                DatabaseContract.EditedTextEntry.COLUMN_NAME_DOCUMENT_TITLE,
                DatabaseContract.EditedTextEntry.COLUMN_NAME_DOCUMENT_NUMBER,
                DatabaseContract.EditedTextEntry.COLUMN_NAME_COUNTRY_DOCUMENT,
                DatabaseContract.EditedTextEntry.COLUMN_NAME_ISSUED_BY,
                DatabaseContract.EditedTextEntry.COLUMN_NAME_ISSUED_DATE,
                DatabaseContract.EditedTextEntry.COLUMN_NAME_EXPIRE_DATE,
                DatabaseContract.EditedTextEntry.COLUMN_NAME_NOTE
        };

        Cursor cursor = db.query(
                DatabaseContract.EditedTextEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        try {
            while (cursor.moveToNext()) {
                EditedText editedText = new EditedText();
                editedText.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.EditedTextEntry._ID)));
                editedText.setDocumentType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.EditedTextEntry.COLUMN_NAME_DOCUMENT_TYPE)));
                editedText.setDocumentTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.EditedTextEntry.COLUMN_NAME_DOCUMENT_TITLE)));
                editedText.setDocumentNumber(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.EditedTextEntry.COLUMN_NAME_DOCUMENT_NUMBER)));
                editedText.setCountryDocument(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.EditedTextEntry.COLUMN_NAME_COUNTRY_DOCUMENT)));
                editedText.setIssuedBy(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.EditedTextEntry.COLUMN_NAME_ISSUED_BY)));
                editedText.setIssuedDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.EditedTextEntry.COLUMN_NAME_ISSUED_DATE)));
                editedText.setExpireDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.EditedTextEntry.COLUMN_NAME_EXPIRE_DATE)));
                editedText.setNote(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.EditedTextEntry.COLUMN_NAME_NOTE)));

                // Add the edited text to the list
                editedTextList.add(editedText);
            }
        } finally {
            cursor.close();
        }

        return editedTextList;
    }

    public void send(View view) {
        if (name.getText().toString().isEmpty()) {
            name.setError("required");
            return;
        }
        if (email.getText().toString().isEmpty()) {
            email.setError("required");
            return;
        }
        if (message.getText().toString().isEmpty()) {
            message.setError("required");
            return;
        }
        databaseHelper = new DatabaseHelper(SendActivity.this);
        List<EditedText> editedTexts = readAllEditedText();

        position = getIntent().getIntExtra("position", 0);
        List<String> imagePaths = new ArrayList<>();

        List<ImageData> imageData = databaseHelper.getImagesForEditedText(editedTexts.get(position).getId());
        for (int i = 0; i < imageData.size(); i++) {
            String pathFromUri = getPathFromUri(imageData.get(i).getImageUri());
            File file = new File(pathFromUri);
            String absolutePath = file.getAbsolutePath();
            imagePaths.add(absolutePath);
        }
        Log.e("SendEmailTask", "sending image" + imagePaths.size() + "  ");

        for (int i = 0; i < imagePaths.size(); i++) {
            Log.e("SendEmailTask", "sending image" + imagePaths.get(i).toString() + "  ");

        }
        Stash.put("name", name.getText().toString());
        Stash.put("email", email.getText().toString());
        Stash.put("message", message.getText().toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_IMAGES},
                        REQUEST_EXTERNAL_STORAGE_PERMISSION);
            } else {
                NetworkTask networkTask = new NetworkTask(SendActivity.this);
                networkTask.execute(imagePaths);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE_PERMISSION);
            } else {
                NetworkTask networkTask = new NetworkTask(SendActivity.this);
                networkTask.execute(imagePaths);
            }
        }


    }


    public void menu(View view) {
        LinearLayout more_layout;
        more_layout = findViewById(R.id.more_layout);
        ImageView menu = findViewById(R.id.menu);
        ImageView close = findViewById(R.id.closebtn);
        menu.setVisibility(View.GONE);
        more_layout.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.setVisibility(View.VISIBLE);
                more_layout.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
            }
        });
    }

    public void explore(View view) {
        startActivity(new Intent(SendActivity.this, ExploreActivity.class));
    }

    public void my_trips(View view) {
        startActivity(new Intent(SendActivity.this, MyTripsActivity.class));
    }

    public void iternties(View view) {
        startActivity(new Intent(SendActivity.this, ItinerariesActivity.class));

    }

    public void organier(View view) {
        startActivity(new Intent(SendActivity.this, OrganizerActivity.class));
    }

    public void contact_us(View view) {
        startActivity(new Intent(SendActivity.this, ContactUsActivity.class));
    }


    public void tips(View view) {
        startActivity(new Intent(SendActivity.this, TravelTipsActivity.class));

    }

    public void about(View view) {
        startActivity(new Intent(SendActivity.this, AppInfoActivity.class));

    }

    /*public void login(View view) {
        startActivity(new Intent(SendActivity.this, LoginActivity.class));
    }*/

    public void home(View view) {
        startActivity(new Intent(SendActivity.this, DashboardActivity.class));

    }

    public static String[] storage_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storage_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storage_permissions_33;
        } else {
            p = storage_permissions;
        }
        return p;
    }
}