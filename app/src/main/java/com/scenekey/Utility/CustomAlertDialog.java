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


public abstract class CustomAlertDialog extends Dialog {

    String message, buttonLeft, buttonRight;
    View pop_up_view;
    TextView txt_pop10_One, txt_pop10_Two, txt_pop10_message, txt_pop_title;


    public CustomAlertDialog(Activity activity) {
        super(activity, android.R.style.Theme_Translucent);
        pop_up_view = LayoutInflater.from(activity).inflate(R.layout.pop_cus_alert, null);
        txt_pop10_One = (TextView) pop_up_view.findViewById(R.id.txt_pop10_One);
        txt_pop10_Two = (TextView) pop_up_view.findViewById(R.id.txt_pop10_Two);
        txt_pop10_message = (TextView) pop_up_view.findViewById(R.id.txt_pop10_message);
        txt_pop_title = (TextView) pop_up_view.findViewById(R.id.txt_pop_title);
        Font font = new Font(activity);
        txt_pop10_One.setTypeface(font.Arial_Regular());
        txt_pop10_Two.setTypeface(font.Arial_Regular());
        txt_pop10_message.setTypeface(font.Arial_Regular());
        txt_pop_title.setTypeface(font.Arial_Regular());
        //this.context = context;
        // This is the layout XML file that describes your Dialog layout
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(pop_up_view);

        txt_pop10_One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftButtonClick();
            }
        });

        txt_pop10_Two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightButtonClick();
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

		/*public static void DisplayProgress(Context ctn){
            dialog = new ProgressDialog(ctn);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialog.show();dialog.setContentView(R.layout.custom_progressdialog);
			dialog.setCancelable(true);
		}
		public static void HideProgress(Context ctn)
		{
			if(dialog!=null){if(dialog.isShowing())
			{dialog.cancel();}}}*/
    }

    public abstract void leftButtonClick();

    public abstract void rightButtonClick();

    public void setLeftButtonText(String s) {
        txt_pop10_One.setText(s);

    }

    public void setRightButtonText(String s) {
        txt_pop10_Two.setText(s);

    }

    public void setMessage(String s) {
        txt_pop10_message.setText(s);
    }

    public void setTitle(String s) {
        txt_pop_title.setText(s);
    }


}
