package ir.eynakgroup.diet.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.network.response_models.User;

/**
 * Created by Shayan on 2/16/2017.
 */

public class ToggleButtonGroupTableLayout extends TableLayout implements OnClickListener {

    private static final String TAG = "ToggleButtonGroupTableLayout";
    private RadioButton activeRadioButton;
    private User mUser;

    /**
     * @param context
     */
    public ToggleButtonGroupTableLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param context
     * @param attrs
     */
    public ToggleButtonGroupTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onClick(View v) {
        final RadioButton rb = (RadioButton) v;
        if ( activeRadioButton != null ) {
            activeRadioButton.setChecked(false);
        }
        rb.setChecked(true);
        switch (rb.getId()) {
            case R.id.radio_very_low:
                mUser.setActivityLevel(1);
                break;
            case R.id.radio_low:
                mUser.setActivityLevel(2);
                break;
            case R.id.radio_normal:
                mUser.setActivityLevel(3);
                break;
            case R.id.radio_high:
                mUser.setActivityLevel(4);
                break;
            case R.id.radio_very_high:
                mUser.setActivityLevel(5);
                break;
        }
        activeRadioButton = rb;
    }

    public void setUser(User user){
        mUser = user;
    }

//    public int getActiveId(){
//        if(activeRadioButton != null)
//            return activeRadioButton.getId();
//
//        return -1;
//    }

    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, int, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setChildrenOnClickListener((TableRow)child);
    }


    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        setChildrenOnClickListener((TableRow)child);
    }


    private void setChildrenOnClickListener(TableRow tr) {
        final int c = tr.getChildCount();
        for (int i=0; i < c; i++) {
            final View v = tr.getChildAt(i);
            if ( v instanceof RadioButton ) {
                v.setOnClickListener(this);
            }
        }
    }

    public int getCheckedRadioButtonId() {
        if ( activeRadioButton != null ) {
            return activeRadioButton.getId();
        }

        return -1;
    }
}