package com.example.qrscanner1;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Item>> items = new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<List<Item>> getItems() {
        return items;
    }

    public void addItem(Item item) {
        List<Item> currentItems = items.getValue();
        if (currentItems != null) {
            currentItems.add(item);
            items.setValue(currentItems); // Notify observers of the change
        }
    }
}
