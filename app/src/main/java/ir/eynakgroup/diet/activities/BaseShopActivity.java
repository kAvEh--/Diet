package ir.eynakgroup.caloriemeter.shop;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.eynakgroup.caloriemeter.BuildConfig;
import ir.eynakgroup.caloriemeter.R;
import ir.eynakgroup.caloriemeter.handlers.InternetFunctions;
import ir.eynakgroup.caloriemeter.payment.PaymentImpl;
import ir.eynakgroup.caloriemeter.payment.ShopPopup;
import ir.eynakgroup.caloriemeter.payment.util.IabResult;
import ir.eynakgroup.caloriemeter.payment.util.Inventory;
import ir.eynakgroup.caloriemeter.payment.util.Purchase;
import ir.eynakgroup.caloriemeter.purchase.Ishop;
import ir.eynakgroup.caloriemeter.util.DatabaseHandler;
import ir.eynakgroup.caloriemeter.util.JDF;
import ir.eynakgroup.caloriemeter.util.StaticMethods;

/**
 * Created by Navid on 10/12/2017.
 */

public abstract class BaseShopActivity extends AppCompatActivity implements Ishop, ShopPopup.ShopPopupClicks {

    public static final String MARKET_MONTHLY = "market_monthly";
    public static final String MARKET_YEARLY = "market_yearly";
    public static final String WEBSITE_MONTHLY = "website_monthly";
    public static final String WEBSITE_SEASONALLY = "website_seasonally";
    public static final String WEBSITE_COUPLE_SEASONALLY = "website_couple_seasonally";
    public static final String WEBSITE_LIFETIME = "website_lifetime";
    public static final String OPERATOR_SUBSCRIPTION = "operator_subscription";
    public static final Map<String, String> skuMap = new HashMap<>();
    private static final String TAG = BaseShopActivity.class.getSimpleName();
    public AbstractPurchase mPurchaser;
    public String mDiscountCode = "NA";
    public DialogFragment mShopPopup;
    protected AppCompatDialog mWaitingDialog;
    private DatabaseHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWaitingDialog = new AppCompatDialog(this);
        mWaitingDialog.setContentView(R.layout.purchase_waiting_dialog);
        mShopPopup = new ShopPopup();
        StaticMethods.overrideFonts(mWaitingDialog.findViewById(R.id.message_tv), StaticMethods.getTypeface(this, StaticMethods.fontsYekan));
        mPurchaser = new PaymentImpl(this);
        db = new DatabaseHandler(this);
        skuMap.put(MARKET_MONTHLY, "MonthSubs");
        skuMap.put(MARKET_YEARLY, "LifeTimeSubs");
        skuMap.put(WEBSITE_MONTHLY, "monthly");
        skuMap.put(WEBSITE_SEASONALLY, "seasonally");
        skuMap.put(WEBSITE_COUPLE_SEASONALLY, "coupleSeasonally");
        skuMap.put(WEBSITE_LIFETIME, "lifeTime");
        skuMap.put(OPERATOR_SUBSCRIPTION, "SKU_KARAFS_SUBSCRIPTION");

        Log.i(TAG, "oncreate");
        try {
            mPurchaser.setupIABHelper();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showInstallMarketDialog() {
        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
        alertDialog.setTitle("دریافت " + BuildConfig.MARKET_NAME);
        alertDialog.setMessage("برای خرید از فروشگاه باید ابتدا " + BuildConfig.MARKET_NAME + " را دریافت کنید.");
        alertDialog.setPositiveButton("دانلود " + BuildConfig.MARKET_NAME, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(BuildConfig.MARKET_URL));
                startActivity(i);
            }
        });
        alertDialog.setNegativeButton("بازگشت", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });
        // Set the Icon for the Dialog
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPurchaser.dispose();
    }

    @Override
    public void onIabSetupFinished(String status) {

    }

    public void onPurchaseButtonClicked(View v) {
        onPurchaseButtonClicked(v, mDiscountCode);
    }

    @Override
    public void onPurchaseButtonClicked(View v, String discountCode) {
        mPurchaser.launchPurchaseFlow(skuMap.get(v.getTag().toString()), discountCode);
    }

    @Override
    public void onQueryInventoryFinished(Inventory inventory) {
        if (inventory != null) {
            Log.i(TAG, inventory.toString());
            if (BuildConfig.FLAVOR.contains("websitePayment")) {
                // this method in Bazaar flavor alwayws returns true, so we must check the build flavor
                if (inventory.hasSubscription()) {
                    db.setUserPremium(true);
                    db.setAccessToAllVids();
                    return;
                }
            } else if (BuildConfig.FLAVOR.contains("bazaar") || BuildConfig.FLAVOR.contains("myket")) {
                db.takeAccessFromAllVids();
                db.setUserPremium(false);
                ArrayList<LocalPurchase> localPurchases = db.getAllPurchases();
                for (LocalPurchase p : localPurchases) {
                    Purchase purchase = inventory.getPurchase(p.getSKU());
                    boolean purchased = (purchase != null && mPurchaser.verifyDeveloperPayload(purchase));
                    if (purchased) {
                        Log.i("ShopActivity", p.toString());
                        setProduct(p.getProducts());
                    }
                }
            } else if (BuildConfig.FLAVOR.contains("operatorPayment")) {
                db.setUserPremium(false);
                db.takeAccessFromAllVids();
                db.setAccessToRecipes(false);
                Log.i(TAG, db.isUserPremium() + " : user premium");
                Purchase subscribePurchase = inventory.getPurchase(skuMap.get(OPERATOR_SUBSCRIPTION));
                if (subscribePurchase != null) {
                    db.setUserPremium(true);
                    db.setAccessToAllVids();
                    Log.i(TAG, db.isUserPremium() + " : user premium after check");
                }

            }
        } else {
            db.setUserPremium(false);
            db.takeAccessFromAllVids();
            db.setAccessToRecipes(false);
        }

    }

    @Override
    public void onCancelSubscriptionFinished(IabResult result) {

    }

    @Override
    public void onPurchaseFlowFinished(Purchase purchase) {
        if (purchase != null) {
            ArrayList<LocalPurchase> allLocalPurchases = db.getAllPurchases();
            db.setUserPremium(false);
            for (LocalPurchase p : allLocalPurchases) {
                if (purchase.getSku().equals(p.getSKU())) {
                    setProduct(p.getProducts());
                }
            }
            sendPurchaseToServer(purchase);
        }
    }

    private void sendPurchaseToServer(Purchase purchase) {
        if (BuildConfig.FLAVOR.contains("bazaar") || BuildConfig.FLAVOR.contains("myket")) {
            if (purchase.getSku().matches(skuMap.get(MARKET_MONTHLY))) {
                InternetFunctions.sendPurchaseToServer(this, purchase.getToken(), "1");
            } else if (purchase.getSku().matches(skuMap.get(MARKET_YEARLY))) {
                InternetFunctions.sendPurchaseToServer(this, purchase.getToken(), "2");
            }
        }
    }

    protected void setProduct(ArrayList<ir.eynakgroup.caloriemeter.shop.Product> products) {
        for (Product p : products)
            switch (p) {
                case FatVideos:
                    db.setAccessToVids(2);
                    break;
                case LegVideos:
                    db.setAccessToVids(1);
                    break;
                case StoVideos:
                    db.setAccessToVids(3);
                    break;
                case WorkoutVideos:
                    db.setAccessToVids(4);
                    db.setAccessToVids(5);
                    db.setAccessToVids(6);
                    db.setAccessToVids(7);
                    db.setAccessToVids(8);
                    db.setAccessToVids(9);
                    db.setAccessToVids(10);
                    db.setAccessToVids(11);
                    db.setAccessToVids(12);
                    db.setAccessToVids(13);
                    break;
                case FFCVideos:
                    db.setAccessToVids(-1);
                    break;
                case Recipes:
                    db.setAccessToRecipes(true);
                    break;
                case MonthlySubscription:
                    db.setUserPremium(true);
                    JDF jdf = new JDF();
                    jdf.nextDay(31);
                    db.updateUserExpirationDate(jdf.getIranianDate());
                    break;
                case YearlySubscription:
                    db.setUserPremium(true);
                    JDF jdf2 = new JDF();
                    jdf2.nextDay(365);
                    db.updateUserExpirationDate(jdf2.getIranianDate());
                    break;
                case LifetimeSubscription:
                    db.setUserPremium(true);
                    JDF jdf3 = new JDF();
                    jdf3.nextDay(10000);
                    db.updateUserExpirationDate(jdf3.getIranianDate());
                    break;

            }
    }

    @Override
    public void onPopupPurchaseClick(String sku) {
        mPurchaser.launchPurchaseFlow(sku, mDiscountCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mPurchaser.getHelper().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    public void showWaiting(boolean show) {
        if (show && !mWaitingDialog.isShowing())
            mWaitingDialog.show();
        else if (!show)
            mWaitingDialog.dismiss();
    }

    public void showShopPopup() {
        if (BuildConfig.FLAVOR.contains("operatorPayment")) {
            mPurchaser.launchPurchaseFlow(skuMap.get(OPERATOR_SUBSCRIPTION), "");
        } else {
            if (!db.isUserPremium() && !mShopPopup.isVisible() && !mShopPopup.isAdded())
                mShopPopup.show(getFragmentManager(), "");
        }
    }
}