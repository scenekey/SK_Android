package com.scenekey.cus_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;

/**
 * Created by mindiii on 9/5/17.
 */

public class CustomGridlayout extends GridLayout {
    int rowcount;

    public CustomGridlayout(Context context) {
        super(context);
    }

    //This is Main method to set the layout from Find View by ID
    public CustomGridlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewAdded(View child) {
        for (int i = 0; i < this.getChildCount(); i++) {

        }
        super.onViewAdded(child);

    }
}
