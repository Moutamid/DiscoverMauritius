package com.moutamid.sqlapp.activities.Organizer;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.textfield.TextInputLayout;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.AppInfo.AppInfoActivity;
import com.moutamid.sqlapp.activities.ContactUs.ContactUsActivity;
import com.moutamid.sqlapp.activities.DashboardActivity;
import com.moutamid.sqlapp.activities.Explore.ExploreActivity;
import com.moutamid.sqlapp.activities.Iteneraries.ItinerariesActivity;
import com.moutamid.sqlapp.activities.LoginActivity;
import com.moutamid.sqlapp.activities.MyTripsActivity;
import com.moutamid.sqlapp.activities.Organizer.Adapter.FileAdapter;
import com.moutamid.sqlapp.activities.Organizer.Adapter.ImageAdapter;
import com.moutamid.sqlapp.activities.Organizer.Model.FileData;
import com.moutamid.sqlapp.activities.Organizer.Model.ImageData;
import com.moutamid.sqlapp.activities.Organizer.helper.DatabaseHelper;
import com.moutamid.sqlapp.activities.TravelTipsActivity;
import com.moutamid.sqlapp.helper.Utils;
import com.nareshchocha.filepickerlibrary.models.DocumentFilePickerConfig;
import com.nareshchocha.filepickerlibrary.ui.FilePicker;
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreateActivity extends AppCompatActivity {
    EditText documentTypeEditText, document_title, document_number, country_document,
            issued_by, issued_date, expire_date, note, format, format_expiry;
    TextView upload;
    ImageView close;
    TextView save_btn, document_title_ok, number_on_document_ok, country_document_ok, issued_by_ok, date_ok, month_ok;
    RelativeLayout upload_layout;
    LinearLayout documentTypeLayout, document_title_lyt, number_on_document_lyt, country_document_lyt, date_lyt, issued_by_lyt, month_lyt;
    private static final int PICK_IMAGES_REQUEST = 1;
    private List<ImageData> selectedImages;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private static final int PICK_FILES_REQUEST = 2;
    private List<FileData> selectedFiles;
    private RecyclerView file_recyclerView;
    private FileAdapter fileAdapter;
    private DatabaseHelper dbHelper;
    ImageView data_image;
    PDFView pdfView;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    String[] permissions13 = new String[]{
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        dbHelper = new DatabaseHelper(this);
        pdfView = findViewById(R.id.pdfView);
        Utils.loginBtnMenuListener(this);

        format = findViewById(R.id.format);
        data_image = findViewById(R.id.data_image);
        format_expiry = findViewById(R.id.format_expiry);
        close = findViewById(R.id.close);
        save_btn = findViewById(R.id.save_btn);
        upload = findViewById(R.id.upload);
        upload_layout = findViewById(R.id.upload_layout);
        country_document_ok = findViewById(R.id.country_document_ok);
        issued_by_ok = findViewById(R.id.issued_by_ok);
        date_ok = findViewById(R.id.date_ok);

        issued_by_lyt = findViewById(R.id.issued_by_lyt);
        date_lyt = findViewById(R.id.date_lyt);
        month_lyt = findViewById(R.id.month_lyt);
        month_ok = findViewById(R.id.month_ok);
        country_document_lyt = findViewById(R.id.country_document_lyt);
        number_on_document_ok = findViewById(R.id.number_on_document_ok);

        number_on_document_lyt = findViewById(R.id.number_on_document_lyt);
        documentTypeEditText = findViewById(R.id.document_type);
        document_title = findViewById(R.id.document_title);
        document_number = findViewById(R.id.document_number);
        country_document = findViewById(R.id.country_document);
        issued_by = findViewById(R.id.issued_by);
        issued_date = findViewById(R.id.issued_date);
        expire_date = findViewById(R.id.expire_date);
        document_title_ok = findViewById(R.id.document_title_ok);
        note = findViewById(R.id.note);
        documentTypeLayout = findViewById(R.id.document_type_lyt);
        document_title_lyt = findViewById(R.id.document_title_lyt);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        file_recyclerView = findViewById(R.id.recyclerViewfiles);
        file_recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        selectedFiles = new ArrayList<>();
        fileAdapter = new FileAdapter(selectedFiles);
        file_recyclerView.setAdapter(fileAdapter);
        selectedImages = new ArrayList<>();
        adapter = new ImageAdapter(selectedImages);
        recyclerView.setAdapter(adapter);
        for (int i = 0; i < documentTypeLayout.getChildCount(); i++) {
            View view = documentTypeLayout.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        documentTypeEditText.setText(textView.getText());
                        documentTypeLayout.setVisibility(View.GONE);

                    }
                });
            }
        }
//        date_format(issued_date, format);
//        date_format(expire_date, format_expiry);
        document_title_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                document_title_lyt.setVisibility(View.GONE);
            }
        });
        number_on_document_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_on_document_lyt.setVisibility(View.GONE);
            }
        });
        country_document_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                country_document_lyt.setVisibility(View.GONE);
            }
        });
        issued_by_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issued_by_lyt.setVisibility(View.GONE);
            }
        });
        date_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date_lyt.setVisibility(View.GONE);
            }
        });
        month_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month_lyt.setVisibility(View.GONE);
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (document_title.getText().toString().isEmpty()) {
                    document_title_lyt.setVisibility(View.VISIBLE);
                } else if (!isValidDate(issued_date.getText().toString())) {
                } else if (!isValidDateExpiry(expire_date.getText().toString())) {
                } else {
                    saveData(); // Call your saveData() function if all validations pass
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_layout.setVisibility(View.VISIBLE);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_layout.setVisibility(View.GONE);
            }
        });

        documentTypeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentTypeLayout.setVisibility(View.VISIBLE);
            }
        });
        /*applyStylesToTextInputLayoutHint(documentTypeEditText, "");/
        applyStylesToTextInputLayoutHint(document_title, "");
        applyStylesToTextInputLayoutHint(document_number, "");
        applyStylesToTextInputLayoutHint(country_document, "");
        applyStylesToTextInputLayoutHint(issued_by, "");
        applyStylesToTextInputLayoutHint(issued_date, "");
        applyStylesToTextInputLayoutHint(expire_date, "");
        applyStylesToTextInputLayoutHint(note, "");*/
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_layout.setVisibility(View.VISIBLE);

            }
        });
    }

    private void applyStylesToTextInputLayoutHint(EditText editText, String hint) {
        TextInputLayout textInputLayout = (TextInputLayout) editText.getParent().getParent();
        SpannableString spannableString = new SpannableString(hint);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, hint.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textInputLayout.setHint(spannableString);

    }

    public void saveData() {
        String docType = documentTypeEditText.getText().toString();
        String docTitle = document_title.getText().toString();
        String docNumber = document_number.getText().toString();
        String countryDoc = country_document.getText().toString();
        String issuedBy = issued_by.getText().toString();
        String issuedDate = issued_date.getText().toString();
        String expireDate = expire_date.getText().toString();
        String noteText = note.getText().toString();

        long editedTextId = dbHelper.insertOrUpdateEditedText(docType, docTitle, docNumber, countryDoc, issuedBy, issuedDate, expireDate, noteText);

        if (editedTextId != -1) {
            // Insert images associated with edited text
            for (ImageData imageData : selectedImages) {
                dbHelper.insertImageForEditedText(editedTextId, imageData.getImageName(), imageData.getImageSize(), String.valueOf(imageData.getImageUri()));
            }

            for (FileData fileData : selectedFiles) {
                dbHelper.insertFileForEditedText(editedTextId, fileData.getBitmap(), fileData.getFileName(), fileData.getFileSize(), (fileData.getFileUri())); // Pass file path here
            }
            Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }


    public void upload(View view) {
        upload_layout.setVisibility(View.VISIBLE);
    }

    public void BackPress(View view) {
        onBackPressed();
    }

    public void openGallery(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (above13Check()) {
                shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES);
                shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_VIDEO);
                ActivityCompat.requestPermissions(CreateActivity.this, permissions13, 2);
            } else {
                gallery();
            }
        } else {
            if (below13Check()) {
                shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(CreateActivity.this, permissions, 2);
            } else {
                gallery();
            }
        }
    }

    public void gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGES_REQUEST);

    }

    public void openFileManager(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (above13Check()) {
                shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES);
                shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_VIDEO);
                ActivityCompat.requestPermissions(CreateActivity.this, permissions13, 2);
            } else {
                open();
            }
        } else {
            if (below13Check()) {
                shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(CreateActivity.this, permissions, 2);
            } else {
                open();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {
            upload_layout.setVisibility(View.GONE);
            if (data != null) {
                Uri imageUri = data.getData();
                String imageName;
                if (getImageName(imageUri) != null) {
                    imageName = getImageName(imageUri);

                } else {
                    imageName = "image.jpg";
                }
                long imageSize = getImageSize(imageUri);
                selectedImages.add(new ImageData(imageUri, imageName, imageSize));
            }
            adapter.notifyDataSetChanged();
        }

    }


    private String getImageName(Uri imageUri) {
        String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(imageUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            String name = cursor.getString(index);
            cursor.close();
            return name;
        }
        return "";
    }

    private long getImageSize(Uri imageUri) {
        String[] projection = {MediaStore.Images.Media.SIZE};
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(imageUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            long size = cursor.getLong(index);
            cursor.close();
            return size;
        }
        return 0;
    }


    public void date_format(EditText issued_date, EditText format) {
        issued_date.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        issued_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    format.setVisibility(View.VISIBLE);
                } else {
                    format.setVisibility(View.GONE);

                }
            }
        });
        issued_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    String firstPart = s.toString();
                    String secondPart = "MM-DD-YYYY";
                    SpannableString spannableString = new SpannableString(firstPart + secondPart);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, firstPart.length(), 0);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), firstPart.length(), firstPart.length() + secondPart.length(), 0);
                    format.setText(spannableString);
                }
                if (s.length() == 1) {
                    String firstPart = s.toString();
                    String secondPart = "M-DD-YYYY";
                    SpannableString spannableString = new SpannableString(firstPart + secondPart);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, firstPart.length(), 0);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), firstPart.length(), firstPart.length() + secondPart.length(), 0);
                    format.setText(spannableString);
                }
                if (s.length() == 2) {
                    String firstPart = s.toString();
                    String secondPart = "-DD-YYYY";
                    SpannableString spannableString = new SpannableString(firstPart + secondPart);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, firstPart.length(), 0);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), firstPart.length(), firstPart.length() + secondPart.length(), 0);
                    format.setText(spannableString);
                }
                if (s.length() == 3 && s.charAt(2) != '-') {
                    issued_date.setText(new StringBuilder(s).insert(2, "-").toString());
                    String firstPart = issued_date.getText().toString();
                    String secondPart = "D-YYYY";
                    SpannableString spannableString = new SpannableString(firstPart + secondPart);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, firstPart.length(), 0);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), firstPart.length(), firstPart.length() + secondPart.length(), 0);
                    format.setText(spannableString);
                    issued_date.setSelection(4);
                }
                if (s.length() == 3) {
                    String firstPart = issued_date.getText().toString();
                    String secondPart = "D-YYYY";
                    SpannableString spannableString = new SpannableString(firstPart + secondPart);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, firstPart.length(), 0);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), firstPart.length(), firstPart.length() + secondPart.length(), 0);
                    format.setText(spannableString);
                }
                if (s.length() == 4) {
                    String firstPart = s.toString();
                    String secondPart = "D-YYYY";
                    SpannableString spannableString = new SpannableString(firstPart + secondPart);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, firstPart.length(), 0);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), firstPart.length(), firstPart.length() + secondPart.length(), 0);
                    format.setText(spannableString);
                }

                if (s.length() == 5) {
                    String firstPart = s.toString();
                    String secondPart = "-YYYY";
                    SpannableString spannableString = new SpannableString(firstPart + secondPart);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, firstPart.length(), 0);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), firstPart.length(), firstPart.length() + secondPart.length(), 0);
                    format.setText(spannableString);
                }
                if (s.length() == 6 && s.charAt(5) != '-') {

                    issued_date.setText(new StringBuilder(s).insert(5, "-").toString());
                    String firstPart = issued_date.getText().toString();
                    String secondPart = "YYY";
                    SpannableString spannableString = new SpannableString(firstPart + secondPart);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, firstPart.length(), 0);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), firstPart.length(), firstPart.length() + secondPart.length(), 0);
                    format.setText(spannableString);
                    issued_date.setSelection(7);
                }
                if (s.length() == 6) {
                    String firstPart = issued_date.getText().toString();
                    String secondPart = "YYY";
                    SpannableString spannableString = new SpannableString(firstPart + secondPart);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, firstPart.length(), 0);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), firstPart.length(), firstPart.length() + secondPart.length(), 0);
                    format.setText(spannableString);
                }
                if (s.length() == 7) {
                    String firstPart = s.toString();
                    String secondPart = "YYY";
                    SpannableString spannableString = new SpannableString(firstPart + secondPart);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, firstPart.length(), 0);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), firstPart.length(), firstPart.length() + secondPart.length(), 0);
                    format.setText(spannableString);
                }
                if (s.length() == 8) {
                    String firstPart = s.toString();
                    String secondPart = "YY";
                    SpannableString spannableString = new SpannableString(firstPart + secondPart);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, firstPart.length(), 0);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), firstPart.length(), firstPart.length() + secondPart.length(), 0);
                    format.setText(spannableString);
                }
                if (s.length() == 9) {
                    String firstPart = s.toString();
                    String secondPart = "Y";
                    SpannableString spannableString = new SpannableString(firstPart + secondPart);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, firstPart.length(), 0);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), firstPart.length(), firstPart.length() + secondPart.length(), 0);
                    format.setText(spannableString);
                }
                if (s.length() == 10) {
                    String firstPart = s.toString();
                    String secondPart = "Y";
                    SpannableString spannableString = new SpannableString(firstPart + secondPart);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, firstPart.length(), 0);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), firstPart.length(), firstPart.length() + secondPart.length(), 0);
                    format.setText(spannableString);
                }


            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private Bitmap generateImageFromPdf(Uri pdfUri) {
        Bitmap bitmap = null;
        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(new File(pdfUri.getPath()), ParcelFileDescriptor.MODE_READ_ONLY));
            final int pageCount = renderer.getPageCount();
            if (pageCount > 0) {
                PdfRenderer.Page page = renderer.openPage(0);
                int width = (int) (page.getWidth());
                int height = (int) (page.getHeight());
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                page.close();
                renderer.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }

    /* public Bitmap generateImageFromPdf(Uri pdfUri) {
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(this);
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            ParcelFileDescriptor fd = getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
//            saveImage(bmp);
            pdfiumCore.closeDocument(pdfDocument); // important!
            return bmp;
        } catch (Exception e) {
            //todo with exception
        }
        return null;

    }*/

    private boolean isValidDate(String inputDate) {
        if (!TextUtils.isEmpty(inputDate)) {
            String[] parts = inputDate.split("-");
            if (parts.length == 3) {
                int month, day, year;

                try {
                    month = Integer.parseInt(parts[0]);
                    day = Integer.parseInt(parts[1]);
                    year = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    // If any part is not an integer, show an error message
                    Toast.makeText(CreateActivity.this, "Invalid date format of issued date", Toast.LENGTH_SHORT).show();
                    return false;
                }

                boolean isValid = true;

                if (month < 1 || month > 12) {
                    // Month is invalid
                    Toast.makeText(CreateActivity.this, "Invalid month in issued date", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }
                if (day < 1 || day > 31) {
                    // Day is invalid
                    Toast.makeText(CreateActivity.this, "Invalid day in issued date", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }
                if (year < 1900 || year > 2100) {
                    // Year is invalid
                    Toast.makeText(CreateActivity.this, "Invalid year in issued date", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }

                return isValid;
            } else {
                // Date format is incomplete
                Toast.makeText(CreateActivity.this, "Please enter the issued date in the format: MM-DD-YYYY", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true; // Date is valid or empty
    }

    private boolean isValidDateExpiry(String inputDate) {
        if (!TextUtils.isEmpty(inputDate)) {
            String[] parts = inputDate.split("-");
            if (parts.length == 3) {
                int month, day, year;

                try {
                    month = Integer.parseInt(parts[0]);
                    day = Integer.parseInt(parts[1]);
                    year = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    // If any part is not an integer, show an error message
                    Toast.makeText(CreateActivity.this, "Invalid date format of expiry date", Toast.LENGTH_SHORT).show();
                    return false;
                }

                boolean isValid = true;

                if (month < 1 || month > 12) {
                    // Month is invalid
                    Toast.makeText(CreateActivity.this, "Invalid month in expiry date", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }
                if (day < 1 || day > 31) {
                    // Day is invalid
                    Toast.makeText(CreateActivity.this, "Invalid day in expiry date", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }
                if (year < 1900 || year > 2100) {
                    // Year is invalid
                    Toast.makeText(CreateActivity.this, "Invalid year in expiry date", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }

                return isValid;
            } else {
                // Date format is incomplete
                Toast.makeText(CreateActivity.this, "Please enter the expiry date in the format: MM-DD-YYYY", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true; // Date is valid or empty
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean above13Check() {
        return ContextCompat.checkSelfPermission(CreateActivity.this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(CreateActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(CreateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(CreateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    private boolean below13Check() {
        return ContextCompat.checkSelfPermission(CreateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(CreateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    private void open() {
        List<String> mMimeTypesList = new ArrayList<>();
        mMimeTypesList.add("application/pdf");
        launcher.launch(new FilePicker.Builder(this)
                .setPopUpConfig(null)
                .addPickDocumentFile(new DocumentFilePickerConfig(
                        null, // DrawableRes Id
                        null,// Title for pop item
                        true, // set Multiple pick file
                        null, // max files working only in android latest version
                        mMimeTypesList, // added Multiple MimeTypes
                        null,  // set Permission ask Title
                        null, // set Permission ask Message
                        null, // set Permission setting Title
                        null // set Permission setting Messag
                ))
                .build());
    }

    private static final String TAG = "CreateActivity";

    private ActivityResultLauncher launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                upload_layout.setVisibility(View.GONE);
                                Uri uri = result.getData().getData();
                                String fileName = getFileName(uri);
                                long fileSize = getFileSize(uri);
                                Bitmap bitmap = generateImageFromPdf(uri);
                                String filePath = result.getData().getStringExtra(Const.BundleExtras.FILE_PATH);
                                Log.d(TAG, "File Name: " + fileName);
                                Log.d(TAG, "File Size: " + fileSize + " bytes");
                                Log.d(TAG, "filePath: " + filePath);
                                selectedFiles.add(new FileData(fileName, fileSize, filePath, bitmap));
                            }

                            fileAdapter.notifyDataSetChanged();

                        }
                    });


    private String getFileName(Uri uri) {
        String fileName = null;
        String scheme = uri.getScheme();
        if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index != -1) {
                    fileName = cursor.getString(index);
                }
                cursor.close();
            }
        } else if (scheme.equals(ContentResolver.SCHEME_FILE)) {
            fileName = new File(uri.getPath()).getName();
        }
        return fileName;
    }

    private long getFileSize(Uri uri) {
        long fileSize = 0;
        String scheme = uri.getScheme();
        if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (index != -1) {
                    fileSize = cursor.getLong(index);
                }
                cursor.close();
            }
        } else if (scheme.equals(ContentResolver.SCHEME_FILE)) {
            File file = new File(uri.getPath());
            fileSize = file.length();
        }
        return fileSize;
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
        startActivity(new Intent(CreateActivity.this, ExploreActivity.class));
    }

    public void my_trips(View view) {
        startActivity(new Intent(CreateActivity.this, MyTripsActivity.class));
    }

    public void iternties(View view) {
        startActivity(new Intent(CreateActivity.this, ItinerariesActivity.class));

    }

    public void organier(View view) {
        startActivity(new Intent(CreateActivity.this, OrganizerActivity.class));
    }

    public void contact_us(View view) {
        startActivity(new Intent(CreateActivity.this, ContactUsActivity.class));
    }


    public void tips(View view) {
        startActivity(new Intent(CreateActivity.this, TravelTipsActivity.class));

    }

    public void about(View view) {
        startActivity(new Intent(CreateActivity.this, AppInfoActivity.class));

    }

   /* public void login(View view) {
        startActivity(new Intent(CreateActivity.this, LoginActivity.class));
    }*/

    public void home(View view) {
        startActivity(new Intent(CreateActivity.this, DashboardActivity.class));

    }
}








