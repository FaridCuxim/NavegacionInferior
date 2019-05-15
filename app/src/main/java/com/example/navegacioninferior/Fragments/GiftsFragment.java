package com.example.navegacioninferior.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.navegacioninferior.R;


public class GiftsFragment extends Fragment {
    public GiftsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootBiew = inflater.inflate(R.layout.fragment_gifts, container, false);
        return rootBiew;
    }

}
