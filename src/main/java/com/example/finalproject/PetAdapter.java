package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Random;

public class PetAdapter extends RecyclerView.Adapter {

    Cursor cursor;
    SQLiteDatabase sqLiteDatabase;
    PetDatabaseHelper petDatabaseHelper;

    public PetAdapter(Cursor cursor, Context applicationContext) {
        this.cursor = cursor;
        petDatabaseHelper = new PetDatabaseHelper(applicationContext);
    }

    public static class PetViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout linearLayout, linearLayout2;
        public TextView textViewName, textViewType, textViewGender, textViewAge, textViewSize;
        public CheckBox checkBox;

        public PetViewHolder(@NonNull View view){
            super(view);
            this.linearLayout = view.findViewById(R.id.linearLayout);
            this.linearLayout2 = view.findViewById(R.id.linearLayout2);
            this.textViewName = view.findViewById(R.id.textViewName);
            this.textViewType = view.findViewById(R.id.textViewType);
            this.textViewGender = view.findViewById(R.id.textViewGender);
            this.textViewAge = view.findViewById(R.id.textViewAge);
            this.textViewSize = view.findViewById(R.id.textViewSize);
            this.checkBox = view.findViewById(R.id.checkBoxFav);
        }

        public LinearLayout getLinearLayout2(){
            return linearLayout2;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        sqLiteDatabase = petDatabaseHelper.getWritableDatabase();
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        PetViewHolder petViewHolder = (PetViewHolder) holder;
        View view = petViewHolder.getLinearLayout2();

        final SQLiteDatabase sqLiteDatabase = petDatabaseHelper.getReadableDatabase();
        cursor.moveToPosition(position);

        int column = cursor.getColumnIndex("name");
        petViewHolder.textViewName.setText("Name: "+cursor.getString(column));
        column = cursor.getColumnIndex("type");
        petViewHolder.textViewType.setText("Type: "+cursor.getString(column));
        column = cursor.getColumnIndex("gender");
        petViewHolder.textViewGender.setText("Gender: "+cursor.getString(column));
        column = cursor.getColumnIndex("age");
        petViewHolder.textViewAge.setText("Age: "+cursor.getString(column));
        column = cursor.getColumnIndex("size");
        petViewHolder.textViewSize.setText("Size: "+cursor.getString(column));
        column = cursor.getColumnIndex("favorite");
        int fav = cursor.getInt(column);
        if(fav == 0){
            petViewHolder.checkBox.setChecked(false);
            view.setBackgroundColor(Color.WHITE);
            petViewHolder.checkBox.setBackgroundColor(Color.WHITE);

        } else if (fav == 1){
            petViewHolder.checkBox.setChecked(true);
            view.setBackgroundColor(Color.GREEN);
            petViewHolder.checkBox.setBackgroundColor(Color.GREEN);
        }

        petViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cursor.moveToPosition(position);
                ContentValues updatedValue = new ContentValues();
                if(b){
                    updatedValue.put("favorite", 1);
                    int id = cursor.getInt(cursor.getColumnIndex("_id"));
                    sqLiteDatabase.update("pets",
                            updatedValue,
                            "_id = ?",
                            new String[] {String.valueOf(id)});
                } else{
                    updatedValue.put("favorite", 0);
                    int id = cursor.getInt(cursor.getColumnIndex("_id"));
                    String where = "_id = ?";
                    sqLiteDatabase.update("pets",
                            updatedValue,
                            where,
                            new String[] {String.valueOf(id)});
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
