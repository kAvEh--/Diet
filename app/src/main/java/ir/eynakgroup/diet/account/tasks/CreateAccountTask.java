package ir.eynakgroup.diet.account.tasks;

/**
 * Created by Shayan on 3/5/2017.
 */
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.account.KarafsAccountConfig;
import ir.eynakgroup.diet.network.response_models.User;

public class CreateAccountTask extends AbstractAsyncTask<CreateAccountTask.AccountInfo, String, Account> {

    private static final String TAG = CreateAccountTask.class.getSimpleName();

    public static class AccountInfo {

        private String mPhoneNumber;
        private String mPassword;
        private Bundle mBundle;

        private AccountInfo(){}

        public AccountInfo(String phoneNumber, String password, User user) {
            mPhoneNumber = phoneNumber;
            mPassword = password;

            mBundle = new Bundle();
            mBundle.putString(KarafsAccountConfig.ACCOUNT_NAME, user.getName());
            mBundle.putString(KarafsAccountConfig.ACCOUNT_GENDER, user.getGender().toString());
            mBundle.putString(KarafsAccountConfig.ACCOUNT_BIRTHDAY, user.getBirthday().toString());
            mBundle.putString(KarafsAccountConfig.ACCOUNT_WEIGHT, String.valueOf(user.getWeight()));
            mBundle.putString(KarafsAccountConfig.ACCOUNT_HEIGHT, String.valueOf(user.getHeight()));
            mBundle.putString(KarafsAccountConfig.ACCOUNT_ACTIVITY_LEVEL, String.valueOf(user.getActivityLevel()));
            mBundle.putString(KarafsAccountConfig.ACCOUNT_API_KEY, user.getApiKey());
            mBundle.putString(KarafsAccountConfig.ACCOUNT_AGE, String.valueOf(user.getAge()));
            mBundle.putString(KarafsAccountConfig.ACCOUNT_ID, user.getUserId());
            mBundle.putString(KarafsAccountConfig.ACCOUNT_EMAIL, user.getEmail());
        }


        public String getPhoneNumber() {
            return mPhoneNumber;
        }

        public String getPassword() {
            return mPassword;
        }

        public Bundle getBundle() {
            return mBundle;
        }
    }

    private ProgressDialog mDialog;

    public CreateAccountTask(Context context, String accountType) {
        super(context, accountType);
    }
    @Override
    protected Account doInBackground(AccountInfo... params) {
        Account account = null;
        if (params == null || params.length == 0) {
            Log.d(TAG, "No account information provided, therefore no accounts can be created.");
            return null;
        }
//        Boolean[] results = new Boolean[params.length];
        Context context = mContextReference.get();
        if (context == null) {
            Log.w(TAG, "Could not add account information as context no longer exists.");
            return null;
        }
        AccountManager accountManager = AccountManager.get(context);
        for (AccountInfo accountInfo : params) {
            publishProgress(new String[] { accountInfo.getPhoneNumber() });
            account = new Account(accountInfo.getPhoneNumber(), mAccountType);
            accountManager.addAccountExplicitly(account, accountInfo.getPassword(), accountInfo.getBundle());
        }
        return account;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = showIndeterminableProgressDialog(null, null);
    }


    @Override
    protected void onPostExecute(Account result) {
        super.onPostExecute(result);
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Context context = mContextReference.get();
        if (context == null) {
            return;
        }
        if (values != null && values.length == 1 && mDialog != null && mDialog.isShowing()) {
            mDialog.setMessage(context.getString(R.string.create_status, values[0]));
        }
    }
}
