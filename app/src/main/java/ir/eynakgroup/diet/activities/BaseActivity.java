package ir.eynakgroup.diet.activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.network.ClientFactory;
import ir.eynakgroup.diet.network.RequestMethod;
import ir.eynakgroup.diet.utils.AppPreferences;

class BaseActivity extends AppCompatActivity {

    protected static DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    protected AppPreferences mAppPreferences;
    protected static RequestMethod mRequestMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppPreferences = AppPreferences.getInstance(this);
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mRequestMethod = ClientFactory.getClientInstance().create(RequestMethod.class);

//        Window window = getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            window.setStatusBarColor(getResources().getColor(R.color.colorWhite, this.getTheme()));
//
//        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            window.setStatusBarColor(getResources().getColor(R.color.colorWhite));

    }

    protected Toast getToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_custom,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        return toast;
    }

}
