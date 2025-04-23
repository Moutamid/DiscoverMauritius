package com.moutamid.sqlapp.activities.Organizer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.AppInfo.AppInfoActivity;
import com.moutamid.sqlapp.activities.ContactUs.ContactUsActivity;
import com.moutamid.sqlapp.activities.DashboardActivity;
import com.moutamid.sqlapp.activities.Explore.ExploreActivity;
import com.moutamid.sqlapp.activities.Iteneraries.ItinerariesActivity;
import com.moutamid.sqlapp.activities.LoginActivity;
import com.moutamid.sqlapp.activities.MyTripsActivity;
import com.moutamid.sqlapp.activities.Organizer.Adapter.ViewFileAdapter;
import com.moutamid.sqlapp.activities.Organizer.Adapter.ViewImageAdapter;
import com.moutamid.sqlapp.activities.Organizer.Model.EditedText;
import com.moutamid.sqlapp.activities.Organizer.Model.FileData;
import com.moutamid.sqlapp.activities.Organizer.Model.ImageData;
import com.moutamid.sqlapp.activities.Organizer.helper.CustomDialogFragment;
import com.moutamid.sqlapp.activities.Organizer.helper.DatabaseContract;
import com.moutamid.sqlapp.activities.Organizer.helper.DatabaseHelper;
import com.moutamid.sqlapp.activities.TravelTipsActivity;
import com.moutamid.sqlapp.helper.Utils;

import java.util.ArrayList;
import java.util.List;

public class DocDetailsActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    private RelativeLayout titleBar;
    private ImageView icon;
    private TextView title;
    private ImageView menu;
    private ImageView more;
    private TextView type;
    private TextView number;
    private TextView issuesDate;
    private TextView expireDate;
    private TextView issuedBy;
    private TextView country;
    private TextView note;
    int position;
    TextView attachment;
    RelativeLayout send, edit, trash;

    LinearLayout number_layout, issue_layout, expiry_layout, issued_by_layout, country_layout, more_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_details);
        Utils.loginBtnMenuListener(this);
        titleBar = findViewById(R.id.title_bar);
        send = findViewById(R.id.send);
        edit = findViewById(R.id.edit);
        trash = findViewById(R.id.trash);
        more_layout = findViewById(R.id.more_layout);

        attachment = findViewById(R.id.attachment);
        number_layout = findViewById(R.id.number_layout);
        issue_layout = findViewById(R.id.issue_layout);
        expiry_layout = findViewById(R.id.expiry_layout);
        issued_by_layout = findViewById(R.id.issued_by_layout);
        country_layout = findViewById(R.id.country_layout);

        icon = findViewById(R.id.icon);
        title = findViewById(R.id.title);
        menu = findViewById(R.id.menu);
        more = findViewById(R.id.more);
        type = findViewById(R.id.type);
        number = findViewById(R.id.number);
        issuesDate = findViewById(R.id.issues_date);
        expireDate = findViewById(R.id.expire_date);
        issuedBy = findViewById(R.id.issued_by);
        country = findViewById(R.id.country);
        note = findViewById(R.id.note);
        databaseHelper = new DatabaseHelper(DocDetailsActivity.this);

        position = getIntent().getIntExtra("position", 0);
        List<EditedText> editedTexts = readAllEditedText();
        String documentTitle_str = editedTexts.get(position).getDocumentTitle();
        String countryDocument_str = editedTexts.get(position).getCountryDocument();
        String documentNumber_str = editedTexts.get(position).getDocumentNumber();
        String expireDate_str = editedTexts.get(position).getExpireDate();
        String issuedBy_str = editedTexts.get(position).getIssuedBy();
        String issuedDate_str = editedTexts.get(position).getIssuedDate();
        String note_str = editedTexts.get(position).getNote();
        String documentType_str = editedTexts.get(position).getDocumentType();
        if (editedTexts.get(position).getNote().length() < 1) {
            note.setVisibility(View.GONE);
        }
        if (editedTexts.get(position).getNote().length() < 1) {
            note.setVisibility(View.GONE);
        }

//        Log.d("data", editedTexts.get(position).getImages().get(0).getImageName()+ "files");
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        List<FileData> filesForEditedText = databaseHelper.getFilesForEditedText(editedTexts.get(position).getId());
        ViewFileAdapter adapter = new ViewFileAdapter(this, filesForEditedText);
        recyclerView.setAdapter(adapter);

        RecyclerView recyclerViewimages = findViewById(R.id.recyclerViewimages);
        recyclerViewimages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        List<ImageData> imageData = databaseHelper.getImagesForEditedText(editedTexts.get(position).getId());
        ViewImageAdapter imageAdapter = new ViewImageAdapter(DocDetailsActivity.this, imageData);
        recyclerViewimages.setAdapter(imageAdapter);
        if (filesForEditedText.size() < 1 && imageData.size() < 1) {
            attachment.setVisibility(View.GONE);
        }
//
        if (countryDocument_str.length() < 1) {
            country_layout.setVisibility(View.GONE);
        }
        if (documentNumber_str.length() < 1) {
            number_layout.setVisibility(View.GONE);
        }
        if (expireDate_str.length() < 1) {
            expiry_layout.setVisibility(View.GONE);
        }
        if (issuedDate_str.length() < 1) {
            issue_layout.setVisibility(View.GONE);
        }
        if (issuedBy_str.length() < 1) {
            issued_by_layout.setVisibility(View.GONE);
        }
        type.setText(documentType_str);
        title.setText(documentTitle_str);
        country.setText(countryDocument_str);
        number.setText(documentNumber_str);
        expireDate.setText(expireDate_str);
        issuesDate.setText(issuedDate_str);
        issuedBy.setText(issuedBy_str);
        note.setText(note_str);
        Stash.put("documentTitle", documentTitle_str);
        Stash.put("countryDocument", countryDocument_str);
        Stash.put("documentNumber", documentNumber_str);
        Stash.put("expireDate", expireDate_str);
        Stash.put("issuedBy", issuedBy_str);
        Stash.put("issuedDate", issuedDate_str);
        Stash.put("note", note_str);
        Stash.put("documentType", documentType_str);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogFragment dialogFragment = new CustomDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "CustomDialogFragment");
                Stash.put("position", position);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocDetailsActivity.this, EditOrganizerDetailsActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<EditedText> editedTexts = readAllEditedText();
                databaseHelper.deleteEditedText(editedTexts.get(position).getId());
                finish();
            }
        });
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

    public void showPopupMenu(View v) {
        if(more_layout.getVisibility()==View.VISIBLE)
        {
            more_layout.setVisibility(View.GONE);

        }
        else
        {
            more_layout.setVisibility(View.VISIBLE);

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
        startActivity(new Intent(DocDetailsActivity.this, ExploreActivity.class));
    }

    public void my_trips(View view) {
        startActivity(new Intent(DocDetailsActivity.this, MyTripsActivity.class));
    }

    public void iternties(View view) {
        startActivity(new Intent(DocDetailsActivity.this, ItinerariesActivity.class));

    }

    public void organier(View view) {
        startActivity(new Intent(DocDetailsActivity.this, OrganizerActivity.class));
    }

    public void contact_us(View view) {
        startActivity(new Intent(DocDetailsActivity.this, ContactUsActivity.class));
    }


    public void tips(View view) {
        startActivity(new Intent(DocDetailsActivity.this, TravelTipsActivity.class));

    }

    public void about(View view) {
        startActivity(new Intent(DocDetailsActivity.this, AppInfoActivity.class));

    }

    /*public void login(View view) {
        startActivity(new Intent(DocDetailsActivity.this, LoginActivity.class));
    }*/

    public void home(View view) {
        startActivity(new Intent(DocDetailsActivity.this, DashboardActivity.class));

    }

    @Override
    protected void onResume() {
        super.onResume();
        titleBar = findViewById(R.id.title_bar);

        attachment = findViewById(R.id.attachment);
        number_layout = findViewById(R.id.number_layout);
        issue_layout = findViewById(R.id.issue_layout);
        expiry_layout = findViewById(R.id.expiry_layout);
        issued_by_layout = findViewById(R.id.issued_by_layout);
        country_layout = findViewById(R.id.country_layout);

        icon = findViewById(R.id.icon);
        title = findViewById(R.id.title);
        menu = findViewById(R.id.menu);
        more = findViewById(R.id.more);
        type = findViewById(R.id.type);
        number = findViewById(R.id.number);
        issuesDate = findViewById(R.id.issues_date);
        expireDate = findViewById(R.id.expire_date);
        issuedBy = findViewById(R.id.issued_by);
        country = findViewById(R.id.country);
        note = findViewById(R.id.note);
        databaseHelper = new DatabaseHelper(DocDetailsActivity.this);
        position = getIntent().getIntExtra("position", 7);
        Log.d("id", position+"");

        List<EditedText> editedTexts = readAllEditedText();
        String documentTitle_str = editedTexts.get(position).getDocumentTitle();
        String countryDocument_str = editedTexts.get(position).getCountryDocument();
        String documentNumber_str = editedTexts.get(position).getDocumentNumber();
        String expireDate_str = editedTexts.get(position).getExpireDate();
        String issuedBy_str = editedTexts.get(position).getIssuedBy();
        String issuedDate_str = editedTexts.get(position).getIssuedDate();
        String note_str = editedTexts.get(position).getNote();
        String documentType_str = editedTexts.get(position).getDocumentType();
        if (editedTexts.get(position).getNote().length() < 1) {
            note.setVisibility(View.GONE);
        }
        if (editedTexts.get(position).getNote().length() < 1) {
            note.setVisibility(View.GONE);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        List<FileData> filesForEditedText = databaseHelper.getFilesForEditedText(editedTexts.get(position).getId());
        ViewFileAdapter adapter = new ViewFileAdapter(this, filesForEditedText);
        recyclerView.setAdapter(adapter);

        RecyclerView recyclerViewimages = findViewById(R.id.recyclerViewimages);
        recyclerViewimages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        List<ImageData> imageData = databaseHelper.getImagesForEditedText(editedTexts.get(position).getId());
        ViewImageAdapter imageAdapter = new ViewImageAdapter(DocDetailsActivity.this, imageData);
        recyclerViewimages.setAdapter(imageAdapter);
        if (filesForEditedText.size() < 1 && imageData.size() < 1) {
            attachment.setVisibility(View.GONE);
        }
//
        if (countryDocument_str.length() < 1) {
            country_layout.setVisibility(View.GONE);
        }
        if (documentNumber_str.length() < 1) {
            number_layout.setVisibility(View.GONE);
        }
        if (expireDate_str.length() < 1) {
            expiry_layout.setVisibility(View.GONE);
        }
        if (issuedDate_str.length() < 1) {
            issue_layout.setVisibility(View.GONE);
        }
        if (issuedBy_str.length() < 1) {
            issued_by_layout.setVisibility(View.GONE);
        }
        type.setText(documentType_str);
        title.setText(documentTitle_str);
        country.setText(countryDocument_str);
        number.setText(documentNumber_str);
        expireDate.setText(expireDate_str);
        issuesDate.setText(issuedDate_str);
        issuedBy.setText(issuedBy_str);
        note.setText(note_str);
        Stash.put("documentTitle", documentTitle_str);
        Stash.put("countryDocument", countryDocument_str);
        Stash.put("documentNumber", documentNumber_str);
        Stash.put("expireDate", expireDate_str);
        Stash.put("issuedBy", issuedBy_str);
        Stash.put("issuedDate", issuedDate_str);
        Stash.put("note", note_str);
        Stash.put("documentType", documentType_str);
        more_layout.setVisibility(View.GONE);
    }

    public void BackPress(View view) {
        onBackPressed();
    }
}