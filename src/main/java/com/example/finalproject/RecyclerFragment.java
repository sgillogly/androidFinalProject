package com.example.finalproject;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecyclerFragment extends Fragment {

    Cursor cursor;
    RecyclerView recyclerView;

    public RecyclerFragment(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        PetAdapter petAdapter = new PetAdapter(cursor, getContext());
        recyclerView.setAdapter(petAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }
}