package ir.eynakgroup.caloriemeter.payment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import ir.eynakgroup.caloriemeter.R;
import ir.eynakgroup.caloriemeter.payment.util.IabHelper;
import ir.eynakgroup.caloriemeter.payment.util.Inventory;
import ir.eynakgroup.caloriemeter.shop.AbstractPurchase;
import ir.eynakgroup.caloriemeter.util.DatabaseHandler;

/**
 * Created by Navid on 5/12/2017.
 */

public class PaymentImpl extends AbstractPurchase {
    private static final String TAG = "Payment implementation";
    Context mContext;
    private IabHelper.QueryInventoryFinishedListener queryInventoryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(String result, Inventory inventory) {
            mShop.onQueryInventoryFinished(inventory);
        }
    };
    private IabHelper.OnIabSetupFinishedListener onIabSetupFinishedListener = new IabHelper.OnIabSetupFinishedListener() {
        @Override
        public void onIabSetupFinished(String result) {
            if (result.equals(AbstractPurchase.STATUS_SUCCESS))
                mShop.onIabSetupFinished(result);
        }
    };


    public PaymentImpl(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public void setupIABHelper() {
        if (mShop instanceof ShopActivity)
            mShop.showWaiting(true);
        mHelper = new IabHelper(mContext);
        mHelper.startSetup(onIabSetupFinishedListener);
    }

    @Override
    public void queryInventory() {
        mHelper.queryInventoryAsync(queryInventoryFinishedListener);
    }


    @Override
    public void launchPurchaseFlow(String sku, String discountCode) {
        String url = mContext.getResources().getString(R.string.server_address) + "shop/signer?";
        url += "username=" + (new DatabaseHandler(mContext)).getUser().getPhoneNumber();
        url += "&android=1";
        url += "&type=" + sku;
        if (discountCode != null && !discountCode.equals("") && !discountCode.equals("NA"))
            url += "&discountCode=" + discountCode;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mContext.startActivity(browserIntent);
    }

    @Override
    public void dispose() {

    }

    public interface ExistsCallback {
        void onSuccess(String status, Inventory inventory);
    }
}
