package com.example.qrscanner1;



import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.List;

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
    protected void onPause() {
        super.onPause();
        //saveData();
        // Code to save data from ViewModel to persistent storage
    }
    private void saveData() {
        new Thread(() -> {
            SharedViewModel viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
            List<Item> items = viewModel.getItems().getValue();
            if (items != null && !items.isEmpty()) {
                AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "database-name").build();
                ItemDao dao = db.itemDao();
                dao.insertAll(items);
            }
        }).start();
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
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted
//                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//                if (fragment instanceof ScanFragment) {
//                    ((ScanFragment) fragment).performExport();
//                }
//            } else {
//                // Permission denied
//                Toast.makeText(this, "Permission denied to write to storage", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}
