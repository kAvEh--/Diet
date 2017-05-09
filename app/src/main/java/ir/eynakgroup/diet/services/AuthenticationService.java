package ir.eynakgroup.diet.services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ir.eynakgroup.diet.BuildConfig;
import ir.eynakgroup.diet.account.KarafsAccountConfig;
import ir.eynakgroup.karafs.account.IAuthentication;

/**
 * Created by Shayan on 3/2/2017.
 */
public class AuthenticationService extends Service {

    private static final String CALORIMETER_PACKAGE = "ir.eynakgroup.caloriemeter";
    private static final String[] legalPackages = new String[]{CALORIMETER_PACKAGE, BuildConfig.APPLICATION_ID};

    private final IAuthentication.Stub mBinder = new IAuthentication.Stub() {
        @Override
        public Map getAccountProperties() throws RemoteException {

            AccountManager accountManager = AccountManager.get(AuthenticationService.this);
            Account[] accounts = accountManager.getAccountsByType(KarafsAccountConfig.ACCOUNT_TYPE);
            WeakReference<Map> mapWeakReference = new WeakReference<Map>(new HashMap());
            if (accounts.length != 0) {
                //TODO making bundle of account
                Map bundle = mapWeakReference.get();
                bundle.put(KarafsAccountConfig.ACCOUNT_PHONE_NUMBER, accounts[0].name);
                bundle.put(KarafsAccountConfig.ACCOUNT_PASSWORD, accountManager.getPassword(accounts[0]));
                bundle.put(KarafsAccountConfig.ACCOUNT_NAME, accountManager.getUserData(accounts[0], KarafsAccountConfig.ACCOUNT_NAME));
                bundle.put(KarafsAccountConfig.ACCOUNT_GENDER, accountManager.getUserData(accounts[0], KarafsAccountConfig.ACCOUNT_GENDER));
                bundle.put(KarafsAccountConfig.ACCOUNT_BIRTHDAY, accountManager.getUserData(accounts[0], KarafsAccountConfig.ACCOUNT_BIRTHDAY));
                bundle.put(KarafsAccountConfig.ACCOUNT_WEIGHT, accountManager.getUserData(accounts[0], KarafsAccountConfig.ACCOUNT_WEIGHT));
                bundle.put(KarafsAccountConfig.ACCOUNT_HEIGHT, accountManager.getUserData(accounts[0], KarafsAccountConfig.ACCOUNT_HEIGHT));
                bundle.put(KarafsAccountConfig.ACCOUNT_ACTIVITY_LEVEL, accountManager.getUserData(accounts[0], KarafsAccountConfig.ACCOUNT_ACTIVITY_LEVEL));
                bundle.put(KarafsAccountConfig.ACCOUNT_AGE, accountManager.getUserData(accounts[0], KarafsAccountConfig.ACCOUNT_AGE));
                bundle.put(KarafsAccountConfig.ACCOUNT_API_KEY, accountManager.getUserData(accounts[0], KarafsAccountConfig.ACCOUNT_API_KEY));
                bundle.put(KarafsAccountConfig.ACCOUNT_ID, accountManager.getUserData(accounts[0], KarafsAccountConfig.ACCOUNT_ID));
                bundle.put(KarafsAccountConfig.ACCOUNT_EMAIL, accountManager.getUserData(accounts[0], KarafsAccountConfig.ACCOUNT_EMAIL));
//                bundle.put(KarafsAccountConfig.ACCOUNT_SICKNESS_KIDNEY, accountManager.getUserData(accounts[0], KarafsAccountConfig.ACCOUNT_SICKNESS_KIDNEY));
//                bundle.put(KarafsAccountConfig.ACCOUNT_SICKNESS_HEART, accountManager.getUserData(accounts[0], KarafsAccountConfig.ACCOUNT_SICKNESS_HEART));
//                bundle.put(KarafsAccountConfig.ACCOUNT_SICKNESS_FAT, accountManager.getUserData(accounts[0], KarafsAccountConfig.ACCOUNT_SICKNESS_FAT));
//                bundle.put(KarafsAccountConfig.ACCOUNT_SICKNESS_DIABETES, accountManager.getUserData(accounts[0], KarafsAccountConfig.ACCOUNT_SICKNESS_DIABETES));

            }
            return mapWeakReference.get();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {

        return mBinder;
    }

}
