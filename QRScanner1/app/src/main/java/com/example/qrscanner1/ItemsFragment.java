package com.example.qrscanner1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class ItemsFragment extends Fragment {
    Button btn_scan;

    public ItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        btn_scan = view.findViewById(R.id.btn_items);
        btn_scan.setOnClickListener(v -> {
            // Replace with ScanFragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ScanFragment())
                    .addToBackStack("ScanFragment")
                    .commit();
        });
        return view;
    }
}

