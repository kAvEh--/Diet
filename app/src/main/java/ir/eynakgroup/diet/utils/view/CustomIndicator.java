package ir.eynakgroup.diet.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by Shayan on 2/20/2017.
 */
public class CustomIndicator extends CirclePageIndicator {
    public CustomIndicator(Context context) {
        super(context);
    }

    public CustomIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

}
