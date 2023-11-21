package com.example.qrscanner1;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.qrscanner1.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM item")
    List<Item> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Item> items);

    @Delete
    void delete(Item item);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Item item);
}
