package ir.eynakgroup.diet.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.account.KarafsAccountConfig;
import ir.eynakgroup.diet.services.AuthenticationService;
import ir.eynakgroup.karafs.account.IAuthentication;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Shayan on 3/2/2017.
 */
public class SplashActivity extends BaseActivity {

    private ServiceConnection mConnection;
    private IAuthentication mService;
    private boolean boundService = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        GifImageView gifSplash = (GifImageView) findViewById(R.id.gif_splash);
        gifSplash.getLayoutParams().width = (int) (getDisplayMetrics().widthPixels / 1.9);
        gifSplash.getLayoutParams().height = (int) (getDisplayMetrics().widthPixels / 1.9);
        gifSplash.requestLayout();
    }

    private class BindServiceTask extends AsyncTask<Void, Void, Boolean> {
        protected void onPreExecute() {
            super.onPreExecute();

            if (mConnection == null) {
                mConnection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        mService = IAuthentication.Stub.asInterface(service);

                        Map accountProperty = null;
                        try {
                            accountProperty = mService.getAccountProperties();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        if (accountProperty != null && accountProperty.size() != 0) {
                            System.out.println(accountProperty.get(KarafsAccountConfig.ACCOUNT_NAME));

                        } else {
                            if (getAppPreferences().getFirstTime()) {
                                startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                            } else {
                                startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                            }

                            finish();
                        }
                        releaseService();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                };
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return authenticate();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.d("bind", "initService() bound with " + result.toString());
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        new CountDownTimer(3000, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("---;:::::::::::");
            }

            @Override
            public void onFinish() {
                new BindServiceTask().execute();
                cancel();
            }
        }.start();

        try {
            updateData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean authenticate() {
        Intent serviceIntent = new Intent(getString(R.string.action_authentication));
        serviceIntent.setClass(this, AuthenticationService.class);
        List<ResolveInfo> intentServices = getPackageManager().queryIntentServices(serviceIntent, 0);
        if (intentServices != null && !intentServices.isEmpty())
            return boundService = bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);

        return false;
    }


    @Override
    protected void onPause() {
        super.onPause();
        releaseService();
    }


    /**
     * Unbinds this activity from the service.
     */
    private void releaseService() {
        if (mConnection != null && boundService) {
            unbindService(mConnection);
            mConnection = null;
            mService = null;
            boundService = false;
            Log.d("unbind", "releaseService() unbound.");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseService();
    }

}
