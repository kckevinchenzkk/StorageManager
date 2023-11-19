package com.example.qrscanner1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {
    Button btn_scan;
    private EditText etItemBarcode;
    private TableLayout tableItems;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        btn_scan = findViewById(R.id.btn_items);
        btn_scan.setOnClickListener(v->{
            openScanLayout() ;
        });
    }
    private void openScanLayout() {
        setContentView(R.layout.activity_scan);

        Button btnScan = findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(v -> {
            scanCode();
        });

        Button btnAddItem = findViewById(R.id.btn_add_item);
        btnAddItem.setOnClickListener(v -> {
            openItemEntryLayout();
        });

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> setContentView(R.layout.activity_items));
        tableItems = findViewById(R.id.table_items);
    }
    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
    private void openItemEntryLayout() {
        setContentView(R.layout.activity_item_entry);

        // Initialize the EditText for barcode
        etItemBarcode = findViewById(R.id.et_item_barcode);

        // Implement logic to handle item picture selection
        ImageView imgItemPicture = findViewById(R.id.img_item_picture);
        // TODO: Set onClickListener to select an image

        // Implement logic to handle item information entry
        EditText etItemName = findViewById(R.id.et_item_name);
        EditText etItemQuantity = findViewById(R.id.et_item_quantity);
        Button btnScanBarcode = findViewById(R.id.btn_scan_barcode);
        Button btnConfirm = findViewById(R.id.btn_confirm);

        // Use existing scanCode() method to scan barcode
        btnScanBarcode.setOnClickListener(v -> scanCode());

        btnConfirm.setOnClickListener(v -> {
            // TODO: Retrieve item details
            // TODO: Update the table in activity_scan layout with new item
            // TODO: Switch back to activity_scan layout
            String itemName = etItemName.getText().toString();
            String itemQuantity = etItemQuantity.getText().toString();
            String barcode = etItemBarcode.getText().toString();

            // Update the table in activity_scan layout with new item
            TableRow newRow = new TableRow(MainActivity.this);
//            newRow.addView(imgItemPicture); // Add the picture
            TextView nameAndQuantity = new TextView(MainActivity.this);
            nameAndQuantity.setText(itemName + " - Qty: " + itemQuantity);
            newRow.addView(nameAndQuantity);
            tableItems.addView(newRow);

            // Switch back to activity_scan layout
            setContentView(R.layout.activity_scan);
        });

//        imgItemPicture.setOnClickListener(v -> {
//            // Open the gallery to select an image
//            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(intent, 1);
//        });

    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->
    {
        if(result.getContents() != null)
        {
            etItemBarcode.setText(result.getContents());
            AlertDialog.Builder builder = new AlertDialog.Builder((MainActivity.this));
            builder.setTitle("Result");

            // Add an EditText to the dialog for quantity input
//            final EditText input = new EditText(MainActivity.this);
//            input.setInputType(InputType.TYPE_CLASS_NUMBER);  // Set the input type as number
//            builder.setView(input);

//            builder.setMessage("Scanned Code: " + result.getContents() + "\nEnter Quantity:");
            builder.setMessage("Scanned Code: " + result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //String quantity = input.getText().toString();  // Get the quantity entered by the user
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
//            imgItemPicture.setImageURI(data.getData());
//        }
//    }
}