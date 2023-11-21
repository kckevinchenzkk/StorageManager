package com.example.qrscanner1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity implements ItemEntryListener {
    private TableLayout tableItems;
    private ActivityResultLauncher<ScanOptions> barLauncher;
    private ScanResultListener scanResultListener;
    public interface ScanResultListener {
        void onScanResult(String result);
    }
    public void setScanResultListener(ScanResultListener listener) {
        this.scanResultListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // This layout should contain a FrameLayout (or similar) for fragment replacement

        // Initialize barcode launcher
        barLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() != null && scanResultListener != null) {
                scanResultListener.onScanResult(result.getContents());
            }
        });

        // Display the initial fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ItemsFragment())
                    .commit();
        }

        // Initialize tableItems (Make sure it's part of your MainActivity's layout)
        // tableItems = findViewById(R.id.table_items); // Uncomment if tableItems are in your MainActivity layout
    }

    public void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    public TableLayout getTableItems() {
        return tableItems;
    }

    private String pendingItemName;
    private String pendingItemQuantity;
    private String pendingItemBarCode;
    @Override
    public void onItemEntry(String itemName, String itemQuantity, String itemBarCode) {
////        ScanFragment scanFragment = (ScanFragment) getSupportFragmentManager().findFragmentByTag("ScanFragment");
////        if (scanFragment != null) {
////            scanFragment.onItemEntry(itemName, itemQuantity);
////        }
//        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//
//        if (currentFragment instanceof ScanFragment) {
//            // If the current fragment is ScanFragment, cast it and call onItemEntry
//            ((ScanFragment) currentFragment).onItemEntry(itemName, itemQuantity);
//        } else {
//            // Handle the case where ScanFragment is not the current fragment
//            // This might involve logging an error, showing a message to the user, etc.
//            Log.e("MainActivity", "ScanFragment is not the current fragment.");
//        }
        this.pendingItemName = itemName;
        this.pendingItemQuantity = itemQuantity;
        this.pendingItemBarCode = itemBarCode;
        // Now trigger the popBackStack
        getSupportFragmentManager().popBackStack();
    }
    public void processPendingItemEntry() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof ScanFragment && pendingItemName != null && pendingItemQuantity != null && pendingItemBarCode != null) {
            ((ScanFragment) currentFragment).onItemEntry(pendingItemName, pendingItemQuantity, pendingItemBarCode);
            // Clear the pending data after processing
            pendingItemName = null;
            pendingItemQuantity = null;
        }
    }
    public void switchToItemEntryFragment() {
        ItemEntryFragment itemEntryFragment = new ItemEntryFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, itemEntryFragment, "ItemEntryFragment")
                .addToBackStack(null)
                .commit();
    }
}
