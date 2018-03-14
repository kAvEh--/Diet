package ir.eynakgroup.diet.purchase.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.activities.BaseShopActivity;
import ir.eynakgroup.diet.purchase.AbstractPurchase;
import ir.eynakgroup.diet.purchase.LocalPurchase;
import ir.eynakgroup.diet.purchase.PaymentImpl;

/**
 * Created by Navid on 16/12/2017.
 */

public class IabHelper {
    private static final String TAG = "IabHelper";
    private Context mContext;
    private Inventory mInventory;

    public IabHelper(Context context) {
        mContext = context;
    }

    public void startSetup(final OnIabSetupFinishedListener onIabSetupFinishedListener) {
        Log.i(TAG, "setupIab start");
        if (new ConnectionDetector(mContext).isConnectingToInternet()) {
            onIabSetupFinishedListener.onIabSetupFinished(AbstractPurchase.STATUS_SUCCESS);
        } else {
            onIabSetupFinishedListener.onIabSetupFinished(AbstractPurchase.STATUS_FAILED);
        }
    }

    public void queryInventoryAsync(final QueryInventoryFinishedListener listener) {
        String phoneNumber = new DatabaseHandler(mContext).getUser().getPhoneNumber();
        checkSubs(phoneNumber, new PaymentImpl.ExistsCallback() {
            @Override
            public void onSuccess(String status, Inventory inventory) {
                mInventory = inventory;
                listener.onQueryInventoryFinished(status, mInventory);
            }
        });
    }

    private void checkSubs(final String phoneNumber, final PaymentImpl.ExistsCallback callback) {
        String url = mContext.getResources().getString(R.string.server_address) + "subscription/check";

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {
                        onSubsFetched(response, callback);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                callback.onSuccess(AbstractPurchase.STATUS_FAILED, null);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phoneNumber", phoneNumber);
                params.put("market", 8 + "");
                params.put("karafs", "true");
                params.put("android", "true");
                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq,
                "login request");
    }

    private void onSubsFetched(String response, PaymentImpl.ExistsCallback callback) {
        try {
            JSONObject responseJSON = new JSONObject(response);
            if (responseJSON.has("err")) {
                callback.onSuccess(Inventory.STATUS_FAILED, null);
                Log.i(TAG, responseJSON.toString());
                return;
            }
            boolean subs = responseJSON.get("subscription").toString().matches("1");
            String expirationDate = responseJSON.get("expirationDate").toString();
            Log.i(TAG, subs + " " + expirationDate);
            if (subs) {
                Purchase purchase = new Purchase(expirationDate, BaseShopActivity.skuMap.get(BaseShopActivity.WEBSITE_MONTHLY));
                Inventory inventory = new Inventory(purchase);
                callback.onSuccess(Inventory.STATUS_SUCCESS, inventory);

            } else {
                callback.onSuccess(Inventory.STATUS_SUCCESS, new Inventory(null));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "onResponse: error");
            callback.onSuccess(Inventory.STATUS_FAILED, null);
        }
    }

    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }

    /**
     * Callback for setup process. This listener's {@link #onIabSetupFinished} method is called
     * when the setup process is complete.
     */
    public interface OnIabSetupFinishedListener {
        /**
         * Called to notify that setup is complete.
         *
         * @param result The result of the setup process.
         */
        public void onIabSetupFinished(String result);
    }


    /**
     * Callback that notifies when a purchase is finished.
     */
    public interface OnIabPurchaseFinishedListener {
        /**
         * Called to notify that an in-app purchase finished. If the purchase was successful,
         * then the sku parameter specifies which item was purchased. If the purchase failed,
         * the sku and extraData parameters may or may not be null, depending on how far the purchase
         * process went.
         *
         * @param result The result of the purchase.
         * @param info   The purchase information (null if purchase failed)
         */
        public void onIabPurchaseFinished(String result, LocalPurchase info);
    }

    /**
     * Listener that notifies when an inventory query operation completes.
     */
    public interface QueryInventoryFinishedListener {
        /**
         * Called to notify that an inventory query operation completed.
         *
         * @param result The result of the operation.
         * @param inv    The inventory.
         */
        public void onQueryInventoryFinished(String result, Inventory inv);
    }
}
