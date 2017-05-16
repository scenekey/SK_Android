package widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.scenekey.R;

//import com.app.tatoostore.R;

/**
 * Created by dharmraj on 17/4/17.
 */

public class CompactTextView extends AppCompatTextView {

    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public CompactTextView(Context context) {
        super(context);
    }

    public CompactTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context, attrs);
    }

    public CompactTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context, attrs);
    }


    private void applyCustomFont(Context context, AttributeSet attrs) {
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.CompactTextView);

        String fontName = attributeArray.getString(R.styleable.CompactTextView_font);
        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);
        Typeface customFont = selectTypeface(context, fontName, textStyle);
        setTypeface(customFont);
        attributeArray.recycle();
    }

    private Typeface selectTypeface(Context context, String fontName, int textStyle) {

        switch (fontName) {

            case FontType.LIGHT:
                return FontCache.getTypeface("Raleway_Light.ttf", context);

            case FontType.MEDIUM: // bold
                return FontCache.getTypeface("Raleway_Medium.ttf", context);

            case FontType.REGULAR: // italic
                return FontCache.getTypeface("Raleway_Regular.ttf", context);

            case FontType.SEMI_BOLD: // bold italic
                return FontCache.getTypeface("Raleway_SemiBold.ttf", context);

            default:
                return FontCache.getTypeface("Raleway_Regular.ttf", context);
        }

       /* if (fontName.contentEquals(context.getString(R.string.font_raleway_light))) {
            return FontCache.getTypeface("Raleway_Regular.ttf", context);
        } else if (fontName.contentEquals(context.getString(R.string.font_raleway_medium))) {
              *//*
              information about the TextView textStyle:
              http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
              *//*

        } else {
            // no matching font found
            // return null so Android just uses the standard font (Roboto)
            return null;

        }*/
    }
}