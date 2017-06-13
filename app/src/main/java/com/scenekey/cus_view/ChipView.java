package com.scenekey.cus_view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.Utility.Font;

/**
 * Created by mindiii on 9/5/17.
 */

public abstract class ChipView extends RelativeLayout implements View.OnClickListener {
    public boolean addAsotherView;
    Context context;
    TextView label;
    ImageView delete_button;
    String Id;

    // ChipDeleteListner chipDeleteListner;
    public ChipView(Context context, String Id) {
        super(context);
        this.context = context;
        this.Id = Id;
        initiateview();
        label.setTypeface(new Font(context).RailRegular());
    }


    void initiateview() {
        View view = inflate(context, R.layout.z_cus_chip_view, this);
        delete_button = (ImageView) view.findViewById(R.id.delete_button);
        label = (TextView) view.findViewById(R.id.label);
        delete_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_button:
                setDeleteListner(this);
                // if(chipDeleteListner != null) setOnDeleteListner(chipDeleteListner);
                ((FlowLayout) this.getParent()).removeView(this);
                break;

        }
    }

    public void setText(String text) {
        label.setText(text);
        System.out.println();
    }

    public ChipView addAsOddOneOut() {
        delete_button.setVisibility(GONE);
        this.getChildAt(0).setBackground(getResources().getDrawable(R.drawable.bg_white_tag));
        label.setTextColor(getResources().getColor(R.color.border_gray));
        return this;
    }

    public TextView getTextView() {
        return label;
    }

    public abstract void setDeleteListner(ChipView chipView);
    /*public void setOnDeleteListner(@Nullable ChipDeleteListner chipDeleteListner){
        setChipDeleteListner(chipDeleteListner);
        chipDeleteListner.onDeleteListner(this);

    }*/

    /*public void setChipDeleteListner(ChipDeleteListner chipDeleteListner) {
        this.chipDeleteListner = chipDeleteListner;
    }
*/

    public String getIdChip() {
        return Id;
    }
}
