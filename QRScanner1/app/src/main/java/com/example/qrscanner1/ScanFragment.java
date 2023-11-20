package com.example.qrscanner1;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ScanFragment extends Fragment implements ItemEntryListener, MainActivity.ScanResultListener {
    Button btnAddItem, btnBack, btnScan;
    TableLayout tableItems;
    EditText etScanResult;
    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        tableItems = view.findViewById(R.id.table_items);
        btnAddItem = view.findViewById(R.id.btn_add_item);
        btnBack = view.findViewById(R.id.btn_back);
        btnScan = view.findViewById(R.id.btn_scan);
        etScanResult = view.findViewById(R.id.et_text_bar);
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setScanResultListener(this);
            btnScan.setOnClickListener(v -> mainActivity.scanCode());
        }
        //btnScan.setOnClickListener(v -> {mainActivity.scanCode();});
        btnAddItem.setOnClickListener(v -> {
            // Replace with ItemEntryFragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ItemEntryFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnBack.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    @Override
    public void onItemEntry(String itemName, String itemQuantity) {
//        TableRow newRow = new TableRow(getContext());
//        TextView nameAndQuantity = new TextView(getContext());
//        nameAndQuantity.setText(itemName + " - Qty: " + itemQuantity);
//        newRow.addView(nameAndQuantity);
//        tableItems.addView(newRow);
//        itemName = "sdfasdf";
//        itemQuantity = "10";
        TableRow newRow = new TableRow(getContext());

        // TextView for Item Name
        TextView tvItemName = new TextView(getContext());
        tvItemName.setText(itemName);
        //tvItemName.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)); // Weight 1
        newRow.addView(tvItemName);

        // TextView for Quantity
        TextView tvItemQuantity = new TextView(getContext());
        tvItemQuantity.setText(itemQuantity);
        //tvItemQuantity.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)); // Weight 1
        newRow.addView(tvItemQuantity);
        tvItemName.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvItemQuantity.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvItemName.setBackgroundColor(Color.YELLOW); // Just for testing
        tvItemQuantity.setBackgroundColor(Color.GREEN); // Just for testing
        tableItems.addView(newRow);
//        Log.d("ScanFragment", "tableItems is " + (tableItems == null ? "null" : "not null"));

    }

    @Override
    public void onScanResult(String result) {
        etScanResult.setText(result);
    }
}

