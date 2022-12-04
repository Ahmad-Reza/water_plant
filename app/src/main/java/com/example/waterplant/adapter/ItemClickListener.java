package com.example.waterplant.adapter;

public interface ItemClickListener<O extends Object> {
    void onItemClick(int position, O item);
}
