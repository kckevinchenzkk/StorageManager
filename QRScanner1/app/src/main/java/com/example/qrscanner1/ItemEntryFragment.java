package com.example.qrscanner1;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

// ... (import statements)

public class ItemEntryFragment extends Fragment{

    private EditText etItemBarcode, etItemName, etItemQuantity;
    //private TableLayout tableItems;
    private ItemEntryListener itemEntryListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemEntryListener) {
            itemEntryListener = (ItemEntryListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ItemEntryListener");
        }
    }

    public ItemEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_entry, container, false);

        etItemBarcode = view.findViewById(R.id.et_item_barcode);
        etItemName = view.findViewById(R.id.et_item_name);
        etItemQuantity = view.findViewById(R.id.et_item_quantity);
        Button btnScanBarcode = view.findViewById(R.id.btn_scan_barcode);
        Button btnConfirm = view.findViewById(R.id.btn_confirm);

        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setScanResultListener(result -> etItemBarcode.setText(result));
           // tableItems = mainActivity.getTableItems(); // Get tableItems from MainActivity
            btnScanBarcode.setOnClickListener(v -> mainActivity.scanCode()); // Use scanCode from MainActivity
        }

//        btnConfirm.setOnClickListener(v -> {
//            String itemName = etItemName.getText().toString();
//            String itemQuantity = etItemQuantity.getText().toString();
//
//            TableRow newRow = new TableRow(getActivity());
//            TextView nameAndQuantity = new TextView(getActivity());
//            nameAndQuantity.setText(itemName + " - Qty: " + itemQuantity);
//            newRow.addView(nameAndQuantity);
////            tableItems.addView(newRow);
//
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, new ScanFragment())
//                    .commit();
//        });
        btnConfirm.setOnClickListener(v -> {
            String itemName = etItemName.getText().toString();
            String itemQuantity = etItemQuantity.getText().toString();
            itemEntryListener.onItemEntry(itemName, itemQuantity);

            // Navigate back to ScanFragment
            getActivity().getSupportFragmentManager().popBackStack();
        });


        // TODO: Implement logic for imgItemPicture selection if needed

        return view;
    }
}
