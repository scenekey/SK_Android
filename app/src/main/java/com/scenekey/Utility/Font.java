package com.scenekey.Utility;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by mindiii on 10/4/17.
 */

public class Font {

    final int REQUEST_OK = 1;
    Context context;
    TextView txtOpinion;
    Typeface Raleway_Light, Raleway_Bold, Raleway_Medium, Raleway_Regular;
    Typeface Franklin_Gothic_Italic, Franklin_Gothic_Reg, Frank_Demi_Reg, Frank_Demi_cond_reg, Frank_Heavy_Reg, Frank_Book;
    Typeface Seaguisym;
    Typeface euphemia;
    Typeface Arial_Regular;

    public Font(Context context) {

        this.context = context;
        this.Raleway_Light = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/Raleway-Light.ttf");
        this.Raleway_Bold = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/Raleway-Bold.ttf");
        this.Raleway_Medium = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/Raleway-Medium.ttf");
        this.Raleway_Regular = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/Raleway-Regular.ttf");
        this.Seaguisym = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/seguisym.ttf");
        this.Franklin_Gothic_Reg = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/Franklin_Gothic_Reg.ttf");
        this.Franklin_Gothic_Italic = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/Franklin_Gothic_Italic.ttf");
        this.Frank_Demi_Reg = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/Frank_Demi_Reg.ttf");
        this.Frank_Demi_cond_reg = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/Frank_Demi_cond_reg.ttf");
        this.Frank_Heavy_Reg = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/Frank_Heavy_Reg.ttf");
        this.Frank_Book = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/Frank_Book.ttf");
        this.euphemia = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/euphemia.ttf");
        this.Arial_Regular = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/Arial_Regular.ttf");

    }

    public void setFont(TextView text) {
        text.setTypeface(Raleway_Light);
    }


    public void setFontBold(TextView text) {

        text.setTypeface(Raleway_Bold);
    }

    public void setFontBold(TextView... texts) {
        for (TextView text : texts) {
            text.setTypeface(Raleway_Bold);
        }
    }

    public void setFontRailMedium(TextView text) {
        text.setTypeface(Raleway_Medium);
    }

    public void setFontRailRegular(TextView text) {
        text.setTypeface(Raleway_Regular);
    }

    public void setFontRailRegularLight(TextView... texts) {
        for (TextView text : texts) {
            text.setTypeface(Raleway_Light);
        }
    }

    public void setFontSGS(TextView text) {
        text.setTypeface(Seaguisym);
    }

    public Typeface RailMedium() {
        return Raleway_Medium;
    }

    public Typeface RailRegular() {
        return Raleway_Regular;
    }

    public Typeface RailLight() {
        return Raleway_Light;
    }

    public Typeface RailBold() {
        return Raleway_Bold;
    }

    public Typeface SGS() {
        return Seaguisym;
    }

    public Typeface FrankRegular() {
        return Franklin_Gothic_Reg;
    }

    public Typeface FrankBookRegular() {
        return Frank_Book;
    }

    public Typeface FrankHeavy() {
        return Frank_Heavy_Reg;
    }

    public Typeface Arial_Regular() {
        return Arial_Regular;
    }

    public void setFontFranklinRegular(TextView... textViews) {
        for (TextView t : textViews) {
            t.setTypeface(Franklin_Gothic_Reg);
        }
    }

    public void setFontFrankDemiReg(TextView... textViews) {
        for (TextView t : textViews) {
            t.setTypeface(Frank_Demi_Reg);
        }
    }

    public void setFontArial_Regular(TextView... textViews) {
        for (TextView t : textViews) {
            t.setTypeface(Arial_Regular);
        }
    }

    public void setFontFrankBookReg(TextView... textViews) {
        for (TextView t : textViews) {
            t.setTypeface(Frank_Book);
        }
    }

    public void setFontEuphemia(TextView... textViews) {
        for (TextView t : textViews) {
            t.setTypeface(euphemia);
        }
    }

    public void setFontFrankDemiCondReg(TextView... textViews) {
        for (TextView t : textViews) {
            t.setTypeface(Frank_Demi_cond_reg);
        }
    }

    public void setFontFrank_Heavy_Reg(TextView... textViews) {
        for (TextView t : textViews) {
            t.setTypeface(Frank_Heavy_Reg);
        }
    }

}
