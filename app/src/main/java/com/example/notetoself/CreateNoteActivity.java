package com.example.notetoself;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;

public class CreateNoteActivity extends AppCompatActivity {

    TextInputEditText title, text;
    MaterialButton save, cancel;
    SQLiteDatabase database;
    FloatingActionButton fab;
    ImageView image;
    String path = null;
    boolean assigned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        title = findViewById(R.id.title_text);
        text = findViewById(R.id.body_text);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        fab = findViewById(R.id.fab);
        image = findViewById(R.id.image);

        DBHelper helper = new DBHelper(this);
        database = helper.getWritableDatabase();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString().trim().length() == 0 || text.getText().toString().trim().length() == 0) {
                    Toast.makeText(CreateNoteActivity.this, "Enter complete information", Toast.LENGTH_LONG).show();
                } else {

                    String title_text = title.getText().toString().trim();
                    String text_body = text.getText().toString().trim();

                    ContentValues cv = new ContentValues();
                    cv.put(Notes.COLUMN_TITLE, title_text);
                    cv.put(Notes.COLUMN_TEXT, text_body);
                    cv.put(Notes.COLUMN_IMAGE, path);
                    database.insert(Notes.TABLE_NAME, null, cv);
                    finish();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!assigned)
                    CropImage.activity().start(CreateNoteActivity.this);
                else {
                    assigned = false;
                    path = null;
                    image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.placeholder));
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_add_24));

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                assert result != null;
                Uri uri = result.getUri();
                path = String.valueOf(uri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    image.setImageBitmap(bitmap);
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_close_24));
                    assigned = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void finish() {
        setResult(345);
        super.finish();
    }
}