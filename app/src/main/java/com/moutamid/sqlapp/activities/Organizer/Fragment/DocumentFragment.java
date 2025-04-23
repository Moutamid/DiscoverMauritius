package com.moutamid.sqlapp.activities.Organizer.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Organizer.Adapter.DocumentTitleAdapter;
import com.moutamid.sqlapp.activities.Organizer.CreateActivity;
import com.moutamid.sqlapp.activities.Organizer.Model.EditedText;
import com.moutamid.sqlapp.activities.Organizer.helper.DatabaseContract;
import com.moutamid.sqlapp.activities.Organizer.helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DocumentFragment extends Fragment {
    ImageView add_icon;
    private DatabaseHelper dbHelper;
    RecyclerView recyclerView;
    RelativeLayout no_text;
    LinearLayout search_layout;
    List<String> filteredDocumentTitles;
    EditText search_edit;
    List<EditedText> editedTexts;
    DocumentTitleAdapter adapter;
    List<String> documentTitles = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_document, container, false);
        search_layout = view.findViewById(R.id.search_layout);
        add_icon = view.findViewById(R.id.add_icon);
        no_text = view.findViewById(R.id.no_text);
        recyclerView = view.findViewById(R.id.recyclerView);
        search_edit = view.findViewById(R.id.search_edit);
        dbHelper = new DatabaseHelper(getContext());
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter document titles based on user input
                filterDocumentTitles(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CreateActivity.class));
            }
        });
        editedTexts = readAllEditedText();
        if (editedTexts.size() > 0) {
            no_text.setVisibility(View.GONE);
            search_layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            for (EditedText editedText : editedTexts) {
                documentTitles.add(editedText.getDocumentType());
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new DocumentTitleAdapter(getContext(), documentTitles);
            recyclerView.setAdapter(adapter);
        } else {
            no_text.setVisibility(View.VISIBLE);
            search_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

        }


        return view;
    }

    public List<EditedText> readAllEditedText() {
        List<EditedText> editedTextList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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

    @Override
    public void onResume() {
        super.onResume();
        List<EditedText> editedTexts = readAllEditedText();
        if (editedTexts.size() > 0) {
            no_text.setVisibility(View.GONE);
            search_layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            List<String> documentTitles = new ArrayList<>();
            for (EditedText editedText : editedTexts) {
                documentTitles.add(editedText.getDocumentType());
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            DocumentTitleAdapter adapter = new DocumentTitleAdapter(getContext(), documentTitles);
            recyclerView.setAdapter(adapter);
        } else {
            no_text.setVisibility(View.VISIBLE);
            search_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

        }
    }
    private void filterDocumentTitles(String query) {
        filteredDocumentTitles = new ArrayList<>();
        for (String title : documentTitles) {
            if (title.toLowerCase().contains(query.toLowerCase()))
            {
                filteredDocumentTitles.add(title);
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DocumentTitleAdapter(getContext(), filteredDocumentTitles);
        recyclerView.setAdapter(adapter);
    }
}