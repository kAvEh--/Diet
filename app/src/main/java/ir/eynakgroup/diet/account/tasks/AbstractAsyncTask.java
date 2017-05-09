package ir.eynakgroup.diet.account.tasks;

/**
 * Created by Shayan on 3/5/2017.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import java.lang.ref.WeakReference;

abstract class AbstractAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    @SuppressWarnings("unused")
    private static final String TAG = AbstractAsyncTask.class.getSimpleName();

    protected WeakReference<Context> mContextReference;
    protected String mAccountType;

    public AbstractAsyncTask(Context context, String accountType) {
        mContextReference = new WeakReference<>(context);
        mAccountType = accountType;
    }

    protected ProgressDialog showIndeterminableProgressDialog(String title, String message) {
        Context context = mContextReference.get();
        if (context == null) {
            return null;
        }
        ProgressDialog dialog = ProgressDialog.show(context, title, message);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }
}