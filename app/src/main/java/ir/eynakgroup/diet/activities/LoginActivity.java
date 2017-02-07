package ir.eynakgroup.diet.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import ir.eynakgroup.diet.R;


/**
 * Created by Shayan on 2/7/2017.
 */
public class LoginActivity extends AppCompatActivity {

    private Button mSignupBtn;
    private Button mKarafsBtn;

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

        mSignupBtn = (Button) findViewById(R.id.btn_signup);
        mKarafsBtn = (Button) findViewById(R.id.btn_enter_karafs);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        mSignupBtn.setWidth((int)(displaymetrics.widthPixels/2.5));
        mSignupBtn.setHeight(displaymetrics.heightPixels/100);

        mKarafsBtn.setWidth((int)(displaymetrics.widthPixels/2.5));
        mKarafsBtn.setHeight(displaymetrics.heightPixels/100);

    }
}
