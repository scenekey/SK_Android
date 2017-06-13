package com.scenekey.Utility;


import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.scenekey.R;


public class CustomToastDialog extends Dialog {

    String message, buttonLeft, buttonRight;
    View pop_up_view;
    TextView txt_pop10_One, txt_pop10_message;


    public CustomToastDialog(Activity activity) {
        super(activity, android.R.style.Theme_Translucent);
        pop_up_view = LayoutInflater.from(activity).inflate(R.layout.pop_cus_toast, null);
        txt_pop10_One = (TextView) pop_up_view.findViewById(R.id.txt_pop10_One);
        txt_pop10_message = (TextView) pop_up_view.findViewById(R.id.txt_pop10_message);
        Font font = new Font(activity);
        font.setFontLibreFranklin_SemiBold(txt_pop10_One, txt_pop10_message);
        //.setTypeface(font.Franklin_Gothic_Reg);
        //this.context = context;
        // This is the layout XML file that describes your Dialog layout
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(pop_up_view);

        txt_pop10_One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        /******************** This is to set Layout Parmanetes ********************************/

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.width = (int) activity.getResources().getDimension(R.dimen._250sdp);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND; // This is for Demmed effect
        lp.dimAmount = 0.4f;                                    // set Dimmed level from here.
        this.getWindow().setAttributes(lp);
    }


    public void setLeftButtonText(String s) {
        txt_pop10_One.setText(s);

    }

    public void setMessage(String s) {
        txt_pop10_message.setText(s);
    }


}
