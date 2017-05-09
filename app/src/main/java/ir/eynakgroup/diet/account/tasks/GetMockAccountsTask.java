package ir.eynakgroup.diet.account.tasks;

/**
 * Created by Shayan on 3/5/2017.
 */
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetMockAccountsTask extends AbstractAsyncTask<Void, Void, List<Account>> {

    private static final String TAG = GetMockAccountsTask.class.getSimpleName();

    public GetMockAccountsTask(Context context, String accountType) {
        super(context, accountType);
    }

    @Override
    protected List<Account> doInBackground(Void... params) {
        Context context = mContextReference.get();
        AccountManager accountManager = AccountManager.get(context);
        if (context == null) {
            Log.w(TAG, "Could not retrieve accounts as context no longer exists.");
            return null;
        }
        List<Account> result = new ArrayList<>();
        Account[] accounts = accountManager.getAccounts();
        for (Account account : accounts) {
            if (mAccountType.equals(account.type)) {
                result.add(account);
            }
        }
        return result;
    }
}