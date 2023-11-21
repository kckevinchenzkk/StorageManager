package com.example.qrscanner1;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.AndroidViewModel;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;



public class SharedViewModel extends  AndroidViewModel {

    private MutableLiveData<List<Item>> items = new MutableLiveData<>(new ArrayList<>());

    // ... other ViewModel methods ...

    public SharedViewModel(Application application) {
        super(application);
        loadSavedData();
    }

    private void loadSavedData() {
        Log.d("SharedViewModel", "Loading saved data...");
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplication(),
                    AppDatabase.class, "database-name").build();
            ItemDao dao = db.itemDao();
            List<Item> savedItems = dao.getAll();

            // Log retrieved data
            Log.d("SharedViewModel", "Retrieved " + savedItems.size() + " items from the database.");
            for (Item item : savedItems) {
                Log.d("SharedViewModel", "Item: " + item.getName() + ", Barcode: " + item.getBarcode() + ", Quantity: " + item.getQuantity());
            }

            items.postValue(savedItems);
        }).start();
    }

    public MutableLiveData<List<Item>> getItems() {
        return items;
    }

    public void addItem(Item item) {
        List<Item> currentItems = items.getValue();
        if (currentItems != null) {
            if (!currentItems.contains(item)) {
                currentItems.add(item);
                items.setValue(currentItems); // Update LiveData

                new Thread(() -> {
                    AppDatabase db = Room.databaseBuilder(getApplication(),
                            AppDatabase.class, "database-name").build();
                    ItemDao dao = db.itemDao();
                    dao.insert(item); // Use the new insert method
                    Log.d("SharedViewModel", "Item added: " + item.getName());
                }).start();
            } else {
                Log.d("SharedViewModel", "Duplicate item not added: " + item.getName());
            }
        }
    }

    public void updateItem(Item item) {
        List<Item> currentItems = items.getValue();
        if (currentItems != null) {
            for (int i = 0; i < currentItems.size(); i++) {
                if (currentItems.get(i).getBarcode().equals(item.getBarcode())) {
                    currentItems.set(i, item); // Replace with the updated item
                    break;
                }
            }
            items.setValue(currentItems); // Update the LiveData
        }
    }

    public void removeItem(Item item) {
        List<Item> currentItems = items.getValue();
        if (currentItems != null) {
            currentItems.remove(item);
            items.setValue(currentItems); // Update the LiveData

            // Delete the item from the database in a background thread
            new Thread(() -> {
                AppDatabase db = Room.databaseBuilder(getApplication(),
                        AppDatabase.class, "database-name").build();
                ItemDao dao = db.itemDao();
                dao.delete(item);
            }).start();
        }
    }
}
