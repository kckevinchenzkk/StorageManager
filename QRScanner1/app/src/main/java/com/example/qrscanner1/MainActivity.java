package com.example.qrscanner1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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


    @Override
    public void onItemEntry(String itemName, String itemQuantity) {
        ScanFragment scanFragment = (ScanFragment) getSupportFragmentManager().findFragmentByTag("ScanFragment");
        if (scanFragment != null) {
            scanFragment.onItemEntry(itemName, itemQuantity);
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
