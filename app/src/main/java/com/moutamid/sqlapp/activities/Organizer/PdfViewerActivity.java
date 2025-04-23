package com.moutamid.sqlapp.activities.Organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Organizer.helper.Constants;

import java.io.File;

public class PdfViewerActivity extends AppCompatActivity {
    PDFView pdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        String path = Stash.getString(Constants.PATH, "");
        pdf=findViewById(R.id.pdf);

        if (!path.isEmpty()){
            loadPdf(path);
        }

    }

    private static final String TAG = "PdfActivity";
    private void loadPdf(String path) {
        Log.d(TAG, "loadPdf: " + path);
//        Uri uri = Uri.parse(path);
        try {
//            ContentResolver contentResolver = getContentResolver();
//            InputStream inputStream = contentResolver.openInputStream(uri);
            pdf.fromFile(new File(path))
                    .defaultPage(0)
                    .onPageChange((page, pageCount) -> {
                    })
                    .enableAnnotationRendering(true)
                    .onError(t -> {

                        t.printStackTrace();
                        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .scrollHandle(new DefaultScrollHandle(this))
                    .spacing(10)
                    .load();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}