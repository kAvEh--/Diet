package ir.eynakgroup.diet.purchase.util;

import java.util.HashMap;
import java.util.Map;

import ir.eynakgroup.caloriemeter.shop.BaseShopActivity;

/**
 * Created by Navid on 5/12/2017.
 */

public class Inventory {
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAILED = "failed";
    Map<String, Purchase> mPurchaseMap = new HashMap<>();
    String mStatus;

    public Inventory(Purchase... purchases) {
        for (Purchase purchase : purchases) {
            mPurchaseMap.put(purchase.getSku(), purchase);
        }
    }

    public Purchase getPurchase(String sku) {
        return mPurchaseMap.get(sku);
    }

    public boolean hasSubscription() {
        return getPurchase(BaseShopActivity.skuMap.get(BaseShopActivity.WEBSITE_MONTHLY)) != null;
    }

    public String getmExpirationDate() {
        Purchase purchase = mPurchaseMap.get(BaseShopActivity.skuMap.get(BaseShopActivity.WEBSITE_MONTHLY));
        if (purchase != null)
            return purchase.getExpirationDate();
        return "";
    }

    public String getmStatus() {
        return mStatus;
    }
}
