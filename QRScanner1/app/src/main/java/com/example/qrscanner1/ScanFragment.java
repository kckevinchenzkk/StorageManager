package com.example.qrscanner1;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class ScanFragment extends Fragment implements ItemEntryListener, MainActivity.ScanResultListener {
    Button btnAddItem, btnBack, btnScan;
    TableLayout tableItems;
    EditText etScanResult;
    private SharedViewModel viewModel;
    public ScanFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).processPendingItemEntry();
        }
    }

// Rest of your ScanFragment code...


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
                    .addToBackStack("ScanFragment")
                    .commit();
        });

        btnBack.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getItems().observe(getViewLifecycleOwner(), this::updateTable);
        return view;
    }
    private void updateTable(List<Item> items) {
        tableItems.removeAllViews(); // Clear the existing views
        for (Item item : items) {
            // Add each item to the table
            TableRow newRow = new TableRow(getContext());
            TextView tvItemName = new TextView(getContext());
            tvItemName.setText("Name: " + item.getName());
            tvItemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            tvItemName.setBackgroundColor(Color.YELLOW); // Just for testing
            newRow.addView(tvItemName);

            TextView tvItemQuantity = new TextView(getContext());
            tvItemQuantity.setText("   Quantity: " + item.getQuantity());
            tvItemQuantity.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            tvItemQuantity.setBackgroundColor(Color.GREEN); // Just for testing
            newRow.addView(tvItemQuantity);

            tableItems.addView(newRow);
        }
    }
    @Override
    public void onItemEntry(String itemName, String itemQuantity, String BarCode) {
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
        tvItemName.setText("Name: " + itemName);
        tvItemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        //tvItemName.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)); // Weight 1
        newRow.addView(tvItemName);

        // TextView for Quantity
        TextView tvItemQuantity = new TextView(getContext());
        tvItemQuantity.setText("   Quantity: " + itemQuantity);
        tvItemQuantity.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
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


