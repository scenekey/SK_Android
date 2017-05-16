package com.scenekey.models;

import java.io.Serializable;

/**
 * Created by mindiii on 9/5/17.
 */

public class Category implements Serializable {
    String category_id;
    String category;
    boolean selected;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
