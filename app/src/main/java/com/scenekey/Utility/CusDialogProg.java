package com.scenekey.Utility;


import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.scenekey.R;


public class CusDialogProg extends Dialog {

    Context context;

	/*public CusDialogProg(Context context) {
		super(context, android.R.style.Theme_Translucent);
		this.context = context;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.setContentView(R.layout.custom_progress_dialog_layout);
	}*/

    public CusDialogProg(Context context, int Layout) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(Layout);
        setColor();
        //Util.setStatusBarColor((Activity) context,R.color.colorPrimary);
    }

    public void setColor() {
        this.getWindow().setBackgroundDrawableResource(R.color.black50p);
    }

    public void setColor(int colorcode) {
        this.getWindow().setBackgroundDrawableResource(colorcode);
    }
}
