package com.example.notetoself;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;

    public NotesAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }
        String title = cursor.getString(cursor.getColumnIndex(Notes.COLUMN_TITLE));
        String text = cursor.getString(cursor.getColumnIndex(Notes.COLUMN_TEXT));
        String path = cursor.getString(cursor.getColumnIndex(Notes.COLUMN_IMAGE));
        long timestamps = cursor.getLong(cursor.getColumnIndex(Notes.COLUMN_TIMESTAMP));
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d");

        long id = cursor.getLong(cursor.getColumnIndex(Notes._ID));

        holder.title.setText(title);
        holder.text.setText(text);
        holder.date.setText(format.format(new Date(timestamps)));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoteDetailsActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("body", text);
                intent.putExtra("image", path);
                intent.putExtra("id", id);
                ((Activity) context).startActivityForResult(intent, 100);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void renewCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, text, date;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            date = itemView.findViewById(R.id.date);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
