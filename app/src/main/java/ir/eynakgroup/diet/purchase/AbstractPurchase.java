package ir.eynakgroup.caloriemeter.shop;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.eynakgroup.caloriemeter.BuildConfig;
import ir.eynakgroup.caloriemeter.R;
import ir.eynakgroup.caloriemeter.handlers.AppController;
import ir.eynakgroup.caloriemeter.handlers.ConnectionDetector;
import ir.eynakgroup.caloriemeter.payment.util.IabHelper;
import ir.eynakgroup.caloriemeter.payment.util.Purchase;
import ir.eynakgroup.caloriemeter.purchase.Ishop;
import ir.eynakgroup.caloriemeter.util.DatabaseHandler;

/**
 * Created by Navid on 4/12/2017.
 */

public abstract class AbstractPurchase {
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAILED = "failed";
    protected Context mContext;
    protected Ishop mShop;
    protected IabHelper mHelper;

    protected AbstractPurchase(Context context) {
        this.mContext = context;
        this.mShop = (Ishop) context;
    }

    public IabHelper getHelper() {
        return mHelper;
    }

    abstract public void setupIABHelper();

    public void getAllSkus() {
        if (new ConnectionDetector(mContext).isConnectingToInternet()) {
            String url = mContext.getResources().getString(R.string.server_address) + "sku/list";

            StringRequest strReq = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {

                        public void onResponse(String response) {
                            try {
                                JSONObject responseJSON = new JSONObject(response);
                                DatabaseHandler db = new DatabaseHandler(mContext);
                                Log.i("SKU", response);
                                if (responseJSON.has("updatedAt")) {
                                    (new DatabaseHandler(mContext)).setPurchaseUpdatedAt(responseJSON.getString("updatedAt"));
                                }
                                JSONArray skus = responseJSON.getJSONArray("skus");
                                for (int i = 0; i < skus.length(); i++) {
                                    JSONObject purchaseObject = new JSONObject(skus.getString(i));
                                    String sku = purchaseObject.getString("skuString");
                                    int isPremium = purchaseObject.getInt("isPremium");
                                    Log.i("SKU FETCH", sku);
                                    boolean premium = true;
                                    if (isPremium == 0)
                                        premium = false;
                                    int price = purchaseObject.getInt("price");
                                    String imagePath = "null";
                                    if (purchaseObject.has("imagePath"))
                                        imagePath = purchaseObject.getString("imagePath");
                                    JSONArray productsObject = purchaseObject.getJSONArray("products");
                                    ArrayList<Product> products = new ArrayList<>();
                                    for (int j = 0; j < productsObject.length(); j++) {
                                        switch (productsObject.getString(j)) {
                                            case "stoVids":
                                                products.add(Product.StoVideos);
                                                break;
                                            case "legVids":
                                                products.add(Product.LegVideos);
                                                break;
                                            case "fatVids":
                                                products.add(Product.FatVideos);
                                                break;
                                            case "ffcVids":
                                                products.add(Product.FFCVideos);
                                                break;
                                            case "wlVids":
                                                products.add(Product.WorkoutVideos);
                                                break;
                                            case "monthSub":
                                            case "monthly":
                                            case "monthSubBazaar":
                                            case "monthSubMyket":
                                                products.add(Product.MonthlySubscription);
                                                break;
                                            case "yearSub":
                                                products.add(Product.YearlySubscription);
                                                break;
                                            case "recipes":
                                                products.add(Product.Recipes);
                                                break;
                                            case "seasonallySub":
                                            case "seasonally":
                                                products.add(Product.SeasonSubscription);
                                                break;
                                            case "coupleSeasonallySub":
                                            case "coupleSeasonally":
                                                products.add(Product.CoupleSeasonSubscription);
                                                break;
                                            case "nolimitSub":
                                            case "lifeTimeSubBazaar":
                                            case "lifeTime":
                                            case "lifeTimeSubMyket":
                                                products.add(Product.LifetimeSubscription);
                                                break;
                                        }
                                    }
                                    db.insertPurchase(new LocalPurchase(sku, price, premium, products, imagePath));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    DatabaseHandler db = new DatabaseHandler(mContext);
                    params.put("market", BuildConfig.MARKET_ID);//
                    params.put("updatedAt", db.getPurchaseUpdatedAt());
                    params.put("apikey", db.getUserApiKey());//
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
                    "discount code verification");
        }
    }

    abstract public void queryInventory();

    abstract public void launchPurchaseFlow(String sku, String discountCode);

    abstract public void dispose();

    public boolean verifyDeveloperPayload(ir.eynakgroup.caloriemeter.payment.util.Purchase p) {
        // String payload = p.getDeveloperPayload();

        // if (payload.equals(devVerif))
        return true;
        // else
        // return false;

		/*
         * TODO: verify that the developer payload of the purchase is correct.
		 * It will be the same one that you sent when initiating the purchase.
		 * 
		 * WARNING: Locally generating a random string when starting a purchase
		 * and verifying it here might seem like a good approach, but this will
		 * fail in the case where the user purchases an item on one device and
		 * then uses your app on a different device, because on the other device
		 * you will not have access to the random string you originally
		 * generated.
		 * 
		 * So a good developer payload has these characteristics:
		 * 
		 * 1. If two different users purchase an item, the payload is different
		 * between them, so that one user's purchase can't be replayed to
		 * another user.
		 * 
		 * 2. The payload must be such that you can verify it even when the app
		 * wasn't the one who initiated the purchase flow (so that items
		 * purchased by the user on one device work on other devices owned by
		 * the user).
		 * 
		 * Using your own server to store and verify developer payloads across
		 * app installations is recommended.
		 */
    }

    public void cancelSubscription(Activity activity, Purchase purchase) {

    }

}
