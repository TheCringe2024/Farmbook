package com.example.farmbook.dao;

import com.example.farmbook.model.Item;

import java.util.List;

/**
 * What the app needs from item storage, without saying how it is stored.
 */
public interface IItemDAO {

    /** Saves a new item. */
    void save(Item item);

    /** Returns all saved items. */
    List<Item> findAll();

    /** Adds stock; false if the amount is negative. */
    boolean addStock(int itemId, int amount);

    /** Removes stock; false if there isn't enough. */
    boolean removeStock(int itemId, int amount);
}
