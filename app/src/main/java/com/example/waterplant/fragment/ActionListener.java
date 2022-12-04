package com.example.waterplant.fragment;

public interface ActionListener<O extends Object> {
    void onActionPerformed(Action actionId, int position, O item);

    enum Action {
        SAVE,
        UPDATE,
        REMOVE
    }
}
