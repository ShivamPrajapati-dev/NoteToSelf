package com.example.notetoself;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteDetailsActivity extends AppCompatActivity {

    TextView title, body;
    ImageView image;
    MaterialButton delete;
    SQLiteDatabase database;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        title = findViewById(R.id.title);
        body = findViewById(R.id.body);
        image = findViewById(R.id.image);
        delete = findViewById(R.id.delete);

        DBHelper dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        String title_text = intent.getStringExtra("title");
        String body_text = intent.getStringExtra("body");
        String path = intent.getStringExtra("image");

        long id = intent.getLongExtra("id", 0);

        title.setText(title_text);
        body.setText(body_text);

        Bitmap bitmap = null;
        try {
            if (path != null)
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(path));
            if (bitmap != null)
                image.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(NoteDetailsActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure to delete")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                database.delete(Notes.TABLE_NAME, Notes._ID + "=" + id, null);
                                finish();
                            }
                        }).setNegativeButton("No", null)
                        .show();

            }
        });

        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        image.setColorFilter(new ColorMatrixColorFilter(matrix));
                        break;
                    case MotionEvent.ACTION_UP:
                        image.clearColorFilter();
                        break;
                }

                return true;
            }
        });

    }

    @Override
    public void finish() {
        setResult(789);
        super.finish();

    }
}