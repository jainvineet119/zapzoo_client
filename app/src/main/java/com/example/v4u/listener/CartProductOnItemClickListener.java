package com.example.v4u.listener;

public interface CartProductOnItemClickListener {
    void onRemove(int position);
    void onAdd(int position);
    void onMinus(int position);
}
