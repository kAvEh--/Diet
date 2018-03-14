package ir.eynakgroup.caloriemeter.purchase;

import android.view.View;

import ir.eynakgroup.caloriemeter.payment.util.IabResult;
import ir.eynakgroup.caloriemeter.payment.util.Inventory;
import ir.eynakgroup.caloriemeter.payment.util.Purchase;

/**
 * Created by Navid on 5/12/2017.
 */

public interface Ishop {
    void onIabSetupFinished(String status);

    void onQueryInventoryFinished(Inventory inventory);

    void onPurchaseFlowFinished(Purchase purchase);

    void onPurchaseButtonClicked(View v, String discountCode);

    void onCancelSubscriptionFinished(IabResult result);

    void showWaiting(boolean show);

    void showInstallMarketDialog();
}
