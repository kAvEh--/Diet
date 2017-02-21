package ir.eynakgroup.diet.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import ir.eynakgroup.diet.R;


/**
 * Created by Shayan on 2/7/2017.
 */
public class LoginActivity extends MainActivity implements View.OnClickListener {

    private Button mSignupBtn;
    private Button mKarafsBtn;
    private Button mEnterBtn;

    private TextInputLayout mPhoneLayout;
    private TextInputLayout mPassLayout;

    private TextInputEditText mPassEdit;
    private TextInputEditText mPhoneEdit;
    private TextView mTxtForgot;

    private boolean showPassword = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPassLayout = (TextInputLayout) findViewById(R.id.txtInput2);
        mPhoneLayout = (TextInputLayout) findViewById(R.id.txtInput1);

        mPassEdit = (TextInputEditText) findViewById(R.id.edit_pass);
        mPassEdit.setLongClickable(false);
        mPassEdit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_black_18dp, 0, 0, 0);
        mPassEdit.getCompoundDrawables()[0].setAlpha(45);


        mPassEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getX() <= (mPassEdit.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width()))          // your action here
                    {
                        // changePasswordVisibility(v);
                        String temporary_stored_text = mPassEdit.getText().toString().trim();
                        if(!showPassword) {
                            mPassEdit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_off_black_18dp, 0, 0, 0);
                            mPassEdit.setTransformationMethod(null);
                            showPassword = true;
                        }else{
                            mPassEdit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_black_18dp, 0, 0, 0);
                            mPassEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            showPassword = false;
                        }
                        mPassEdit.setText(temporary_stored_text);
                        mPassEdit.getCompoundDrawables()[DRAWABLE_LEFT].setAlpha(45);
                        return true;
                    }
                }
                return false;
            }
        });

        mPhoneEdit = (TextInputEditText) findViewById(R.id.edit_phone);
        mTxtForgot = (TextView) findViewById(R.id.txt_password_forgot);

        mSignupBtn = (Button) findViewById(R.id.btn_signup);
        mKarafsBtn = (Button) findViewById(R.id.btn_enter_karafs);
        mEnterBtn = (Button) findViewById(R.id.btn_enter);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        mSignupBtn.setWidth(displaymetrics.widthPixels / 2);
        mSignupBtn.setHeight(displaymetrics.heightPixels / 100);

        mKarafsBtn.setWidth(displaymetrics.widthPixels / 2);
        mKarafsBtn.setHeight(displaymetrics.heightPixels / 100);

        mEnterBtn.setWidth(displaymetrics.widthPixels / 2);
        mEnterBtn.setHeight(displaymetrics.heightPixels / 100);

        mPhoneEdit.setWidth((int) (displaymetrics.widthPixels / 1.25));
        mPassEdit.setWidth((int) (displaymetrics.widthPixels / 1.25));

        mKarafsBtn.setOnClickListener(this);
        mSignupBtn.setOnClickListener(this);
        mTxtForgot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_enter_karafs:
                mKarafsBtn.setVisibility(View.INVISIBLE);
                mSignupBtn.setVisibility(View.INVISIBLE);
                mPassLayout.setVisibility(View.VISIBLE);
                mPhoneLayout.setVisibility(View.VISIBLE);
                mTxtForgot.setVisibility(View.VISIBLE);
                mEnterBtn.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_signup:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.btn_enter:

                break;
            case R.id.txt_password_forgot:
                startActivity(new Intent(this, ForgotPassActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (mEnterBtn.getVisibility() == View.INVISIBLE) {
            super.onBackPressed();
            return;
        }
        mPassLayout.setVisibility(View.INVISIBLE);
        mPhoneLayout.setVisibility(View.INVISIBLE);
        mTxtForgot.setVisibility(View.INVISIBLE);
        mEnterBtn.setVisibility(View.INVISIBLE);
        mKarafsBtn.setVisibility(View.VISIBLE);
        mSignupBtn.setVisibility(View.VISIBLE);

    }


}
