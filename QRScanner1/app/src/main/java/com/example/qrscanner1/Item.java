package com.example.qrscanner1;

public class Item {
    private String name;
    private String quantity;
    private String barcode;

    public Item(String name, String quantity, String barcode) {
        this.name = name;
        this.quantity = quantity;
        this.barcode = barcode;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
}
