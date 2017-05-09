package ir.eynakgroup.diet.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import ir.eynakgroup.diet.R;

/**
 * Created by trainee.sana on 5/8/2016.
 */
public class CustomTextView extends AppCompatTextView {
    private String fontName = "iran_sans.ttf";

    public CustomTextView(Context context) {
        super(context);
        setFont();
     }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TextViewWithCustomFont);

        if(a.hasValue(0))
            fontName = a.getString(0);

        setFont();
    }


    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TextViewWithCustomFont);
        if(a.hasValue(0))
            fontName = a.getString(0);

        setFont();
    }

    private void setFont() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
        if (typeface != null)
            setTypeface(typeface);
    }
}
