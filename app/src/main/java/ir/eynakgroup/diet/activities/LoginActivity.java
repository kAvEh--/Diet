package ir.eynakgroup.diet.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import ir.eynakgroup.diet.R;


/**
 * Created by Shayan on 2/7/2017.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mSignupBtn;
    private Button mKarafsBtn;
    private Button mEnterBtn;

    private TextInputLayout mPhoneLayout;
    private TextInputLayout mPassLayout;

    private TextInputEditText mPassEdit;
    private TextInputEditText mPhoneEdit;
    private TextView mTxtForgot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorBlue, null));
        }

        mPassLayout = (TextInputLayout) findViewById(R.id.txtInput2);
        mPhoneLayout = (TextInputLayout) findViewById(R.id.txtInput1);

        mPassEdit = (TextInputEditText) findViewById(R.id.edit_pass);
        mPhoneEdit = (TextInputEditText) findViewById(R.id.edit_phone);
        mTxtForgot = (TextView) findViewById(R.id.txt_password_forgot);

        mSignupBtn = (Button) findViewById(R.id.btn_signup);
        mKarafsBtn = (Button) findViewById(R.id.btn_enter_karafs);
        mEnterBtn = (Button) findViewById(R.id.btn_enter);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        mSignupBtn.setWidth((int) (displaymetrics.widthPixels / 2.5));
        mSignupBtn.setHeight(displaymetrics.heightPixels / 100);

        mKarafsBtn.setWidth((int) (displaymetrics.widthPixels / 2.5));
        mKarafsBtn.setHeight(displaymetrics.heightPixels / 100);

        mEnterBtn.setWidth((int) (displaymetrics.widthPixels / 2.5));
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

                break;
            case R.id.btn_enter:

                break;
            case R.id.txt_password_forgot:
                
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
