package com.example.notetoself;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {
    ExtendedFloatingActionButton fab;
    RecyclerView recyclerView;
    NotesAdapter adapter;
    SQLiteDatabase database;
    TextView guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper helper = new DBHelper(this);
        database = helper.getWritableDatabase();

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerview);
        guide = findViewById(R.id.empty_text);

        adapter = new NotesAdapter(this, getCursor());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        if (adapter.getItemCount() > 0) {
            guide.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.empty_text).setVisibility(View.GONE);
        } else {
            guide.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, CreateNoteActivity.class), 123);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter.getItemCount() > 0) {
            guide.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.empty_text).setVisibility(View.GONE);
        } else {
            guide.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 345 && requestCode == 123) {
            guide.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.renewCursor(getCursor());
        } else if (requestCode == 100 && resultCode == 789) {

            adapter.renewCursor(getCursor());
        }
    }

    private Cursor getCursor() {
        return database.query(
                Notes.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Notes.COLUMN_TITLE + " ASC"
        );
    }
}