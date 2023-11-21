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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.fragment.app.Fragment;

import java.util.List;

// ... (import statements)

public class ItemEntryFragment extends Fragment{

    private EditText etItemBarcode, etItemName, etItemQuantity;
    //private TableLayout tableItems;
    private ItemEntryListener itemEntryListener;
    private SharedViewModel viewModel;
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

        // Get the ViewModel from the activity
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

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
//        btnConfirm.setOnClickListener(v -> {
//            String itemName = etItemName.getText().toString();
//            String itemQuantity = etItemQuantity.getText().toString();
//            itemEntryListener.onItemEntry(itemName, itemQuantity);
//
//            // Navigate back to ScanFragment
//            //getActivity().getSupportFragmentManager().popBackStack();
////            getActivity().getSupportFragmentManager().beginTransaction()
////                    .replace(R.id.fragment_container, new ScanFragment())
////                    .commit();
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, new ScanFragment(), "ScanFragment")
//                    .commit();
//        });
//        btnConfirm.setOnClickListener(v -> {
//            String itemName = etItemName.getText().toString();
//            String itemQuantity = etItemQuantity.getText().toString();
//            String BarCode = etItemBarcode.getText().toString();
//            // Notify the original ScanFragment about the new item
//            itemEntryListener.onItemEntry(itemName, itemQuantity, BarCode);
//
//            // Navigate back to the existing ScanFragment
//            getActivity().getSupportFragmentManager().popBackStack("ScanFragment", 0);
//        });

        btnConfirm.setOnClickListener(v -> {
            String itemName = etItemName.getText().toString();
            String itemQuantity = etItemQuantity.getText().toString();
            String itemBarcode = etItemBarcode.getText().toString();
            //itemEntryListener.onItemEntry(itemName, itemQuantity, BarCode);
            if (isUniqueItem(itemName, itemBarcode)) {
                viewModel.addItem(new Item(itemName, itemQuantity,itemBarcode));
                // Navigate back
                getActivity().getSupportFragmentManager().popBackStack();}
            else{
                Toast.makeText(getContext(), "Item with this name or barcode already exists.", Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: Implement logic for imgItemPicture selection if needed

        return view;
    }
    private boolean isUniqueItem(String itemName, String itemBarcode) {
        List<Item> currentItems = viewModel.getItems().getValue();
        if (currentItems != null) {
            for (Item item : currentItems) {
                if (item.getName().equalsIgnoreCase(itemName) || item.getBarcode().equalsIgnoreCase(itemBarcode)) {
                    return false;
                }
            }
        }
        return true;
    }
}
